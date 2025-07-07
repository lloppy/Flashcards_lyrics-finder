package com.lloppy.domain

import com.lloppy.model.Flashcard
import kotlinx.coroutines.flow.Flow

interface FlashcardRepository {
    val flashcardsFlow: Flow<List<Flashcard>>
    fun getNonLearnedFlashcardFlow(): Flow<Flashcard?>

    suspend fun insert(flashcard: Flashcard)
    suspend fun update(flashcard: Flashcard)
    suspend fun delete(flashcard: Flashcard)
    suspend fun getDueFlashcard(): Flashcard?

    suspend fun getFlashcardById(id: Int): Flashcard
    suspend fun getRandomAvailableFlashcard(): Flashcard?

    suspend fun markAsLearned(flashcard: Flashcard)
    suspend fun markForRepeat(flashcard: Flashcard)
}