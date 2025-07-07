package com.lloppy.flashcards.util

import android.util.Log

object Logger {
    const val ACTION_TAG = "action_tag"

    fun makeLog(from: String) {
        Log.e(ACTION_TAG, from)
    }
}