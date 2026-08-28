package com.example.gram.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ScrollStateViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("scroll_prefs", Context.MODE_PRIVATE)

    // Cache for lesson lists
    private val listStates = mutableMapOf<Int, LazyListState>()

    // --- Lesson List Scroll State ---
    fun getListScrollState(sourceIndex: Int): LazyListState {
        return listStates.getOrPut(sourceIndex) {
            val index = prefs.getInt("list_index_$sourceIndex", 0)
            val offset = prefs.getInt("list_offset_$sourceIndex", 0)
            LazyListState(
                firstVisibleItemIndex = index,
                firstVisibleItemScrollOffset = offset
            )
        }
    }

    fun saveListScrollState(sourceIndex: Int, state: LazyListState) {
        prefs.edit().apply {
            putInt("list_index_$sourceIndex", state.firstVisibleItemIndex)
            putInt("list_offset_$sourceIndex", state.firstVisibleItemScrollOffset)
            apply()
        }
    }

    // --- Lesson Content Scroll State ---
    fun getSavedContentScrollValue(sourceIndex: Int, lessonName: String): Int {
        val key = "${sourceIndex}_$lessonName"
        return prefs.getInt("content_scroll_$key", 0)
    }

    fun saveContentScrollState(sourceIndex: Int, lessonName: String, scrollValue: Int) {
        val key = "${sourceIndex}_$lessonName"
        prefs.edit().apply {
            putInt("content_scroll_$key", scrollValue)
            apply()
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ScrollStateViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ScrollStateViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
