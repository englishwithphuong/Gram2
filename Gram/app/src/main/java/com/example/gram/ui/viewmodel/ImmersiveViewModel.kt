package com.example.gram.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ImmersiveViewModel: ViewModel() {
    var isImmersive by mutableStateOf(false)
        private set

    fun toggleImmersive() {
        isImmersive =!isImmersive
    }
}
