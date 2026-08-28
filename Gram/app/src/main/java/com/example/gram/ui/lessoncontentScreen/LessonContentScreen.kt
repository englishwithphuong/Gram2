package com.example.gram.ui.lessoncontentScreen

import android.app.Application
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gram.ui.viewmodel.LessonContentViewModel
import com.example.gram.ui.viewmodel.ScrollStateViewModel

@Composable
fun LessonContentScreen(
    sourceIndex: Int,
    lessonName: String
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application

    val viewModel: LessonContentViewModel = viewModel(
        factory = LessonContentViewModel.provideFactory(sourceIndex, lessonName)
    )
    val lesson by viewModel.lesson.collectAsState()

    val scrollViewModel: ScrollStateViewModel = viewModel(
        factory = ScrollStateViewModel.Factory(application)
    )

    // Load the saved scroll position from SharedPreferences upfront
    val savedScrollValue = remember(sourceIndex, lessonName) {
        scrollViewModel.getSavedContentScrollValue(sourceIndex, lessonName)
    }

    // Create a ScrollState initialized directly to the saved position
    val scrollState = remember(sourceIndex, lessonName) {
        ScrollState(initial = savedScrollValue)
    }

    // Restore and snap to position once the asynchronous lesson data is loaded and rendered
    LaunchedEffect(lesson) {
        if (lesson != null && savedScrollValue > 0) {
            scrollState.scrollTo(savedScrollValue)
        }
    }

    // Automatically save scroll position whenever it changes
    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.value }
            .collect { scrollValue ->
                scrollViewModel.saveContentScrollState(sourceIndex, lessonName, scrollValue)
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        // Main Title from JSON metadata
        Text(
            text = lesson?.title ?: lessonName,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Dynamically render each section based on its type and level
        lesson?.sections?.forEach { section ->
            when (section.type) {
                "text" -> {
                    val textSize = if (section.level == 1) 18.sp else 16.sp
                    val textColor = if (section.level == 1) Color.Yellow else Color.LightGray

                    Text(
                        text = section.content,
                        fontSize = textSize,
                        color = textColor,
                        fontWeight = if (section.level == 1) FontWeight.SemiBold else FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}
