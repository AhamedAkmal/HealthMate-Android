package com.healthmate.app.domain.model

/**
 * Domain model representing a daily health record.
 * This is a pure Kotlin class with no Android or SQLite dependencies.
 */
data class HealthRecord(
    val id: Long = 0, // Default to 0 for new records before DB insertion
    val date: String, // Format: YYYY-MM-DD
    val steps: Int,
    val calories: Int,
    val water: Int
)