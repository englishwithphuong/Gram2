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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gram.ui.navigation.Host
import com.example.gram.ui.viewmodel.SourcesViewModel
import com.example.gram.ui.viewmodel.SourcesViewModelFactory

@Composable
fun AppRoot() {
    val context = LocalContext.current
    val app = context.applicationContext as Application
    val viewModel: SourcesViewModel = viewModel(factory = SourcesViewModelFactory(app))
    val sources by viewModel.sources.collectAsState()
    if (sources.isEmpty()) {
        LoadingScreen()
    } else {
        Host(sources)
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
