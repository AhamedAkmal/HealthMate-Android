package com.healthmate.app.presentation.dashboard

import com.healthmate.app.domain.model.HealthRecord

data class DashboardUiState(
    val isLoading: Boolean = true,
    val todayRecord: HealthRecord? = null,
    val error: String? = null,
    val todayDate: String = "",
    
    // Goal data
    val hasGoals: Boolean = false,
    val stepsGoal: Int = 0,
    val caloriesGoal: Int = 0,
    val waterGoal: Int = 0,
    
    // Progress percentages (0 to >100)
    val stepsProgress: Float = 0f,
    val caloriesProgress: Float = 0f,
    val waterProgress: Float = 0f
)