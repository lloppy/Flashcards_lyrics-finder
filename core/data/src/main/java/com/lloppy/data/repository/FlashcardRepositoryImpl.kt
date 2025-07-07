package com.lloppy.data.repository

import com.lloppy.data.local.dao.FlashcardDao
import com.lloppy.data.mapper.FlashcardMapper
import com.lloppy.domain.FlashcardRepository
import com.lloppy.model.Flashcard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Date

class FlashcardRepositoryImpl(
    private val flashcardDao: FlashcardDao,
    private val mapper: FlashcardMapper
) : FlashcardRepository {

    // ==================== Flow Operations ====================

    override val flashcardsFlow: Flow<List<Flashcard>>
        get() = flashcardDao.getAllFlashcards()
            .map { entities ->
                entities.map(mapper::toDomain)
            }

    override fun getNonLearnedFlashcardFlow(): Flow<Flashcard?> =
        flashcardDao.getNonLearnedFlashcardFlow()
            .map { entity ->
                entity?.let(mapper::toDomain)
            }

    // ==================== CRUD Operations ====================

    override suspend fun insert(flashcard: Flashcard) {
        flashcardDao.insert(
            flashcard = mapper.toEntity(flashcard)
        )
    }

    override suspend fun update(flashcard: Flashcard) {
        flashcardDao.update(
            flashcard = mapper.toEntity(flashcard)
        )
    }

    override suspend fun delete(flashcard: Flashcard) {
        flashcardDao.delete(
            flashcard = mapper.toEntity(flashcard)
        )
    }

    // ==================== Query Operations ====================

    override suspend fun getDueFlashcard(): Flashcard? =
        flashcardDao.getDueFlashcard(
            currentTime = System.currentTimeMillis()
        )?.let(mapper::toDomain)

    override suspend fun getFlashcardById(id: Int): Flashcard =
        flashcardDao.getById(id)
            .first()
            .let(mapper::toDomain)

    override suspend fun getRandomAvailableFlashcard(): Flashcard? =
        flashcardDao.getRandomAvailableFlashcard()
            ?.let(mapper::toDomain)

    // ==================== Learning Logic ====================

    override suspend fun markAsLearned(flashcard: Flashcard) {
        val updatedFlashcard = createUpdatedFlashcard(
            original = flashcard,
            learned = true
        )
        update(updatedFlashcard)
    }

    override suspend fun markForRepeat(flashcard: Flashcard) {
        val updatedFlashcard = createUpdatedFlashcard(
            original = flashcard,
            learned = false
        )
        update(updatedFlashcard)
    }

    // ==================== Private Helpers ====================

    private fun createUpdatedFlashcard(
        original: Flashcard,
        learned: Boolean
    ): Flashcard {
        return original.copy(
            shouldShowAgain = !learned,
            lastReviewed = Date(),
            nextReviewDue = calculateNextReviewDate(learned)
        )
    }

    private fun calculateNextReviewDate(learned: Boolean): Date {
        val days = if (learned) 7 else 1
        return Date(System.currentTimeMillis() + days * MILLIS_PER_DAY)
    }

    companion object {
        private const val MILLIS_PER_DAY = 86_400_000L
    }
}