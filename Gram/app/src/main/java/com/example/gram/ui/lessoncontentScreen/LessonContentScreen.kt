package com.example.gram.ui.lessoncontentscreen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gram.model.Lesson
import com.example.gram.ui.lessoncontentscreen.components.LessonSectionItem
import com.example.gram.ui.lessoncontentscreen.components.leftbar.LeftButtonBar
import com.example.gram.ui.theme.TitleColor
import com.example.gram.ui.theme.Typography
import com.example.gram.ui.viewmodel.LessonContentViewModel
import com.example.gram.ui.viewmodel.ScrollStateViewModel
import com.example.gram.ui.lessoncontentscreen.components.VerticalScrollbar
import com.example.gram.ui.lessoncontentscreen.components.buildStyledChineseText
import com.example.gram.ui.theme.KaitiBoldFontFamily

@Composable
fun LessonContentScreen(
    navController: NavController,
    sourceIndex: Int,
    lessonIndex: Int,
    lessonCount: Int,
    isImmersive: Boolean
) {
    val viewModel: LessonContentViewModel = viewModel(
        factory = LessonContentViewModel.provideFactory(
            sourceIndex,
            lessonIndex
        )
    )
    val lesson by viewModel.lesson.collectAsState()

    val scrollViewModel: ScrollStateViewModel = viewModel(
        factory = ScrollStateViewModel.Factory
    )

    val savedScrollValue = remember(sourceIndex, lessonIndex) {
        scrollViewModel.getSavedContentScrollValue(sourceIndex, lessonIndex)
    }

    val scrollState = remember(sourceIndex, lessonIndex) {
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
                scrollViewModel.saveContentScrollState(sourceIndex, lessonIndex, scrollValue)
            }
    }

    LessonContentBody(
        navController = navController,
        sourceIndex = sourceIndex,
        lessonIndex = lessonIndex,
        lessonCount = lessonCount,
        lesson = lesson,
        scrollState = scrollState,
        isImmersive = isImmersive
    )
}

@Composable
fun LessonContentBody(
    navController: NavController,
    sourceIndex: Int,
    lessonIndex: Int,
    lessonCount: Int,
    lesson: Lesson?,
    scrollState: ScrollState,
    isImmersive: Boolean
) {
    val rawTitle = lesson?.title ?: "Lesson ${lessonIndex + 1}"

    // Apply KaitiBoldFontFamily to the Chinese characters in the lesson title
    val styledTitle = remember(rawTitle) {
        buildStyledChineseText(
            text = rawTitle,
            chineseFontFamily = KaitiBoldFontFamily
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 12.dp, top = 0.dp, bottom = 0.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = styledTitle,
                fontSize = Typography.titleLarge.fontSize,
                fontWeight = Typography.titleLarge.fontWeight,
                lineHeight = Typography.titleLarge.lineHeight,
                color = TitleColor
            )
            Spacer(modifier = Modifier.height(16.dp))

            lesson?.sections?.forEach { section ->
                LessonSectionItem(section = section)
            }
        }

        if (!isImmersive) {
            LeftButtonBar(
                modifier = Modifier.align(Alignment.CenterStart),
                navController = navController,
                sourceIndex = sourceIndex,
                lessonIndex = lessonIndex,
                lessonCount = lessonCount
            )

            VerticalScrollbar(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .padding(end = 4.dp, top = 16.dp, bottom = 16.dp), // Fixed padding parameters
                scrollState = scrollState
            )
        }
    }
}
