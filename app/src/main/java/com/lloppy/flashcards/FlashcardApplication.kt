package com.lloppy.flashcards

import android.app.Application
import com.lloppy.flashcards.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class FlashcardApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@FlashcardApplication)
            modules(appModule)
        }

        // Schedule periodic widget updates
        // FlashcardWidgetWorker.enqueueUpdate(this)
    }
}