package com.lloppy.flashcards.ui.screens.wiget.repeat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import androidx.glance.layout.size
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

@Composable
fun RepeatWidget(
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
