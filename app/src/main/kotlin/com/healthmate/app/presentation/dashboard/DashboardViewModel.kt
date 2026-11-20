package com.healthmate.app.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthmate.app.domain.repository.GoalRepository
import com.healthmate.app.domain.repository.HealthRecordRepository
import com.healthmate.app.domain.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class DashboardViewModel(
    private val healthRecordRepository: HealthRecordRepository,
    private val goalRepository: GoalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        refreshDashboard()
    }

    fun refreshDashboard() {
        val todayDate = LocalDate.now().toString()
        val goals = goalRepository.getGoals()
        val hasGoals = goals != null

        _uiState.update {
            it.copy(
                isLoading = true,
                todayDate = todayDate,
                error = null,
                hasGoals = hasGoals,
                stepsGoal = goals?.stepsGoal ?: 0,
                caloriesGoal = goals?.caloriesGoal ?: 0,
                waterGoal = goals?.waterGoal ?: 0
            )
        }

        viewModelScope.launch {
            when (val result = healthRecordRepository.getRecordByDate(todayDate)) {
                is Result.Success -> {
                    val record = result.data
                    var stepsProg = 0f
                    var calProg = 0f
                    var waterProg = 0f

                    if (record != null && hasGoals && goals != null) {
                        if (goals.stepsGoal > 0) stepsProg = (record.steps.toFloat() / goals.stepsGoal) * 100f
                        if (goals.caloriesGoal > 0) calProg = (record.calories.toFloat() / goals.caloriesGoal) * 100f
                        if (goals.waterGoal > 0) waterProg = (record.water.toFloat() / goals.waterGoal) * 100f
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            todayRecord = record,
                            stepsProgress = stepsProg,
                            caloriesProgress = calProg,
                            waterProgress = waterProg
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