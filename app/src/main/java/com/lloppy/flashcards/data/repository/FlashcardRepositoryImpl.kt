package com.lloppy.flashcards.data.repository

import com.lloppy.flashcards.data.FlashcardRepository
import com.lloppy.flashcards.data.dao.FlashcardDao
import com.lloppy.flashcards.model.Flashcard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class FlashcardRepositoryImpl(
    private val flashcardDao: FlashcardDao
) : FlashcardRepository {
    override val flashcardsFlow: Flow<List<Flashcard>> = flashcardDao.getAllFlashcards()

    override suspend fun insert(flashcard: Flashcard) = flashcardDao.insert(flashcard)
    override suspend fun update(flashcard: Flashcard) = flashcardDao.update(flashcard)
    override suspend fun delete(flashcard: Flashcard) = flashcardDao.delete(flashcard)
    override suspend fun getDueFlashcard(): Flashcard? = flashcardDao.getDueFlashcard(System.currentTimeMillis())
    override suspend fun getFlashcardById(id: Int): Flashcard = flashcardDao.getById(id).first()
    override suspend fun getRandomAvailableFlashcard(): Flashcard? = flashcardDao.getRandomAvailableFlashcard()
    override fun getNonLearnedFlashcardFlow(): Flow<Flashcard?> = flashcardDao.getNonLearnedFlashcardFlow()

    override suspend fun markAsLearned(flashcard: Flashcard) {
        val updatedFlashcard = flashcard.copy(
            shouldShowAgain = false,
            lastReviewed = System.currentTimeMillis(),
            nextReviewDue = calculateNextReviewDate(true)
        )
        flashcardDao.update(updatedFlashcard)
    }

    override suspend fun markForRepeat(flashcard: Flashcard) {
        val updatedFlashcard = flashcard.copy(
            shouldShowAgain = true,
            lastReviewed = System.currentTimeMillis(),
            nextReviewDue = calculateNextReviewDate(false)
        )
        flashcardDao.update(updatedFlashcard)
    }

    private fun calculateNextReviewDate(isLearned: Boolean): Long {
        return if (isLearned) {
            // If learned, review in 7 days
            System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000
        } else {
            // If needs repetition, review in 1 day
            System.currentTimeMillis() + 24 * 60 * 60 * 1000
        }
    }
}