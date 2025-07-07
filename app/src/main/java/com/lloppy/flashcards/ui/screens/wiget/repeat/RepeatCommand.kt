package com.lloppy.flashcards.ui.screens.wiget.repeat

import android.content.Context
import androidx.glance.GlanceId
import com.lloppy.flashcards.data.FlashcardRepository
import com.lloppy.flashcards.ui.screens.wiget.FlashcardCommand

class RepeatCommand(
    private val repository: FlashcardRepository
): FlashcardCommand{
    override suspend fun execute(context: Context, glanceId: GlanceId): Boolean {
        val flashcard = repository.getDueFlashcard() ?: return false
        repository.markForRepeat(flashcard)
        return true
    }
}