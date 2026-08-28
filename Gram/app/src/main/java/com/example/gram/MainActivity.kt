package com.example.gram

import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.gram.ui.AppRoot
import com.example.gram.ui.theme.BackColor
import com.example.gram.ui.theme.GramTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gram.ui.viewmodel.ImmersiveViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val immersiveState: ImmersiveViewModel = viewModel()
            val isImmersive = immersiveState.isImmersive
            val window = this@MainActivity.window

            GramTheme {
                LaunchedEffect(isImmersive) {
                    setImmersiveMode(window, isImmersive)
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackColor
                ) {
                    MainScreen(immersiveState = immersiveState) { immersiveState.toggleImmersive() }
                }
            }
        }
    }

    @Composable
    private fun MainScreen(immersiveState: ImmersiveViewModel, onToggleImmersive: () -> Unit) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = { onToggleImmersive() }
                    )
                }
        ) {
            Scaffold(
                containerColor = Color.Transparent // Let root Surface color show through
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    AppRoot(immersiveState = immersiveState)
                }
            }
        }
    }

    private fun setImmersiveMode(window: Window, immersive: Boolean) {
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (immersive) {
            controller.hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            controller.show(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
        }
    }
}
