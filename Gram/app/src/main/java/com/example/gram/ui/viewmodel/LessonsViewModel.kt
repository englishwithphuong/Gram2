package com.example.gram.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gram.repository.getLessonsForSource
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
            val loadedLessons = getLessonsForSource(application, sourceIndex)
            _lessons.value = loadedLessons
        }
    }

    companion object {
        fun provideFactory(sourceIndex: Int): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as Application
                LessonsViewModel(application, sourceIndex)
            }
        }
    }
}
