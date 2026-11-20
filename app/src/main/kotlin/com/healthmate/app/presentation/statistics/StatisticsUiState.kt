package com.healthmate.app.presentation.statistics

import com.healthmate.app.domain.model.WeeklyStats

data class StatisticsUiState(
    val isLoading: Boolean = true,
    val weeklyStats: WeeklyStats? = null,
    val error: String? = null
)