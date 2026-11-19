package com.healthmate.app.presentation.dashboard

import com.healthmate.app.domain.model.HealthRecord

data class DashboardUiState(
    val isLoading: Boolean = true,
    val todayRecord: HealthRecord? = null,
    val error: String? = null,
    val todayDate: String = "" // Will be set by the ViewModel
)