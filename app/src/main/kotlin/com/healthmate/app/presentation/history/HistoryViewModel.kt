package com.healthmate.app.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthmate.app.domain.repository.HealthRecordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class HistoryViewModel(
    private val repository: HealthRecordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        // Start observing the database as soon as the ViewModel is created
        observeRecords()
    }

    private fun observeRecords() {
        viewModelScope.launch {
            repository.observeAllRecords().collect { records ->
                // Update the state with the new records
                _uiState.update { currentState ->
                    currentState.copy(
                        allRecords = records,
                        isLoading = false,
                        error = null
                    )
                }
            }
        }
    }

    fun onFilterDateChange(date: LocalDate?) {
        _uiState.update { it.copy(filterDate = date) }
    }

    fun clearFilter() {
        _uiState.update { it.copy(filterDate = null) }
    }
}