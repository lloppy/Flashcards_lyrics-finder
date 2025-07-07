package com.lloppy.flashcards.ui.screens.wiget.flip

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.getAppWidgetState
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.lloppy.flashcards.ui.screens.wiget.FlashcardWidget
import com.lloppy.flashcards.ui.screens.wiget.FlashcardWidget.Companion.IS_FLIPPED_KEY
import com.lloppy.flashcards.util.Logger

class FlipCardAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        val prefs = getAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId)
        val currentFlipped = prefs[IS_FLIPPED_KEY] ?: false

        updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) { preferences ->
            preferences.toMutablePreferences().apply {
                set(IS_FLIPPED_KEY, !currentFlipped)
            }
        }

        Logger.makeLog("FlipCardAction")
        FlashcardWidget().update(context, glanceId)
    }
}
