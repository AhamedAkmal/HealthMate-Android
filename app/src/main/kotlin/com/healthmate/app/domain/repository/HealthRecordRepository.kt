package com.healthmate.app.domain.repository

import com.healthmate.app.domain.model.HealthRecord
import com.healthmate.app.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface HealthRecordRepository {
    
    // --- CRUD Operations ---
    
    suspend fun insertRecord(record: HealthRecord): Result<Long>
    
    suspend fun updateRecord(record: HealthRecord): Result<Int>
    
    suspend fun deleteRecord(id: Long): Result<Int>
    
    // --- Read Operations ---
    
    suspend fun getAllRecords(): Result<List<HealthRecord>>
    
    suspend fun getRecordById(id: Long): Result<HealthRecord?>
    
    suspend fun getRecordByDate(date: String): Result<HealthRecord?>
    
    // --- Observability ---
    
    /**
     * Returns a Flow that emits the list of records whenever the database changes.
     * This is used by the Dashboard and History screens to auto-refresh.
     */
    fun observeAllRecords(): Flow<List<HealthRecord>>
}