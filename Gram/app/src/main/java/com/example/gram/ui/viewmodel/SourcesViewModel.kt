package com.example.gram.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gram.model.SourceItem
import com.example.gram.repository.getSources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SourcesViewModel(application: Application) : AndroidViewModel(application) {
    private val _sources = MutableStateFlow<List<SourceItem>>(emptyList())
    val sources: StateFlow<List<SourceItem>> = _sources

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _sources.value = getSources(application)
        }
    }
}
