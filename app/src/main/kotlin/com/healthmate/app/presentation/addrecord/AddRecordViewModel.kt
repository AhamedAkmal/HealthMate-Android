package com.healthmate.app.presentation.addrecord

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthmate.app.domain.model.HealthRecord
import com.healthmate.app.domain.repository.HealthRecordRepository
import com.healthmate.app.domain.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddRecordViewModel(
    private val repository: HealthRecordRepository
) : ViewModel() {

    private val TAG = "AddRecordVM"

    private val _uiState = MutableStateFlow(AddRecordUiState())
    val uiState: StateFlow<AddRecordUiState> = _uiState.asStateFlow()

    // Input handlers
    fun onDateChange(date: LocalDate) {
        _uiState.update { it.copy(date = date, dateError = null, saveError = null) }
    }

    fun onStepsChange(steps: String) {
        _uiState.update { it.copy(steps = steps, stepsError = null, saveError = null) }
    }

    fun onCaloriesChange(calories: String) {
        _uiState.update { it.copy(calories = calories, caloriesError = null, saveError = null) }
    }

    fun onWaterChange(water: String) {
        _uiState.update { it.copy(water = water, waterError = null, saveError = null) }
    }

    // Save record
    fun saveRecord() {
        val currentState = _uiState.value
        if (currentState.isSaving) return // Prevent double submission

        // 1. Validate
        val validatedState = AddRecordValidator.validate(currentState)
        _uiState.value = validatedState

        if (!AddRecordValidator.isValid(validatedState)) {
            return // Stop if validation fails
        }

        // 2. Set loading state
        _uiState.update { it.copy(isSaving = true, saveError = null) }

        // 3. Call Repository
        viewModelScope.launch {
            val record = HealthRecord(
                date = currentState.date.toString(), // ISO 8601 (YYYY-MM-DD)
                steps = currentState.steps.toIntOrNull() ?: 0,
                calories = currentState.calories.toIntOrNull() ?: 0,
                water = currentState.water.toIntOrNull() ?: 0
            )

            when (val result = repository.insertRecord(record)) {
                is Result.Success -> {
                    _uiState.update { it.copy(isSaving = false, isSuccess = true) }
                }
                is Result.Error -> {
                    // Handle duplicate date error nicely
                    val errorMsg = if (result.exception.message?.contains("UNIQUE") == true) {
                        "A record already exists for this date. Please choose a different date."
                    } else {
                        "Failed to save record: ${result.exception.message}"
                    }
                    
                    Log.e(TAG, "Save failed", result.exception)
                    _uiState.update { it.copy(isSaving = false, saveError = errorMsg) }
                }
            }
        }
    }

    // Call this after the UI has handled the success (e.g., showing a Snackbar)
    fun consumeSuccess() {
        _uiState.update { 
            it.copy(
                isSuccess = false, 
                steps = "", 
                calories = "", 
                water = "", 
                date = LocalDate.now() 
            ) 
        }
    }

    // Call this to dismiss errors
    fun consumeError() {
        _uiState.update { it.copy(saveError = null) }
    }
}