package com.lloppy.flashcards.ui.screens.wiget.learned

import android.content.Context
import androidx.glance.GlanceId
import com.lloppy.flashcards.data.FlashcardRepository
import com.lloppy.flashcards.ui.screens.wiget.FlashcardCommand

class LearnedCommand(
    private val repository: FlashcardRepository
) :FlashcardCommand{
    override suspend fun execute(context: Context, glanceId: GlanceId): Boolean {
        val flashcard = repository.getDueFlashcard() ?: return false
        repository.markAsLearned(flashcard)
        return true
    }
}