package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExportJobDao {
    @Query("SELECT * FROM export_jobs ORDER BY exportedAtMillis DESC")
    fun getAllExports(): Flow<List<ExportJobEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExport(job: ExportJobEntity)

    @Query("DELETE FROM export_jobs WHERE id = :jobId")
    suspend fun deleteExportById(jobId: String)

    @Query("DELETE FROM export_jobs")
    suspend fun clearAllExports()
}
