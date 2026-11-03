package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "export_jobs")
data class ExportJobEntity(
    @PrimaryKey
    val id: String,
    val clipId: String,
    val clipTitle: String,
    val resolution: String, // "1080p (Full HD)" or "720p (HD)"
    val aspectRatio: String, // "9:16", "1:1", "16:9"
    val burnedInCaptions: Boolean,
    val captionStyle: String,
    val status: String, // "COMPLETED", "IN_PROGRESS", "FAILED"
    val outputFileName: String,
    val fileSizeBytes: Long,
    val durationSeconds: Int,
    val exportedAtMillis: Long = System.currentTimeMillis()
)
