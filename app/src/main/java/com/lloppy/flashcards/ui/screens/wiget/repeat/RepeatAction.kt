package com.lloppy.flashcards.ui.screens.wiget.repeat

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.lloppy.flashcards.data.FlashcardRepository
import com.lloppy.flashcards.ui.screens.wiget.FlashcardCommand
import com.lloppy.flashcards.ui.screens.wiget.FlashcardWidget
import org.koin.java.KoinJavaComponent.inject

class RepeatAction: ActionCallback {
    private val command: FlashcardCommand by lazy {
        val repository: FlashcardRepository by inject(FlashcardRepository::class.java)
        RepeatCommand(repository)
    }

    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        if (command.execute(context, glanceId)) {
            FlashcardWidget().update(context, glanceId)
        }
    }
}