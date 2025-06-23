package com.lloppy.flashcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.lloppy.flashcards.ui.FlashcardApp
import com.lloppy.flashcards.ui.screens.home.FlashcardViewModel
import com.lloppy.flashcards.ui.theme.FlashcardsTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FlashcardsTheme {
                val viewModel: FlashcardViewModel = koinViewModel()
                FlashcardApp(viewModel = viewModel)
            }
        }
    }
}