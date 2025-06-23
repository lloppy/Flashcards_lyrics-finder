package com.lloppy.flashcards.di

import androidx.work.WorkManager
import com.lloppy.flashcards.data.FlashcardRepository
import com.lloppy.flashcards.data.database.AppDatabase
import com.lloppy.flashcards.data.repository.FlashcardRepositoryImpl
import com.lloppy.flashcards.ui.screens.home.FlashcardViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Database
    single { AppDatabase.getDatabase(androidApplication()) }
    single { get<AppDatabase>().flashcardDao() }

    // Repository
    single<FlashcardRepository> { FlashcardRepositoryImpl(get()) }

    // ViewModel
    viewModel { FlashcardViewModel(get()) }

    // WorkManager
    single { WorkManager.getInstance(androidApplication()) }
}