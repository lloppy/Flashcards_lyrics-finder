package com.lloppy.flashcards.ui.screens.wiget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.getAppWidgetState
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.lloppy.flashcards.data.FlashcardRepository
import com.lloppy.flashcards.model.Flashcard
import com.lloppy.flashcards.ui.screens.wiget.FlashcardWidget.Companion.IS_FLIPPED_KEY
import org.koin.compose.koinInject
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey

class FlashcardWidget : GlanceAppWidget() {

    companion object {
        val IS_FLIPPED_KEY = booleanPreferencesKey("is_flipped")
    }

    override var stateDefinition = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            WidgetContent()
        }
    }

    @Composable
    private fun WidgetContent() {
        val repository: FlashcardRepository = koinInject()
        var currentFlashcard by remember { mutableStateOf<Flashcard?>(null) }

        val prefs = currentState<Preferences>()
        val isFlipped = prefs[IS_FLIPPED_KEY] ?: false

        LaunchedEffect(Unit) {
            currentFlashcard = repository.getDueFlashcard()
        }

        GlanceTheme {
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .clickable(onClick = actionRunCallback<FlipCardAction>())
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                currentFlashcard?.let { flashcard ->
                    Text(
                        text = if (isFlipped) flashcard.backText else flashcard.frontText,
                        style = TextStyle(
                            fontSize = if (isFlipped) 16.sp else 18.sp,
                            fontWeight = if (isFlipped) FontWeight.Normal else FontWeight.Bold
                        )
                    )
                } ?: Text(text = "No cards due")
            }
        }
    }
}

class FlipCardAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val prefs = getAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId)
        val currentFlipped = prefs[IS_FLIPPED_KEY] ?: false

        updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) { prefs ->
            prefs.toMutablePreferences().apply {
                set(IS_FLIPPED_KEY, !currentFlipped)
            }
        }
        FlashcardWidget().update(context, glanceId)
    }
}
