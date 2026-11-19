package com.healthmate.app.ui.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.healthmate.app.presentation.history.HistoryViewModel
import com.healthmate.app.ui.components.EmptyState
import com.healthmate.app.ui.components.ErrorMessage
import com.healthmate.app.ui.components.HealthRecordCard
import com.healthmate.app.ui.components.LoadingIndicator
import com.healthmate.app.ui.components.SectionHeader
import com.healthmate.app.util.DateFormatter
import com.healthmate.app.util.DatePickerUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        SectionHeader(
            title = "Health Records",
            subtitle = "Search and review your past entries"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Date filter field
        OutlinedTextField(
            value = uiState.filterDate?.let { DateFormatter.formatForDisplay(it.toString()) } ?: "",
            onValueChange = { },
            readOnly = true,
            label = { Text("Filter by date") },
            placeholder = { Text("All records") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Filter"
                )
            },
            trailingIcon = {
                if (uiState.filterDate != null) {
                    IconButton(onClick = { viewModel.clearFilter() }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear filter"
                        )
                    }
                } else {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Filled.CalendarToday,
                            contentDescription = "Pick date"
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Screen Content based on state
        Box(modifier = Modifier.weight(1f)) {
            when {
                uiState.isLoading -> LoadingIndicator()

                uiState.error != null -> ErrorMessage(message = uiState.error!!)

                uiState.displayedRecords.isEmpty() -> {
                    EmptyState(
                        title = "No records found",
                        message = if (uiState.filterDate != null) {
                            "No record exists for the selected date. Clear the filter to see all records."
                        } else {
                            "Your health records will appear here once you start adding entries."
                        }
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.displayedRecords) { record ->
                            HealthRecordCard(
                                dateIso = record.date,
                                steps = record.steps,
                                calories = record.calories,
                                water = record.water
                            )
                        }
                    }
                }
            }
        }
    }

    // Date picker dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.filterDate?.let { DatePickerUtils.localDateToMillis(it) }
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            viewModel.onFilterDateChange(DatePickerUtils.millisToLocalDate(millis))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Filter")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}