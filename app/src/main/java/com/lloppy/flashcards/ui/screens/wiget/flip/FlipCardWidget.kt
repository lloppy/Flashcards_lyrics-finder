package com.lloppy.flashcards.ui.screens.wiget.flip

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.lloppy.flashcards.model.Flashcard

@Composable
fun FlipCardWidget(
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