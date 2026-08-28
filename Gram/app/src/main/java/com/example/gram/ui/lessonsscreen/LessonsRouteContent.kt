package com.example.gram.ui.lessonsscreen

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gram.model.SourceItem
import com.example.gram.ui.viewmodel.LessonsViewModel

@Composable
fun LessonsRouteContent(
    sources: List<SourceItem>,
    sourceIndex: Int,
    onLessonClick: (String) -> Unit
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application

    val lessonsViewModel: LessonsViewModel = viewModel(
        factory = LessonsViewModel.provideFactory(application, sourceIndex)
    )
    val lessons by lessonsViewModel.lessons.collectAsState()

    LessonsScreen(
        sourceTitle = sources.find { it.index == sourceIndex }?.title ?: "Lessons",
        lessons = lessons,
        onLessonClick = onLessonClick
    )
}
