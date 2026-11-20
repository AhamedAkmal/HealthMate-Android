package com.healthmate.app.domain.model

data class DailyDataPoint(
    val date: String, // YYYY-MM-DD
    val steps: Int,
    val calories: Int,
    val water: Int,
    val hasRecord: Boolean // Distinguishes missing data from 0 values
)

data class WeeklyStats(
    val dataPoints: List<DailyDataPoint>,
    
    val totalSteps: Int,
    val totalCalories: Int,
    val totalWater: Int,
    
    val avgSteps: Int,
    val avgCalories: Int,
    val avgWater: Int,
    
    val startDate: String,
    val endDate: String
)