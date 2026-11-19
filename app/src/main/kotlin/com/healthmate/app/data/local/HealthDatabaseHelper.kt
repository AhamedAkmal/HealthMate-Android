package com.healthmate.app.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class HealthDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DatabaseConstants.DATABASE_NAME,
    null,
    DatabaseConstants.DATABASE_VERSION
) {

    private val TAG = "HealthDatabaseHelper"

    // Called only when the database file is created for the very first time
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DatabaseConstants.SQL_CREATE_HEALTH_RECORDS_TABLE)
        Log.d(TAG, "Database created and table '${DatabaseConstants.TABLE_HEALTH_RECORDS}' initialized.")
        
        seedInitialData(db)
    }

    // Called when the database version number is increased in DatabaseConstants
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "Database upgrade requested from $oldVersion to $newVersion.")
        
        // For Module 03, we use a safe default.
        // We drop the old table and recreate it. 
        // In a production app with real user data, we would use ALTER TABLE to preserve data.
        db?.execSQL(DatabaseConstants.SQL_DROP_HEALTH_RECORDS_TABLE)
        onCreate(db)
    }

    // Insert the 5 initial records
    private fun seedInitialData(db: SQLiteDatabase?) {
        if (db == null) return

        // Use a transaction to ensure all 5 records are inserted, or none at all.
        db.beginTransaction()
        try {
            for (record in DatabaseConstants.INITIAL_DUMMY_DATA) {
                val values = ContentValues().apply {
                    put(DatabaseConstants.COLUMN_DATE, record.date)
                    put(DatabaseConstants.COLUMN_STEPS, record.steps)
                    put(DatabaseConstants.COLUMN_CALORIES, record.calories)
                    put(DatabaseConstants.COLUMN_WATER, record.water)
                }
                db.insert(DatabaseConstants.TABLE_HEALTH_RECORDS, null, values)
            }
            db.setTransactionSuccessful()
            Log.d(TAG, "Initial dummy data seeded successfully.")
        } catch (e: Exception) {
            Log.e(TAG, "Error seeding initial data", e)
        } finally {
            db.endTransaction()
        }
    }

    
    
}