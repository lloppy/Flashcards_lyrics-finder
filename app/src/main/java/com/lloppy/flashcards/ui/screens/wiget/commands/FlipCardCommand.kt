package com.lloppy.flashcards.ui.screens.wiget.commands

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.state.getAppWidgetState
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.lloppy.flashcards.ui.screens.wiget.FlashcardCommand
import com.lloppy.flashcards.ui.screens.wiget.FlashcardWidget.WidgetKeys.IS_FLIPPED_KEY

class FlipCardCommand : FlashcardCommand {
    override suspend fun execute(context: Context, glanceId: GlanceId): Boolean {
        return try {
            val prefs = getAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId)
            val currentFlipped = prefs[IS_FLIPPED_KEY] ?: false // текущее состояние

            updateAppWidgetState(
                context = context,
                definition = PreferencesGlanceStateDefinition,
                glanceId = glanceId
            ) { preferences ->
                preferences.toMutablePreferences().apply {
                    set(IS_FLIPPED_KEY, !currentFlipped)
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}