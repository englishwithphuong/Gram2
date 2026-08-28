package com.example.gram.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

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
    fun getSavedContentScrollValue(sourceIndex: Int, lessonIndex: Int): Int {
        val key = "${sourceIndex}_$lessonIndex"
        return prefs.getInt("content_scroll_$key", 0)
    }

    fun saveContentScrollState(sourceIndex: Int, lessonIndex: Int, scrollValue: Int) {
        val key = "${sourceIndex}_$lessonIndex"
        prefs.edit().apply {
            putInt("content_scroll_$key", scrollValue)
            apply()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as Application
                ScrollStateViewModel(application)
            }
        }
    }
}
