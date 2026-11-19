package com.healthmate.app.presentation.editrecord

import java.time.LocalDate

data class EditRecordUiState(
    val id: Long = 0,
    val date: LocalDate = LocalDate.now(),
    val steps: String = "",
    val calories: String = "",
    val water: String = "",
    
    val stepsError: String? = null,
    val caloriesError: String? = null,
    val waterError: String? = null,
    val dateError: String? = null,
    
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val saveError: String? = null,
    val saveSuccess: Boolean = false,
    val deleteSuccess: Boolean = false,
    
    val showDeleteDialog: Boolean = false
)