package com.lloppy.flashcards.data.repository

import com.lloppy.flashcards.data.FlashcardRepository
import com.lloppy.flashcards.data.dao.FlashcardDao
import com.lloppy.flashcards.model.Flashcard
import kotlinx.coroutines.flow.Flow

class FlashcardRepositoryImpl(
    private val flashcardDao: FlashcardDao
) : FlashcardRepository {
    override val allFlashcards: Flow<List<Flashcard>> = flashcardDao.getAllFlashcards()

    override suspend fun insert(flashcard: Flashcard) = flashcardDao.insert(flashcard)
    override suspend fun update(flashcard: Flashcard) = flashcardDao.update(flashcard)
    override suspend fun delete(flashcard: Flashcard) = flashcardDao.delete(flashcard)
    override suspend fun getDueFlashcard(): Flashcard? = flashcardDao.getDueFlashcard(System.currentTimeMillis())
}