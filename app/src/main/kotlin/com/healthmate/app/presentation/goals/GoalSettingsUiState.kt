package com.healthmate.app.presentation.goals

data class GoalSettingsUiState(
    val stepsGoal: String = "",
    val caloriesGoal: String = "",
    val waterGoal: String = "",
    
    val stepsError: String? = null,
    val caloriesError: String? = null,
    val waterError: String? = null,
    
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false
)