package com.healthmate.app.presentation.editrecord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthmate.app.domain.model.HealthRecord
import com.healthmate.app.domain.repository.HealthRecordRepository
import com.healthmate.app.domain.util.Result
import com.healthmate.app.presentation.addrecord.AddRecordValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class EditRecordViewModel(
    private val repository: HealthRecordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditRecordUiState())
    val uiState: StateFlow<EditRecordUiState> = _uiState.asStateFlow()

    fun loadRecord(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, id = id) }
            when (val result = repository.getRecordById(id)) {
                is Result.Success -> {
                    val record = result.data
                    if (record != null) {
                        _uiState.update {
                            it.copy(
                                date = LocalDate.parse(record.date),
                                steps = record.steps.toString(),
                                calories = record.calories.toString(),
                                water = record.water.toString(),
                                isLoading = false
                            )
                        }
                    } else {
                        _uiState.update { it.copy(isLoading = false, saveError = "Record not found") }
                    }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, saveError = "Failed to load record") }
                }
            }
        }
    }

    fun onDateChange(date: LocalDate) = _uiState.update { it.copy(date = date, dateError = null, saveError = null) }
    fun onStepsChange(steps: String) = _uiState.update { it.copy(steps = steps, stepsError = null, saveError = null) }
    fun onCaloriesChange(calories: String) = _uiState.update { it.copy(calories = calories, caloriesError = null, saveError = null) }
    fun onWaterChange(water: String) = _uiState.update { it.copy(water = water, waterError = null, saveError = null) }

    fun saveChanges() {
        val currentState = _uiState.value
        if (currentState.isSaving) return
        
        // Reuse the validator logic from AddRecord
        val validatedState = AddRecordValidator.validate(
            com.healthmate.app.presentation.addrecord.AddRecordUiState(
                date = currentState.date,
                steps = currentState.steps,
                calories = currentState.calories,
                water = currentState.water
            )
        )

        _uiState.update {
            it.copy(
                dateError = validatedState.dateError,
                stepsError = validatedState.stepsError,
                caloriesError = validatedState.caloriesError,
                waterError = validatedState.waterError
            )
        }

        if (!AddRecordValidator.isValid(validatedState)) return

        _uiState.update { it.copy(isSaving = true, saveError = null) }

        viewModelScope.launch {
            val record = HealthRecord(
                id = currentState.id,
                date = currentState.date.toString(),
                steps = currentState.steps.toIntOrNull() ?: 0,
                calories = currentState.calories.toIntOrNull() ?: 0,
                water = currentState.water.toIntOrNull() ?: 0
            )

            when (val result = repository.updateRecord(record)) {
                is Result.Success -> {
                    if (result.data > 0) {
                        _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
                    } else {
                        _uiState.update { it.copy(isSaving = false, saveError = "Record not found") }
                    }
                }
                is Result.Error -> {
                    val errorMsg = if (result.exception.message?.contains("UNIQUE") == true) {
                        "A record already exists for this date."
                    } else {
                        "Update failed: ${result.exception.message}"
                    }
                    _uiState.update { it.copy(isSaving = false, saveError = errorMsg) }
                }
            }
        }
    }

    fun showDeleteDialog() = _uiState.update { it.copy(showDeleteDialog = true) }
    fun hideDeleteDialog() = _uiState.update { it.copy(showDeleteDialog = false) }

    fun deleteRecord() {
        val id = _uiState.value.id
        if (id == 0L) return
        
        _uiState.update { it.copy(isSaving = true, showDeleteDialog = false) }
        
        viewModelScope.launch {
            when (val result = repository.deleteRecord(id)) {
                is Result.Success -> {
                    _uiState.update { it.copy(isSaving = false, deleteSuccess = true) }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isSaving = false, saveError = "Delete failed") }
                }
            }
        }
    }

    fun consumeSaveSuccess() = _uiState.update { it.copy(saveSuccess = false) }
    fun consumeDeleteSuccess() = _uiState.update { it.copy(deleteSuccess = false) }
    fun consumeError() = _uiState.update { it.copy(saveError = null) }
}