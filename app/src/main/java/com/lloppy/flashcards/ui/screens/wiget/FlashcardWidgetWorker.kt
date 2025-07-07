package com.lloppy.flashcards.ui.screens.wiget

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import java.util.concurrent.TimeUnit

class FlashcardWidgetWorker(
    val context: Context,
    val params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    override suspend fun doWork(): Result {
        return try {
            FlashcardWidget().updateAll(context)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        private const val UPDATE_DELAY_HOURS = 1L

        fun enqueueUpdate(context: Context) {
            val workRequest = OneTimeWorkRequestBuilder<FlashcardWidgetWorker>()
                .setInitialDelay(UPDATE_DELAY_HOURS, TimeUnit.HOURS)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                "flashcard_widget_update",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        }
    }
}