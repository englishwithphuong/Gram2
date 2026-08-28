package com.example.gram.ui.lessoncontentscreen

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gram.model.Lesson // assuming your model package
import com.example.gram.model.Section // assuming your section model
import com.example.gram.ui.viewmodel.LessonContentViewModel
import com.example.gram.ui.viewmodel.ScrollStateViewModel

// 1. Stateful Wrapper: Manages ViewModels, states, and side-effects
@Composable
fun LessonContentScreen(
    sourceIndex: Int,
    lessonName: String
) {
    val viewModel: LessonContentViewModel = viewModel(
        factory = LessonContentViewModel.provideFactory(sourceIndex, lessonName)
    )
    val lesson by viewModel.lesson.collectAsState()

    val scrollViewModel: ScrollStateViewModel = viewModel(
        factory = ScrollStateViewModel.Factory
    )

    val savedScrollValue = remember(sourceIndex, lessonName) {
        scrollViewModel.getSavedContentScrollValue(sourceIndex, lessonName)
    }

    val scrollState = remember(sourceIndex, lessonName) {
        ScrollState(initial = savedScrollValue)
    }

    LaunchedEffect(lesson) {
        if (lesson != null && savedScrollValue > 0) {
            scrollState.scrollTo(savedScrollValue)
        }
    }

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.value }
            .collect { scrollValue ->
                scrollViewModel.saveContentScrollState(sourceIndex, lessonName, scrollValue)
            }
    }

    LessonContentBody(
        lessonName = lessonName,
        lesson = lesson,
        scrollState = scrollState
    )
}

// 2. Stateless Layout: Focuses purely on displaying the content
@Composable
fun LessonContentBody(
    lessonName: String,
    lesson: Lesson?,
    scrollState: ScrollState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = lesson?.title ?: lessonName,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))

        lesson?.sections?.forEach { section ->
            LessonSectionItem(section = section)
        }
    }
}

// 3. Granular Item Renderer: Easily extensible when adding new section types
@Composable
fun LessonSectionItem(section: Section) {
    when (section.type) {
        "text" -> {
            val textSize = if (section.level == 1) 18.sp else 16.sp
            val textColor = if (section.level == 1) Color.Yellow else Color.LightGray
            val fontWeight = if (section.level == 1) FontWeight.SemiBold else FontWeight.Normal

            Text(
                text = section.content,
                fontSize = textSize,
                color = textColor,
                fontWeight = fontWeight
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        // Future types like "image", "code", "quiz" can easily be added here:
        // "image" -> { ... }
    }
}
