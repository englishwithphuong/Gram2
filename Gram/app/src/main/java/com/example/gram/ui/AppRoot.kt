package com.example.gram.ui

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gram.ui.navigation.Host
import com.example.gram.ui.viewmodel.ImmersiveViewModel
import com.example.gram.ui.viewmodel.SourcesViewModel

@Composable
fun AppRoot(immersiveState: ImmersiveViewModel) {
    val viewModel: SourcesViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as Application
                SourcesViewModel(application)
            }
        }
    )
    val sources by viewModel.sources.collectAsState()
    if (sources.isEmpty()) {
        LoadingScreen()
    } else {
        Host(sources = sources, immersiveState = immersiveState)
    }
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(Modifier.height(16.dp))
            Text("Loading lessons…", color = Color.Gray)
        }
    }
}
