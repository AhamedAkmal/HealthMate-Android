package com.healthmate.app.data.local

object DatabaseConstants {
    // Database Info
    const val DATABASE_NAME = "healthmate.db"
    const val DATABASE_VERSION = 1
        // Initial Dummy Data (5 records)
    // Array format: [date, steps, calories, water]
    // Initial Dummy Data (5 records)
    data class DummyRecord(
        val date: String,
        val steps: Int,
        val calories: Int,
        val water: Int
    )

    val INITIAL_DUMMY_DATA = listOf(
        DummyRecord("2026-11-13", 6500, 320, 1800),
        DummyRecord("2026-11-14", 8200, 410, 2200),
        DummyRecord("2026-11-15", 5400, 270, 1500),
        DummyRecord("2026-11-16", 9100, 460, 2400),
        DummyRecord("2026-11-17", 7300, 360, 2000)
    )

    // Table Info
    const val TABLE_HEALTH_RECORDS = "health_records"

    // Columns
    const val COLUMN_ID = "id"
    const val COLUMN_DATE = "date"
    const val COLUMN_STEPS = "steps"
    const val COLUMN_CALORIES = "calories"
    const val COLUMN_WATER = "water"

    // SQL Statements
    const val SQL_CREATE_HEALTH_RECORDS_TABLE = """
        CREATE TABLE $TABLE_HEALTH_RECORDS (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_DATE TEXT NOT NULL UNIQUE,
            $COLUMN_STEPS INTEGER NOT NULL CHECK($COLUMN_STEPS >= 0),
            $COLUMN_CALORIES INTEGER NOT NULL CHECK($COLUMN_CALORIES >= 0),
            $COLUMN_WATER INTEGER NOT NULL CHECK($COLUMN_WATER >= 0)
        )
    """

    const val SQL_DROP_HEALTH_RECORDS_TABLE = "DROP TABLE IF EXISTS $TABLE_HEALTH_RECORDS"
}