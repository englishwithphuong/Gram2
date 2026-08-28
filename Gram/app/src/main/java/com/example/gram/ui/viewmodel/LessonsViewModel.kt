package com.example.gram.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LessonsViewModel(
    application: Application,
    private val sourceIndex: Int
) : AndroidViewModel(application) {

    private val _lessons = MutableStateFlow<List<String>>(emptyList())
    val lessons: StateFlow<List<String>> = _lessons

    init {
        viewModelScope.launch(Dispatchers.IO) {
            // TODO: Load lessons from assets based on sourceIndex or folder name
            // Example placeholder logic:
            _lessons.value = listOf("Lesson 1", "Lesson 2", "Lesson 3")
        }
    }

    companion object {
        fun provideFactory(
            application: Application,
            sourceIndex: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LessonsViewModel(application, sourceIndex) as T
            }
        }
    }
}
