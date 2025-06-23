package com.lloppy.flashcards.ui.screens.wiget

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import org.koin.core.component.KoinComponent

class FlashcardWidgetWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    override suspend fun doWork(): Result {
        return try {
            FlashcardWidget().updateAll(applicationContext)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        fun enqueueUpdate(context: Context) {
            val workRequest = OneTimeWorkRequestBuilder<FlashcardWidgetWorker>()
                .setInitialDelay(1, TimeUnit.HOURS)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                "flashcard_widget_update",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        }
    }
}