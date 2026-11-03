package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_clips")
data class ClipEntity(
    @PrimaryKey
    val id: String,
    val videoId: String,
    val videoTitle: String,
    val channel: String,
    val thumbnailUrl: String,
    val clipTitle: String,
    val startSeconds: Int,
    val endSeconds: Int,
    val timeRangeFormatted: String,
    val overallScore: Int,
    val hookScore: Int,
    val retentionScore: Int,
    val emotionalScore: Int,
    val shareabilityScore: Int,
    val whyViralExplanation: String,
    val creatorParallels: String,
    val bestPlatform: String,
    val formatRatio: String, // "9:16", "1:1", "16:9"
    val tagsJson: String,
    val captionsJson: String,
    val captionOptionsJson: String,
    val platformLinksJson: String,
    val isFavorite: Boolean = false,
    val savedAtMillis: Long = System.currentTimeMillis()
)
