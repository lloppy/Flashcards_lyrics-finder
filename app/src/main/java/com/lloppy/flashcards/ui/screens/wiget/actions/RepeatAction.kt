package com.lloppy.flashcards.ui.screens.wiget.actions

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.lloppy.flashcards.ui.screens.wiget.FlashcardCommand
import com.lloppy.flashcards.ui.screens.wiget.FlashcardWidget
import com.lloppy.flashcards.ui.screens.wiget.commands.RepeatCommand
import org.koin.java.KoinJavaComponent.inject

class RepeatAction: ActionCallback {
    private val command: FlashcardCommand by lazy {
        val repository: com.lloppy.domain.FlashcardRepository by inject(com.lloppy.domain.FlashcardRepository::class.java)
        RepeatCommand(repository)
    }

    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        if (command.execute(context, glanceId)) {
            FlashcardWidget().update(context, glanceId)
        }
    }
}