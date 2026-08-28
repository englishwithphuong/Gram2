package com.example.gram.ui.lessoncontentscreen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.gram.model.Section
import com.example.gram.ui.lessoncontentscreen.leftbar.LeftButtonBar
import com.example.gram.ui.theme.Level1Color
import com.example.gram.ui.theme.Level2Color
import com.example.gram.ui.theme.ScrollBackgroundColor
import com.example.gram.ui.theme.ScrollThumbColor
import com.example.gram.ui.theme.TitleColor
import com.example.gram.ui.theme.Typography
import com.example.gram.ui.viewmodel.LessonContentViewModel
import com.example.gram.ui.viewmodel.ScrollStateViewModel

@Composable
fun LessonContentScreen(
    navController: NavController,
    sourceIndex: Int,
    lessonIndex: Int,
    lessonCount: Int,
    isImmersive: Boolean
) {
    val viewModel: LessonContentViewModel = viewModel(
        factory = LessonContentViewModel.provideFactory(sourceIndex, lessonIndex) // Ensure your VM factory accepts Int
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
    Box(modifier = Modifier.fillMaxSize()) {
        // Main scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                // Fixed: explicitly chain or use individual padding parameters
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = lesson?.title ?: "Lesson ${lessonIndex + 1}",
                fontSize = Typography.titleLarge.fontSize,
                fontWeight = Typography.titleLarge.fontWeight,
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

@Composable
fun VerticalScrollbar(
    modifier: Modifier = Modifier,
    scrollState: ScrollState
) {
    val viewportHeight = scrollState.viewportSize.toFloat()
    val scrollRange = scrollState.maxValue
    val totalContentHeight = scrollRange + viewportHeight

    if (totalContentHeight > 0f) {
        val indicatorHeightFraction = (viewportHeight / totalContentHeight).coerceIn(0.1f, 1f)

        Box(
            modifier = modifier
                .width(6.dp)
                .background(ScrollBackgroundColor, shape = RoundedCornerShape(3.dp))
        ) {
            androidx.compose.foundation.layout.BoxWithConstraints(
                modifier = Modifier.fillMaxSize()
            ) {
                val availableHeightPx = constraints.maxHeight.toFloat()
                val thumbHeightPx = availableHeightPx * indicatorHeightFraction
                val maxTravelPx = availableHeightPx - thumbHeightPx

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(indicatorHeightFraction)
                        // The lambda overload of offset expects an IntOffset in pixels.
                        // Reading scrollState.value here defers execution to the layout phase.
                        .offset {
                            val currentMaxValue = scrollState.maxValue
                            val scrollFraction = if (currentMaxValue > 0) {
                                (scrollState.value.toFloat() / currentMaxValue).coerceIn(0f, 1f)
                            } else {
                                0f
                            }
                            val currentOffsetYPx = scrollFraction * maxTravelPx

                            androidx.compose.ui.unit.IntOffset(0, currentOffsetYPx.toInt())
                        }
                        .background(ScrollThumbColor, shape = RoundedCornerShape(3.dp))
                )
            }
        }
    }
}

@Composable
fun LessonSectionItem(section: Section) {
    when (section.type) {
        "text" -> {
            val textSize = if (section.level == 1) Typography.titleMedium.fontSize else Typography.bodyMedium.fontSize
            val textColor = if (section.level == 1) Level1Color else Level2Color
            val fontWeight = if (section.level == 1) Typography.titleMedium.fontWeight else Typography.bodyMedium.fontWeight

            Text(
                text = section.content,
                fontSize = textSize,
                color = textColor,
                fontWeight = fontWeight
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
