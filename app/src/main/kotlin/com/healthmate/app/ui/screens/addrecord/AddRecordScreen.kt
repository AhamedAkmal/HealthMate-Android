package com.healthmate.app.ui.screens.addrecord

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.healthmate.app.ui.components.PrimaryButton
import com.healthmate.app.ui.components.SectionHeader
import com.healthmate.app.ui.theme.CaloriesOrange
import com.healthmate.app.ui.theme.StepsGreen
import com.healthmate.app.ui.theme.WaterBlue
import com.healthmate.app.util.DateFormatter
import com.healthmate.app.util.DatePickerUtils
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecordScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var steps by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var water by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionHeader(
                title = "Record Your Activity",
                subtitle = "Enter your health metrics for today or a past date"
            )

            // Date field (read-only, opens picker on tap)
            OutlinedTextField(
                value = DateFormatter.formatForDisplay(selectedDate.toString()),
                onValueChange = { },
                readOnly = true,
                label = { Text("Date") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription = "Select date"
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Filled.CalendarToday,
                            contentDescription = "Open date picker"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )

            // Steps field
            OutlinedTextField(
                value = steps,
                onValueChange = { steps = it.filter { ch -> ch.isDigit() } },
                label = { Text("Steps") },
                placeholder = { Text("e.g. 7850") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.DirectionsWalk,
                        contentDescription = null,
                        tint = StepsGreen
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )

            // Calories field
            OutlinedTextField(
                value = calories,
                onValueChange = { calories = it.filter { ch -> ch.isDigit() } },
                label = { Text("Calories burned") },
                placeholder = { Text("e.g. 420") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.LocalFireDepartment,
                        contentDescription = null,
                        tint = CaloriesOrange
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )

            // Water field
            OutlinedTextField(
                value = water,
                onValueChange = { water = it.filter { ch -> ch.isDigit() } },
                label = { Text("Water intake (ml)") },
                placeholder = { Text("e.g. 2000") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.WaterDrop,
                        contentDescription = null,
                        tint = WaterBlue
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            PrimaryButton(
                text = "Save Record",
                onClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            "Save functionality will be implemented in Module 05"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your data is stored locally on your device.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    // Date picker dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = DatePickerUtils.localDateToMillis(selectedDate)
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectedDate = DatePickerUtils.millisToLocalDate(millis)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
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