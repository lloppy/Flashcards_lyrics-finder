package com.lloppy.data.repository

import android.util.Log
import com.lloppy.domain.FlashcardRepository
import com.lloppy.model.Flashcard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FlashcardRepositoryLogger(
    private val repository: FlashcardRepository,
    private val tag: String = "FlashcardRepository"
) : FlashcardRepository {

    override val flashcardsFlow: Flow<List<Flashcard>> = repository.flashcardsFlow
        .map { flashcards ->
            Log.d(tag, "Retrieved ${flashcards.size} flashcards")
            flashcards
        }

    override suspend fun insert(flashcard: Flashcard) {
        Log.d(tag, "Inserting flashcard: ${flashcard.question.take(20)}...")
        repository.insert(flashcard)
        Log.d(tag, "Flashcard inserted with id: ${flashcard.id}")
    }

    override suspend fun update(flashcard: Flashcard) {
        Log.d(tag, "Updating flashcard id=${flashcard.id}")
        repository.update(flashcard)
        Log.d(tag, "Flashcard updated")
    }

    override suspend fun delete(flashcard: Flashcard) {
        Log.d(tag, "Deleting flashcard id=${flashcard.id}")
        repository.delete(flashcard)
        Log.d(tag, "Flashcard deleted")
    }

    override suspend fun getDueFlashcard(): Flashcard? {
        Log.d(tag, "Getting due flashcard")
        return repository.getDueFlashcard().also { flashcard ->
            if (flashcard != null) {
                Log.d(tag, "Retrieved due flashcard: ${flashcard.question.take(20)}...")
            } else {
                Log.d(tag, "No due flashcards found")
            }
        }
    }

    override suspend fun getFlashcardById(id: Int): Flashcard {
        Log.d(tag, "Getting flashcard by id=$id")
        return repository.getFlashcardById(id).also {
            Log.d(tag, "Retrieved flashcard: ${it.question.take(20)}...")
        }
    }

    override suspend fun getRandomAvailableFlashcard(): Flashcard? {
        Log.d(tag, "Getting random available flashcard")
        return repository.getRandomAvailableFlashcard().also { flashcard ->
            if (flashcard != null) {
                Log.d(tag, "Retrieved random flashcard: ${flashcard.question.take(20)}...")
            } else {
                Log.d(tag, "No available flashcards found")
            }
        }
    }

    override fun getNonLearnedFlashcardFlow(): Flow<Flashcard?> {
        Log.d(tag, "Getting non learned flashcards flow")
        return repository.getNonLearnedFlashcardFlow().also {
            Log.d(tag, "Retrieved random flashcard flow")
        }
    }

    override suspend fun markAsLearned(flashcard: Flashcard) {
        Log.d(tag, "Marking flashcard id=${flashcard.id} ${flashcard.question} as learned")
        repository.markAsLearned(flashcard)
        Log.d(tag, "Flashcard marked as learned")
    }

    override suspend fun markForRepeat(flashcard: Flashcard) {
        Log.d(tag, "Marking flashcard id=${flashcard.id} ${flashcard.question} for repeat")
        repository.markForRepeat(flashcard)
        Log.d(tag, "Flashcard marked for repeat")
    }
}