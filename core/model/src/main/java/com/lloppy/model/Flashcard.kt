package com.lloppy.model

import java.util.Date

data class Flashcard(
    val id: Int = 0,
    val question: String,
    val answer: String,
    val category: String? = null,
    val lastReviewed: Date = Date(),
    val nextReviewDue: Date = Date(),
    val shouldShowAgain: Boolean = true,
)