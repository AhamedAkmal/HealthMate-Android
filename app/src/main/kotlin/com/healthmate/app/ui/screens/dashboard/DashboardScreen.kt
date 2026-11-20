package com.healthmate.app.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.healthmate.app.presentation.dashboard.DashboardViewModel
import com.healthmate.app.ui.components.ErrorMessage
import com.healthmate.app.ui.components.HealthMetricCard
import com.healthmate.app.ui.components.LoadingIndicator
import com.healthmate.app.ui.components.PrimaryButton
import com.healthmate.app.ui.components.SectionHeader
import com.healthmate.app.ui.theme.CaloriesContainer
import com.healthmate.app.ui.theme.CaloriesOrange
import com.healthmate.app.ui.theme.StepsGreen
import com.healthmate.app.ui.theme.StepsGreenContainer
import com.healthmate.app.ui.theme.WaterBlue
import com.healthmate.app.ui.theme.WaterContainer
import com.healthmate.app.util.DateFormatter

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onAddRecordClick: () -> Unit = {},
    onSetGoalsClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refreshDashboard()
    }

    if (uiState.isLoading) {
        LoadingIndicator()
        return
    }

    if (uiState.error != null) {
        ErrorMessage(message = uiState.error!!)
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Greeting
        Column {
            Text(
                text = "Welcome back",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = DateFormatter.todayDisplay(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionHeader(title = "Today's Activity")
            TextButton(onClick = onSetGoalsClick) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = null,
                    modifier = Modifier.width(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Goals")
            }
        }

        HealthMetricCard(
            title = "Steps",
            value = uiState.todayRecord?.steps?.toString() ?: "0",
            unit = "steps",
            icon = Icons.Filled.DirectionsWalk,
            iconColor = StepsGreen,
            iconBackground = StepsGreenContainer,
            progress = if (uiState.hasGoals) uiState.stepsProgress else null,
            goalText = if (uiState.hasGoals) "Goal: ${uiState.stepsGoal}" else null
        )

        HealthMetricCard(
            title = "Calories",
            value = uiState.todayRecord?.calories?.toString() ?: "0",
            unit = "kcal",
            icon = Icons.Filled.LocalFireDepartment,
            iconColor = CaloriesOrange,
            iconBackground = CaloriesContainer,
            progress = if (uiState.hasGoals) uiState.caloriesProgress else null,
            goalText = if (uiState.hasGoals) "Goal: ${uiState.caloriesGoal}" else null
        )

        HealthMetricCard(
            title = "Water",
            value = uiState.todayRecord?.water?.toString() ?: "0",
            unit = "ml",
            icon = Icons.Filled.WaterDrop,
            iconColor = WaterBlue,
            iconBackground = WaterContainer,
            progress = if (uiState.hasGoals) uiState.waterProgress else null,
            goalText = if (uiState.hasGoals) "Goal: ${uiState.waterGoal}" else null
        )

        if (!uiState.hasGoals) {
            Text(
                text = "Tap 'Goals' to set your daily targets and track your progress.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (uiState.todayRecord == null) {
            SectionHeader(
                title = "Daily Summary",
                subtitle = "No record found for today"
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No record for today",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Add your entry to start tracking today's health.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PrimaryButton(
                        text = "Add Record",
                        onClick = onAddRecordClick
                    )
                }
            }
        }
    }
}