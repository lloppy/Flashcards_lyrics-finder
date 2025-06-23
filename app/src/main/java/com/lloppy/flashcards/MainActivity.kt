package com.lloppy.flashcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lloppy.flashcards.ui.screens.home.FlashcardApp
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