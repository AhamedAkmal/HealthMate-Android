package com.healthmate.app.ui.screens.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.healthmate.app.presentation.statistics.StatisticsViewModel
import com.healthmate.app.ui.components.ErrorMessage
import com.healthmate.app.ui.components.HealthBarChart
import com.healthmate.app.ui.components.LoadingIndicator
import com.healthmate.app.ui.components.SectionHeader
import com.healthmate.app.ui.theme.CaloriesOrange
import com.healthmate.app.ui.theme.StepsGreen
import com.healthmate.app.ui.theme.WaterBlue
import com.healthmate.app.util.DateFormatter

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Refresh every time the screen is opened
    LaunchedEffect(Unit) {
        viewModel.loadWeeklyStats()
    }

    if (uiState.isLoading) {
        LoadingIndicator()
        return
    }

    if (uiState.error != null) {
        ErrorMessage(message = uiState.error!!)
        return
    }

    val stats = uiState.weeklyStats ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Date Range Display
        SectionHeader(
            title = "Weekly Statistics",
            subtitle = "${DateFormatter.formatForDisplay(stats.startDate)} to ${DateFormatter.formatForDisplay(stats.endDate)}"
        )

        // Summary Totals
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SummaryCard(title = "Steps", value = stats.totalSteps.toString(), modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.padding(4.dp))
            SummaryCard(title = "Calories", value = stats.totalCalories.toString(), modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.padding(4.dp))
            SummaryCard(title = "Water", value = stats.totalWater.toString(), modifier = Modifier.weight(1f))
        }

        // Averages
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SummaryCard(title = "Avg Steps", value = stats.avgSteps.toString(), modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.padding(4.dp))
            SummaryCard(title = "Avg Cal", value = stats.avgCalories.toString(), modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.padding(4.dp))
            SummaryCard(title = "Avg Water", value = stats.avgWater.toString(), modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Charts
        SectionHeader(title = "Daily Trends")
        
        ChartCard(title = "Steps", color = StepsGreen) {
            HealthBarChart(data = stats.dataPoints, valueExtractor = { it.steps }, barColor = StepsGreen)
        }
        
        ChartCard(title = "Calories", color = CaloriesOrange) {
            HealthBarChart(data = stats.dataPoints, valueExtractor = { it.calories }, barColor = CaloriesOrange)
        }
        
        ChartCard(title = "Water", color = WaterBlue) {
            HealthBarChart(data = stats.dataPoints, valueExtractor = { it.water }, barColor = WaterBlue)
        }
    }
}

@Composable
private fun SummaryCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun ChartCard(title: String, color: androidx.compose.ui.graphics.Color, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleSmall, color = color)
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}