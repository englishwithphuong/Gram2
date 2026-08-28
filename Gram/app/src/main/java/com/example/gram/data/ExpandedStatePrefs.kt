package com.example.gram.data

import android.content.Context
import androidx.core.content.edit

object ExpandedStatePrefs {
    private const val PREF_NAME = "expanded_lessons_prefs"

    fun saveChunkExpanded(context: Context, sourceIndex: Int, chunkIndex: Int, isExpanded: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit { putBoolean("source_${sourceIndex}_chunk_$chunkIndex", isExpanded) }
    }

    fun loadChunkExpanded(context: Context, sourceIndex: Int, chunkIndex: Int, default: Boolean): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("source_${sourceIndex}_chunk_$chunkIndex", default)
    }
}
