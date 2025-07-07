package com.lloppy.flashcards.ui.screens.wiget

import android.content.Context
import androidx.glance.GlanceId

interface FlashcardCommand {
    suspend fun execute(context: Context, glanceId: GlanceId): Boolean
}