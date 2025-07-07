package com.lloppy.flashcards.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcards")
data class Flashcard(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val question: String,
    val answer: String,
    val category: String? = null,
    val lastReviewed: Long = System.currentTimeMillis(),
    val nextReviewDue: Long = System.currentTimeMillis(),
    val shouldShowAgain: Boolean = true
)