package com.example.gram.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ScrollStateViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("scroll_prefs", Context.MODE_PRIVATE)
    private val states = mutableMapOf<Int, LazyListState>()

    fun getScrollState(sourceIndex: Int): LazyListState {
        return states.getOrPut(sourceIndex) {
            val index = prefs.getInt("index_$sourceIndex", 0)
            val offset = prefs.getInt("offset_$sourceIndex", 0)
            LazyListState(
                firstVisibleItemIndex = index,
                firstVisibleItemScrollOffset = offset
            )
        }
    }

    fun saveScrollState(sourceIndex: Int, state: LazyListState) {
        prefs.edit().apply {
            putInt("index_$sourceIndex", state.firstVisibleItemIndex)
            putInt("offset_$sourceIndex", state.firstVisibleItemScrollOffset)
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
