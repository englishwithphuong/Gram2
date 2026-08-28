package com.example.gram.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gram.model.Lesson
import com.example.gram.repository.getLessonContent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LessonContentViewModel(
    application: Application,
    private val sourceIndex: Int,
    private val lessonIndex: Int // Changed from lessonName: String
) : AndroidViewModel(application) {

    private val _lesson = MutableStateFlow<Lesson?>(null)
    val lesson: StateFlow<Lesson?> = _lesson.asStateFlow()

    init {
        loadContent()
    }

    private fun loadContent() {
        viewModelScope.launch {
            // Note: Ensure your repository function `getLessonContent` is updated
            // to accept an Int (lessonIndex) instead of a String if it hasn't been already.
            _lesson.value = getLessonContent(getApplication(), sourceIndex, lessonIndex)
        }
    }

    companion object {
        fun provideFactory(
            sourceIndex: Int,
            lessonIndex: Int // Changed from lessonName: String
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as Application
                LessonContentViewModel(application, sourceIndex, lessonIndex)
            }
        }
    }
}
