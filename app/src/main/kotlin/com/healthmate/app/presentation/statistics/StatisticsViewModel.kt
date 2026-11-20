package com.healthmate.app.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthmate.app.domain.model.DailyDataPoint
import com.healthmate.app.domain.model.WeeklyStats
import com.healthmate.app.domain.repository.HealthRecordRepository
import com.healthmate.app.domain.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StatisticsViewModel(
    private val repository: HealthRecordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    init {
        loadWeeklyStats()
    }

    fun loadWeeklyStats() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val endDate = LocalDate.now()
            val startDate = endDate.minusDays(6)

            val startDateStr = startDate.toString()
            val endDateStr = endDate.toString()

            when (val result = repository.getRecordsBetweenDates(startDateStr, endDateStr)) {
                is Result.Success -> {
                    val records = result.data
                    val dataPoints = mutableListOf<DailyDataPoint>()

                    var totalSteps = 0
                    var totalCalories = 0
                    var totalWater = 0

                    // Loop exactly 7 days from startDate to endDate
                    for (i in 0..6) {
                        val currentDate = startDate.plusDays(i.toLong())
                        val dateStr = currentDate.toString()
                        val record = records.find { it.date == dateStr }

                        if (record != null) {
                            totalSteps += record.steps
                            totalCalories += record.calories
                            totalWater += record.water
                            dataPoints.add(
                                DailyDataPoint(
                                    date = dateStr,
                                    steps = record.steps,
                                    calories = record.calories,
                                    water = record.water,
                                    hasRecord = true
                                )
                            )
                        } else {
                            dataPoints.add(
                                DailyDataPoint(
                                    date = dateStr,
                                    steps = 0,
                                    calories = 0,
                                    water = 0,
                                    hasRecord = false
                                )
                            )
                        }
                    }

                    // Calculate averages using 7 calendar days
                    val avgSteps = totalSteps / 7
                    val avgCalories = totalCalories / 7
                    val avgWater = totalWater / 7

                    val stats = WeeklyStats(
                        dataPoints = dataPoints,
                        totalSteps = totalSteps,
                        totalCalories = totalCalories,
                        totalWater = totalWater,
                        avgSteps = avgSteps,
                        avgCalories = avgCalories,
                        avgWater = avgWater,
                        startDate = startDateStr,
                        endDate = endDateStr
                    )

                    _uiState.update {
                        it.copy(isLoading = false, weeklyStats = stats)
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Failed to load statistics")
                    }
                }
            }
        }
    }
}