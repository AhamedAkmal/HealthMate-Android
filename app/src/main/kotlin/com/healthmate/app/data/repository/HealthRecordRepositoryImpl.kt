package com.healthmate.app.data.repository

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.util.Log
import com.healthmate.app.data.local.DatabaseConstants
import com.healthmate.app.data.local.HealthDatabaseHelper
import com.healthmate.app.domain.model.HealthRecord
import com.healthmate.app.domain.repository.HealthRecordRepository
import com.healthmate.app.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class HealthRecordRepositoryImpl(
    private val dbHelper: HealthDatabaseHelper
) : HealthRecordRepository {

    private val TAG = "HealthRecordRepo"

    // ==========================================
    // CREATE
    // ==========================================
    override suspend fun insertRecord(record: HealthRecord): Result<Long> = withContext(Dispatchers.IO) {
        try {
            val db = dbHelper.writableDatabase
            val values = record.toContentValues()

            // insert() returns -1 if it fails without throwing an exception
            val id = db.insert(DatabaseConstants.TABLE_HEALTH_RECORDS, null, values)
            
            if (id == -1L) {
                Result.Error(Exception("Failed to insert record"))
            } else {
                Result.Success(id)
            }
        } catch (e: SQLiteException) {
            // Catches UNIQUE constraint failures (duplicate dates) or CHECK failures
            Log.e(TAG, "SQLite error inserting record", e)
            Result.Error(e)
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error inserting record", e)
            Result.Error(e)
        }
    }

    // ==========================================
    // UPDATE
    // ==========================================
    override suspend fun updateRecord(record: HealthRecord): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val db = dbHelper.writableDatabase
            val values = record.toContentValues()

            // Update the record where the ID matches
            val rowsAffected = db.update(
                DatabaseConstants.TABLE_HEALTH_RECORDS,
                values,
                "${DatabaseConstants.COLUMN_ID} = ?",
                arrayOf(record.id.toString())
            )
            Result.Success(rowsAffected)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating record", e)
            Result.Error(e)
        }
    }

    // ==========================================
    // DELETE
    // ==========================================
    override suspend fun deleteRecord(id: Long): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val db = dbHelper.writableDatabase
            val rowsAffected = db.delete(
                DatabaseConstants.TABLE_HEALTH_RECORDS,
                "${DatabaseConstants.COLUMN_ID} = ?",
                arrayOf(id.toString())
            )
            Result.Success(rowsAffected)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting record", e)
            Result.Error(e)
        }
    }

    // ==========================================
    // READ: ALL RECORDS
    // ==========================================
    override suspend fun getAllRecords(): Result<List<HealthRecord>> = withContext(Dispatchers.IO) {
        try {
            val db = dbHelper.readableDatabase
            val cursor = db.query(
                DatabaseConstants.TABLE_HEALTH_RECORDS,
                null, null, null, null, null,
                "${DatabaseConstants.COLUMN_DATE} DESC" // Order by date descending
            )
            
            val records = cursor.use { it.toHealthRecordList() }
            Result.Success(records)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting all records", e)
            Result.Error(e)
        }
    }

    // ==========================================
    // READ: BY ID
    // ==========================================
    override suspend fun getRecordById(id: Long): Result<HealthRecord?> = withContext(Dispatchers.IO) {
        try {
            val db = dbHelper.readableDatabase
            val cursor = db.query(
                DatabaseConstants.TABLE_HEALTH_RECORDS,
                null,
                "${DatabaseConstants.COLUMN_ID} = ?",
                arrayOf(id.toString()),
                null, null, null
            )
            
            val record = cursor.use { if (it.moveToFirst()) it.toHealthRecord() else null }
            Result.Success(record)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting record by ID", e)
            Result.Error(e)
        }
    }

    // ==========================================
    // READ: BY DATE
    // ==========================================
    override suspend fun getRecordByDate(date: String): Result<HealthRecord?> = withContext(Dispatchers.IO) {
        try {
            val db = dbHelper.readableDatabase
            val cursor = db.query(
                DatabaseConstants.TABLE_HEALTH_RECORDS,
                null,
                "${DatabaseConstants.COLUMN_DATE} = ?",
                arrayOf(date),
                null, null, null
            )
            
            val record = cursor.use { if (it.moveToFirst()) it.toHealthRecord() else null }
            Result.Success(record)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting record by date", e)
            Result.Error(e)
        }
    }

    // ==========================================
    // OBSERVE (Flow)
    // ==========================================
    override fun observeAllRecords(): Flow<List<HealthRecord>> = flow {
        // A simple polling flow for now. When Room is used, this would be reactive.
        // For native SQLite, we manually fetch every 2 seconds to keep UI updated.
        while (true) {
            when (val result = getAllRecords()) {
                is Result.Success -> emit(result.data)
                is Result.Error -> emit(emptyList())
            }
            kotlinx.coroutines.delay(2000) // Update every 2 seconds
        }
    }.flowOn(Dispatchers.IO)

    // ==========================================
    // MAPPERS (Extension Functions)
    // ==========================================

    private fun HealthRecord.toContentValues(): ContentValues {
        return ContentValues().apply {
            put(DatabaseConstants.COLUMN_DATE, date)
            put(DatabaseConstants.COLUMN_STEPS, steps)
            put(DatabaseConstants.COLUMN_CALORIES, calories)
            put(DatabaseConstants.COLUMN_WATER, water)
        }
    }

    private fun Cursor.toHealthRecord(): HealthRecord {
        return HealthRecord(
            id = getLong(getColumnIndexOrThrow(DatabaseConstants.COLUMN_ID)),
            date = getString(getColumnIndexOrThrow(DatabaseConstants.COLUMN_DATE)),
            steps = getInt(getColumnIndexOrThrow(DatabaseConstants.COLUMN_STEPS)),
            calories = getInt(getColumnIndexOrThrow(DatabaseConstants.COLUMN_CALORIES)),
            water = getInt(getColumnIndexOrThrow(DatabaseConstants.COLUMN_WATER))
        )
    }

    private fun Cursor.toHealthRecordList(): List<HealthRecord> {
        val list = mutableListOf<HealthRecord>()
        while (moveToNext()) {
            list.add(toHealthRecord())
        }
        return list
    }
}