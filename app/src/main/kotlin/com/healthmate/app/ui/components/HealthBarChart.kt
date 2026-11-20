package com.healthmate.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthmate.app.domain.model.DailyDataPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HealthBarChart(
    data: List<DailyDataPoint>,
    valueExtractor: (DailyDataPoint) -> Int,
    barColor: Color,
    modifier: Modifier = Modifier
) {
    val maxValue = data.maxOfOrNull { valueExtractor(it) }?.coerceAtLeast(1) ?: 1

    Column(modifier = modifier.fillMaxWidth()) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(top = 16.dp, bottom = 8.dp)
        ) {
            val barWidth = size.width / (data.size * 2)
            val maxBarHeight = size.height

            data.forEachIndexed { index, point ->
                val value = valueExtractor(point)
                val barHeight = (value.toFloat() / maxValue) * maxBarHeight

                // X position (center of each slot)
                val xPos = (index * 2 * barWidth) + barWidth

                // Draw the bar
                drawRect(
                    color = if (point.hasRecord) barColor else barColor.copy(alpha = 0.2f),
                    topLeft = Offset(xPos, maxBarHeight - barHeight),
                    size = Size(barWidth, barHeight)
                )
            }
        }

        // Day labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceAround
        ) {
            data.forEach { point ->
                // Convert "2026-11-19" to a 3-letter day (e.g., "Wed")
                val dayLabel = try {
                    LocalDate.parse(point.date).format(DateTimeFormatter.ofPattern("EEE"))
                } catch (e: Exception) {
                    point.date.takeLast(2)
                }

                Text(
                    text = dayLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}