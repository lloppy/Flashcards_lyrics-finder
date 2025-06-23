package com.lloppy.flashcards.ui.screens.wiget

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.lloppy.flashcards.data.FlashcardRepository
import com.lloppy.flashcards.di.appModule
import com.lloppy.flashcards.ui.screens.widget.FlashcardWidget
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class FlashcardWidgetWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val repository: FlashcardRepository = get()

    override suspend fun doWork(): Result {
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                androidContext(applicationContext)
                modules(appModule)
            }
        }
        FlashcardWidget().updateAll(applicationContext)

        return Result.success()
    }

    companion object {
        fun enqueueUpdate(context: Context) {
            val workRequest = OneTimeWorkRequestBuilder<FlashcardWidgetWorker>()
                .setInitialDelay(1, TimeUnit.HOURS)
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}