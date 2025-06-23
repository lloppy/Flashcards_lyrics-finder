package com.lloppy.flashcards.ui.screens.widget

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
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.lloppy.flashcards.di.appModule
import com.lloppy.flashcards.model.Flashcard
import com.lloppy.flashcards.ui.screens.home.FlashcardViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.compose.koinInject
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

class FlashcardWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                androidContext(context)
                modules(appModule)
            }
        }

        provideContent {
            WidgetContent()
        }
    }

    @Composable
    private fun WidgetContent() {
        val viewModel: FlashcardViewModel = koinInject()
        var currentFlashcard by remember { mutableStateOf<Flashcard?>(null) }
        var isFlipped by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            currentFlashcard = viewModel.getDueFlashcardSync()
        }

        GlanceTheme {

                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .clickable(onClick = actionRunCallback<FlipCardAction>())
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (currentFlashcard != null) {
                        if (isFlipped) {
                            Text(
                                text = currentFlashcard!!.backText,
                                style = TextStyle(fontSize = 16.sp)
                            )
                        } else {
                            Text(
                                text = currentFlashcard!!.frontText,
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    } else {
                        Text(text = "No cards due")
                    }
                }

        }
    }
}

class FlipCardAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        FlashcardWidget().update(context, glanceId)
    }
}