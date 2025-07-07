package com.lloppy.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lloppy.data.local.entity.FlashcardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {
    @Insert
    suspend fun insert(flashcard: FlashcardEntity)

    @Update
    suspend fun update(flashcard: FlashcardEntity)

    @Delete
    suspend fun delete(flashcard: FlashcardEntity)

    @Query("SELECT * FROM flashcards ORDER BY nextReviewDue ASC")
    fun getAllFlashcards(): Flow<List<FlashcardEntity>>

    @Query("SELECT * FROM flashcards WHERE id == :id")
    fun getById(id: Int): Flow<FlashcardEntity>

    @Query("SELECT * FROM flashcards WHERE nextReviewDue <= :currentTime ORDER BY nextReviewDue ASC LIMIT 1")
    suspend fun getDueFlashcard(currentTime: Long): FlashcardEntity?

    @Query("SELECT * FROM flashcards WHERE shouldShowAgain == 1 LIMIT 1")
    suspend fun getRandomAvailableFlashcard(): FlashcardEntity?

    @Query("SELECT * FROM flashcards WHERE shouldShowAgain == 1")
    fun getNonLearnedFlashcardFlow(): Flow<FlashcardEntity?>
}