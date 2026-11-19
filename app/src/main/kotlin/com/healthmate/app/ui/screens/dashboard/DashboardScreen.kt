package com.healthmate.app.ui.screens.dashboard

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
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.healthmate.app.ui.components.HealthMetricCard
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
    onAddRecordClick: () -> Unit = {}
) {
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

        Spacer(modifier = Modifier.height(8.dp))

        // Today's Activity
        SectionHeader(title = "Today's Activity")

        HealthMetricCard(
            title = "Steps",
            value = "0",
            unit = "steps",
            icon = Icons.Filled.DirectionsWalk,
            iconColor = StepsGreen,
            iconBackground = StepsGreenContainer
        )

        HealthMetricCard(
            title = "Calories",
            value = "0",
            unit = "kcal",
            icon = Icons.Filled.LocalFireDepartment,
            iconColor = CaloriesOrange,
            iconBackground = CaloriesContainer
        )

        HealthMetricCard(
            title = "Water",
            value = "0",
            unit = "ml",
            icon = Icons.Filled.WaterDrop,
            iconColor = WaterBlue,
            iconBackground = WaterContainer
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Daily Summary section with empty state
        SectionHeader(
            title = "Daily Summary",
            subtitle = "Your health data will appear here once you start recording"
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No records yet",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Add your first entry to start tracking your daily health.",
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