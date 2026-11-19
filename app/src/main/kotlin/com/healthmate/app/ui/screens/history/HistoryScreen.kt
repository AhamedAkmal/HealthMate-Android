package com.healthmate.app.ui.screens.history

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.healthmate.app.ui.components.EmptyState
import com.healthmate.app.ui.components.HealthRecordCard
import com.healthmate.app.ui.components.SectionHeader
import com.healthmate.app.util.DateFormatter
import com.healthmate.app.util.DatePickerUtils
import java.time.LocalDate

data class HistoryRecord(
    val date: String,
    val steps: Int,
    val calories: Int,
    val water: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen() {
    // Placeholder data — replaced by database in Module 03+
    val sampleRecords = remember {
        val today = LocalDate.now()
        listOf(
            HistoryRecord(today.toString(), 7850, 420, 2000),
            HistoryRecord(today.minusDays(1).toString(), 6200, 380, 1800),
            HistoryRecord(today.minusDays(2).toString(), 9100, 510, 2400)
        )
    }

    var filterDate by remember { mutableStateOf<LocalDate?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    val filteredRecords = remember(filterDate, sampleRecords) {
        if (filterDate == null) sampleRecords
        else sampleRecords.filter { it.date == filterDate.toString() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        // Fixed header section
        SectionHeader(
            title = "Health Records",
            subtitle = "Search and review your past entries"
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = filterDate?.let { DateFormatter.formatForDisplay(it.toString()) } ?: "",
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
                if (filterDate != null) {
                    IconButton(onClick = { filterDate = null }) {
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

        // Scrollable content region — takes remaining space via weight(1f)
        if (filteredRecords.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                EmptyState(
                    title = "No records found",
                    message = if (filterDate != null) {
                        "No record exists for the selected date. Clear the filter to see all records."
                    } else {
                        "Your health records will appear here once you start adding entries."
                    }
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredRecords) { record ->
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

    // Date picker dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            filterDate = DatePickerUtils.millisToLocalDate(millis)
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