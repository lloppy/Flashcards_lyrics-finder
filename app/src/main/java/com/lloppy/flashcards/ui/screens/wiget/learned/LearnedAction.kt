package com.lloppy.flashcards.ui.screens.wiget.learned

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.lloppy.flashcards.data.FlashcardRepository
import com.lloppy.flashcards.ui.screens.wiget.FlashcardWidget
import com.lloppy.flashcards.util.Logger
import org.koin.java.KoinJavaComponent.inject

class LearnedAction: ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val repository: FlashcardRepository by inject(FlashcardRepository::class.java)

        val flashcard = repository.getDueFlashcard()
        if (flashcard != null) {
            repository.markAsLearned(flashcard)
        }

        Logger.makeLog("LearnedAction")
        FlashcardWidget().update(context, glanceId)
    }
}