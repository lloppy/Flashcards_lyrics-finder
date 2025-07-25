package com.lloppy.flashcards.ui.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lloppy.domain.FlashcardRepository
import com.lloppy.model.Flashcard
import kotlinx.coroutines.launch

class FlashcardViewModel(
    private val repository: FlashcardRepository,
) : ViewModel() {
    val allFlashcards: LiveData<List<Flashcard>> = repository.flashcardsFlow.asLiveData()
    val dueFlashcard = MutableLiveData<Flashcard?>()

    fun insert(flashcard: Flashcard) = viewModelScope.launch {
        repository.insert(flashcard)
    }

    fun update(flashcard: Flashcard) = viewModelScope.launch {
        repository.update(flashcard)
    }

    fun delete(flashcard: Flashcard) = viewModelScope.launch {
        repository.delete(flashcard)
    }

    fun loadDueFlashcard() = viewModelScope.launch {
        dueFlashcard.postValue(repository.getDueFlashcard())
    }

    suspend fun getDueFlashcardSync(): Flashcard? {
        return repository.getDueFlashcard()
    }

    fun markAsLearned(flashcard: Flashcard) {
        viewModelScope.launch {
            repository.markAsLearned(flashcard)
        }
    }

    fun markForRepeat(flashcard: Flashcard) {
        viewModelScope.launch {
            repository.markForRepeat(flashcard)
        }
    }
}