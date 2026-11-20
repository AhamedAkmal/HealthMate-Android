package com.healthmate.app.presentation.goals

import androidx.lifecycle.ViewModel
import com.healthmate.app.domain.model.DailyGoal
import com.healthmate.app.domain.repository.GoalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GoalSettingsViewModel(
    private val goalRepository: GoalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GoalSettingsUiState())
    val uiState: StateFlow<GoalSettingsUiState> = _uiState.asStateFlow()

    init {
        loadGoals()
    }

    private fun loadGoals() {
        val existingGoals = goalRepository.getGoals()
        if (existingGoals != null) {
            _uiState.update {
                it.copy(
                    stepsGoal = existingGoals.stepsGoal.toString(),
                    caloriesGoal = existingGoals.caloriesGoal.toString(),
                    waterGoal = existingGoals.waterGoal.toString()
                )
            }
        }
    }

    fun onStepsGoalChange(value: String) {
        _uiState.update { it.copy(stepsGoal = value, stepsError = null) }
    }

    fun onCaloriesGoalChange(value: String) {
        _uiState.update { it.copy(caloriesGoal = value, caloriesError = null) }
    }

    fun onWaterGoalChange(value: String) {
        _uiState.update { it.copy(waterGoal = value, waterError = null) }
    }

    fun saveGoals() {
        val state = _uiState.value
        var hasError = false

        var newState = state

        if (state.stepsGoal.isBlank() || (state.stepsGoal.toIntOrNull() ?: 0) <= 0) {
            newState = newState.copy(stepsError = "Must be greater than 0")
            hasError = true
        }
        if (state.caloriesGoal.isBlank() || (state.caloriesGoal.toIntOrNull() ?: 0) <= 0) {
            newState = newState.copy(caloriesError = "Must be greater than 0")
            hasError = true
        }
        if (state.waterGoal.isBlank() || (state.waterGoal.toIntOrNull() ?: 0) <= 0) {
            newState = newState.copy(waterError = "Must be greater than 0")
            hasError = true
        }

        _uiState.update { newState }

        if (hasError) return

        val goal = DailyGoal(
            stepsGoal = newState.stepsGoal.toInt(),
            caloriesGoal = newState.caloriesGoal.toInt(),
            waterGoal = newState.waterGoal.toInt()
        )

        goalRepository.saveGoals(goal)
        _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
    }

    fun consumeSuccess() {
        _uiState.update { it.copy(saveSuccess = false) }
    }
}