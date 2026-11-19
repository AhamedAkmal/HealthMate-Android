package com.healthmate.app.presentation.addrecord

import java.time.LocalDate

data class AddRecordUiState(
    val date: LocalDate = LocalDate.now(),
    val steps: String = "",
    val calories: String = "",
    val water: String = "",
    
    // Validation errors
    val stepsError: String? = null,
    val caloriesError: String? = null,
    val waterError: String? = null,
    val dateError: String? = null,
    
    // Status states
    val isSaving: Boolean = false,
    val isSuccess: Boolean = false,
    val saveError: String? = null
)