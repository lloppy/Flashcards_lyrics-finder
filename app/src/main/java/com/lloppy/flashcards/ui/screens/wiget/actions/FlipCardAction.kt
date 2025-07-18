package com.lloppy.flashcards.ui.screens.wiget.actions

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.lloppy.flashcards.ui.screens.wiget.FlashcardCommand
import com.lloppy.flashcards.ui.screens.wiget.FlashcardWidget
import com.lloppy.flashcards.ui.screens.wiget.commands.FlipCardCommand

class FlipCardAction : ActionCallback {
    private val command: FlashcardCommand = FlipCardCommand()

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        if (command.execute(context, glanceId)) {
            FlashcardWidget().update(context, glanceId)
        }
    }
}
