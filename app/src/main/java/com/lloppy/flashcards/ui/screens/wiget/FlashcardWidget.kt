package com.lloppy.flashcards.ui.screens.wiget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.lloppy.flashcards.data.FlashcardRepository
import com.lloppy.flashcards.model.Flashcard
import com.lloppy.flashcards.ui.screens.wiget.flip.FlipCardWidget
import com.lloppy.flashcards.ui.screens.wiget.learned.LearnedWidget
import com.lloppy.flashcards.ui.screens.wiget.repeat.RepeatWidget
import org.koin.java.KoinJavaComponent.inject

class FlashcardWidget : GlanceAppWidget() {

    companion object WidgetKeys {
        val IS_FLIPPED_KEY = booleanPreferencesKey("is_flipped")
    }

    override var stateDefinition = PreferencesGlanceStateDefinition
    private val repository: FlashcardRepository by inject(FlashcardRepository::class.java)


    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            WidgetContent()
        }
    }

    @Composable
    private fun WidgetContent() {
        var currentFlashcard by remember { mutableStateOf<Flashcard?>(null) }
        val prefs = currentState<Preferences>()

        LaunchedEffect(Unit) {
            repository.getNonLearnedFlashcardFlow()
                .collect { nonLearnedFlashcard ->
                    currentFlashcard = nonLearnedFlashcard ?: Flashcard(
                        id = -1,
                        question = "No cards due",
                        answer = "All cards have learned",
                        shouldShowAgain = true
                    )
                }
        }

        Row(
            modifier = GlanceModifier
                .fillMaxSize()
                .appWidgetBackground()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RepeatWidget()
            FlipCardWidget(
                flashcard = currentFlashcard,
                isFlipped = prefs[IS_FLIPPED_KEY] ?: false
            )
            LearnedWidget()
        }
    }
}

