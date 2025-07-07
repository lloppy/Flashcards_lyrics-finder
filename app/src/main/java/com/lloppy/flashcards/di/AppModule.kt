package com.lloppy.flashcards.di

import androidx.work.WorkManager
import com.lloppy.data.local.AppDatabase
import com.lloppy.data.mapper.FlashcardMapper
import com.lloppy.data.repository.FlashcardRepositoryImpl
import com.lloppy.data.repository.FlashcardRepositoryLogger
import com.lloppy.domain.FlashcardRepository
import com.lloppy.flashcards.ui.screens.home.FlashcardViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Database
    single { AppDatabase.getDatabase(androidApplication()) }
    single { get<AppDatabase>().flashcardDao() }

    // Repository
    single<FlashcardRepository> {
        val realRepository =
            FlashcardRepositoryImpl(
                flashcardDao = get(),
                mapper = FlashcardMapper()
            )
        FlashcardRepositoryLogger(repository = realRepository)
    }

    // ViewModel
    viewModel { FlashcardViewModel(repository = get()) }

    // WorkManager
    single { WorkManager.getInstance(androidApplication()) }
}