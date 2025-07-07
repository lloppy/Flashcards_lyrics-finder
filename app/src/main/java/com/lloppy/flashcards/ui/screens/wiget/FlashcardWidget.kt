package com.lloppy.flashcards.ui.screens.wiget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.lloppy.domain.FlashcardRepository
import com.lloppy.flashcards.ui.screens.wiget.actions.FlipCardAction
import com.lloppy.flashcards.ui.screens.wiget.actions.LearnedAction
import com.lloppy.flashcards.ui.screens.wiget.actions.RepeatAction
import com.lloppy.model.Flashcard
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
                    currentFlashcard = nonLearnedFlashcard
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

@Composable
private fun FlipCardWidget(
    flashcard: Flashcard?,
    isFlipped: Boolean,
    action: Action = actionRunCallback<FlipCardAction>(),
    modifier: GlanceModifier = GlanceModifier,
) {
    Box(
        modifier = modifier
            .height(130.dp)
            .padding(horizontal = 16.dp)
            .background(Color.Black.copy(alpha = 0.2f))
            .clickable(onClick = action),
        contentAlignment = Alignment.Center
    ) {
        flashcard?.let { card ->
            Text(
                text = if (isFlipped) card.answer else card.question,
                style = TextStyle(
                    fontSize = if (isFlipped) 16.sp else 18.sp,
                    fontWeight = if (isFlipped) FontWeight.Normal else FontWeight.Bold
                )
            )
        } ?: Text(text = "No cards due")
    }
}

@Composable
private fun RepeatWidget(
    action: Action = actionRunCallback<RepeatAction>(),
    modifier: GlanceModifier = GlanceModifier,
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .background(Color.Red.copy(alpha = 0.2f))
            .clickable(
                onClick = action
            ),
        contentAlignment = Alignment.Center
    ) {
        Text("❌", style = TextStyle(fontSize = 24.sp))
    }
}

@Composable
private fun LearnedWidget(
    action: Action = actionRunCallback<LearnedAction>(),
    modifier: GlanceModifier = GlanceModifier,
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .background(Color.Green.copy(alpha = 0.2f))
            .clickable(onClick = action),
        contentAlignment = Alignment.Center
    ) {
        Text("✅", style = TextStyle(fontSize = 24.sp))
    }
}
