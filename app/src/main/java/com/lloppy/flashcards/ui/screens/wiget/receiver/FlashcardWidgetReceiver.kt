package com.lloppy.flashcards.ui.screens.wiget.receiver

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.lloppy.flashcards.ui.screens.wiget.FlashcardWidget
import com.lloppy.flashcards.ui.screens.wiget.FlashcardWidgetWorker

class FlashcardWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = FlashcardWidget()

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        FlashcardWidgetWorker.enqueueUpdate(context)
    }
}