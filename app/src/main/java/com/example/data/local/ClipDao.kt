package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ClipDao {
    @Query("SELECT * FROM saved_clips ORDER BY savedAtMillis DESC")
    fun getAllSavedClips(): Flow<List<ClipEntity>>

    @Query("SELECT * FROM saved_clips ORDER BY overallScore DESC")
    fun getClipsSortedByScore(): Flow<List<ClipEntity>>

    @Query("SELECT * FROM saved_clips WHERE isFavorite = 1 ORDER BY savedAtMillis DESC")
    fun getFavoriteClips(): Flow<List<ClipEntity>>

    @Query("SELECT * FROM saved_clips WHERE clipTitle LIKE '%' || :query || '%' OR videoTitle LIKE '%' || :query || '%' OR whyViralExplanation LIKE '%' || :query || '%' ORDER BY savedAtMillis DESC")
    fun searchClips(query: String): Flow<List<ClipEntity>>

    @Query("SELECT * FROM saved_clips WHERE id = :clipId LIMIT 1")
    suspend fun getClipById(clipId: String): ClipEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM saved_clips WHERE id = :clipId)")
    suspend fun isClipSaved(clipId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClip(clip: ClipEntity)

    @Query("UPDATE saved_clips SET isFavorite = :isFavorite WHERE id = :clipId")
    suspend fun updateFavorite(clipId: String, isFavorite: Boolean)

    @Delete
    suspend fun deleteClip(clip: ClipEntity)

    @Query("DELETE FROM saved_clips WHERE id = :clipId")
    suspend fun deleteClipById(clipId: String)
}
