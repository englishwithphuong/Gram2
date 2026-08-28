package com.example.gram.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gram.repository.getLessonContent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LessonContentViewModel(
    application: Application,
    private val sourceIndex: Int,
    private val lessonName: String
) : AndroidViewModel(application) {

    private val _content = MutableStateFlow("Loading...")
    val content: StateFlow<String> = _content.asStateFlow()

    init {
        loadContent()
    }

    private fun loadContent() {
        viewModelScope.launch {
            _content.value = getLessonContent(getApplication(), sourceIndex, lessonName)
        }
    }

    companion object {
        fun provideFactory(
            application: Application,
            sourceIndex: Int,
            lessonName: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LessonContentViewModel(application, sourceIndex, lessonName) as T
            }
        }
    }
}
