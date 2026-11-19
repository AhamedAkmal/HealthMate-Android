package com.healthmate.app.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthmate.app.domain.repository.HealthRecordRepository
import com.healthmate.app.domain.util.Result
import com.healthmate.app.util.DateFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class DashboardViewModel(
    private val repository: HealthRecordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        refreshDashboard()
    }

    fun refreshDashboard() {
        val todayDate = LocalDate.now().toString() // YYYY-MM-DD
        _uiState.update { it.copy(isLoading = true, todayDate = todayDate, error = null) }

        viewModelScope.launch {
            when (val result = repository.getRecordByDate(todayDate)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            todayRecord = result.data
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Failed to load today's record"
                        )
                    }
                }
            }
        }
    }
}