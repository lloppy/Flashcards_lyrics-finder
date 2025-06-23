package com.lloppy.flashcards

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.lloppy.flashcards.di.appModule
import com.lloppy.flashcards.ui.screens.wiget.FlashcardWidgetWorker
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class FlashcardApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Koin for DI if needed
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@FlashcardApplication)
            modules(appModule)
        }

        // Schedule periodic widget updates
        FlashcardWidgetWorker.enqueueUpdate(this)
    }
}