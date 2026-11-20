package com.healthmate.app.ui.screens.goals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.healthmate.app.presentation.goals.GoalSettingsViewModel
import com.healthmate.app.ui.components.PrimaryButton
import com.healthmate.app.ui.components.SectionHeader
import com.healthmate.app.ui.theme.CaloriesOrange
import com.healthmate.app.ui.theme.StepsGreen
import com.healthmate.app.ui.theme.WaterBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalSettingsScreen(
    viewModel: GoalSettingsViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle success event
    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            snackbarHostState.showSnackbar("Goals saved successfully!")
            viewModel.consumeSuccess()
            onNavigateBack()
        }
    }

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
                title = "Daily Goals",
                subtitle = "Set your daily targets to track progress"
            )

            // Steps Goal field
            OutlinedTextField(
                value = uiState.stepsGoal,
                onValueChange = viewModel::onStepsGoalChange,
                label = { Text("Steps Goal") },
                placeholder = { Text("e.g. 10000") },
                isError = uiState.stepsError != null,
                supportingText = uiState.stepsError?.let { { Text(it) } },
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

            // Calories Goal field
            OutlinedTextField(
                value = uiState.caloriesGoal,
                onValueChange = viewModel::onCaloriesGoalChange,
                label = { Text("Calories Goal (kcal)") },
                placeholder = { Text("e.g. 500") },
                isError = uiState.caloriesError != null,
                supportingText = uiState.caloriesError?.let { { Text(it) } },
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

            // Water Goal field
            OutlinedTextField(
                value = uiState.waterGoal,
                onValueChange = viewModel::onWaterGoalChange,
                label = { Text("Water Goal (ml)") },
                placeholder = { Text("e.g. 2000") },
                isError = uiState.waterError != null,
                supportingText = uiState.waterError?.let { { Text(it) } },
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
                text = if (uiState.isSaving) "Saving..." else "Save Goals",
                onClick = { viewModel.saveGoals() },
                enabled = !uiState.isSaving
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "These goals are used to calculate your daily progress on the Dashboard.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}