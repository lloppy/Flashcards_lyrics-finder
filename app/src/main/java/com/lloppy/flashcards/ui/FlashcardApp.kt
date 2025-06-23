package com.lloppy.flashcards.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lloppy.flashcards.model.Flashcard
import com.lloppy.flashcards.ui.screens.home.FlashcardViewModel
import com.lloppy.flashcards.ui.screens.home.components.AddFlashcardDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardApp(
    viewModel: FlashcardViewModel
) {
    val allFlashcards by viewModel.allFlashcards.observeAsState(emptyList())
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Flashcards") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Flashcard")
            }
        }
    ) { padding ->
        if (showAddDialog) {
            AddFlashcardDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { front, back ->
                    viewModel.insert(Flashcard(frontText = front, backText = back))
                    showAddDialog = false
                }
            )
        }

        LazyColumn(modifier = Modifier.padding(padding)) {
            items(allFlashcards) { flashcard ->
                FlashcardItem(
                    flashcard = flashcard,
                    onDelete = { viewModel.delete(it) }
                )
            }
        }
    }
}

@Composable
fun FlashcardItem(flashcard: Flashcard, onDelete: (Flashcard) -> Unit) {
    var isFlipped by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { isFlipped = !isFlipped },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f),
            contentAlignment = Alignment.Center
        ) {
            if (isFlipped) {
                Text(
                    text = flashcard.backText,
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Text(
                    text = flashcard.frontText,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}