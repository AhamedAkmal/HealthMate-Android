package com.healthmate.app.presentation.history

import com.healthmate.app.domain.model.HealthRecord
import java.time.LocalDate

data class HistoryUiState(
    // The full list of records retrieved from the database
    val allRecords: List<HealthRecord> = emptyList(),
    
    // The current date being used to filter (null means "show all")
    val filterDate: LocalDate? = null,
    
    // Status flags
    val isLoading: Boolean = true,
    val error: String? = null
) {
    // A computed property that automatically calculates what to display
    val displayedRecords: List<HealthRecord>
        get() = if (filterDate == null) {
            allRecords
        } else {
            allRecords.filter { it.date == filterDate.toString() }
        }
}