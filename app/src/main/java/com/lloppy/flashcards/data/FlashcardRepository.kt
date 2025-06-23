package com.lloppy.flashcards.data

import com.lloppy.flashcards.model.Flashcard
import kotlinx.coroutines.flow.Flow

interface FlashcardRepository {
    val allFlashcards: Flow<List<Flashcard>>
    suspend fun insert(flashcard: Flashcard)
    suspend fun update(flashcard: Flashcard)
    suspend fun delete(flashcard: Flashcard)
    suspend fun getDueFlashcard(): Flashcard?
}