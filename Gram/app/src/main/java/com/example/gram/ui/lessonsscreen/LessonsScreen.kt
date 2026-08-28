package com.example.gram.ui.lessonsscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gram.data.ExpandedStatePrefs
import com.example.gram.model.LessonItem
import com.example.gram.ui.lessoncontentscreen.components.buildStyledChineseText
import com.example.gram.ui.theme.KaitiFontFamily

sealed interface LessonUiItem {
    data class Header(val chunkIndex: Int, val title: String) : LessonUiItem
    data class Lesson(val index: Int, val item: LessonItem) : LessonUiItem
}

@Composable
fun LessonsScreen(
    sourceIndex: Int,
    sourceTitle: String,
    lessons: List<LessonItem>,
    listState: LazyListState,
    onLessonClick: (Int) -> Unit
) {
    val context = LocalContext.current

    // Group lessons into chunks of 10
    val chunks = remember(lessons) { lessons.chunked(10) }

    // Track expansion state for each chunk
    val chunkExpandedStates = remember(sourceIndex, chunks) {
        mutableStateMapOf<Int, Boolean>().apply {
            chunks.forEachIndexed { chunkIndex, _ ->
                val isSavedExpanded = ExpandedStatePrefs.loadChunkExpanded(context, sourceIndex, chunkIndex, false)
                put(chunkIndex, isSavedExpanded)
            }
        }
    }

    val uiItems = buildList {
        chunks.forEachIndexed { chunkIndex, chunkLessons ->
            val start = chunkIndex * 10 + 1
            val end = start + chunkLessons.size - 1
            val headerTitle = "$start–$end"

            add(
                LessonUiItem.Header(
                    chunkIndex = chunkIndex,
                    title = headerTitle
                )
            )

            if (chunkExpandedStates[chunkIndex] == true) {
                chunkLessons.forEachIndexed { relativeIndex, lessonItem ->
                    val absoluteIndex = chunkIndex * 10 + relativeIndex

                    add(
                        LessonUiItem.Lesson(
                            index = absoluteIndex,
                            item = lessonItem
                        )
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = sourceTitle,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = uiItems,
                key = { uiItem ->
                    when (uiItem) {
                        is LessonUiItem.Header -> "header_${uiItem.chunkIndex}"
                        is LessonUiItem.Lesson -> "lesson_${uiItem.index}"
                    }
                }
            ) { uiItem ->
                when (uiItem) {
                    is LessonUiItem.Header -> {
                        val isExpanded = chunkExpandedStates[uiItem.chunkIndex] == true
                        val arrow = if (isExpanded) "▼" else "▶"

                        Text(
                            text = "$arrow  ${uiItem.title}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Cyan,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    val newState = !isExpanded
                                    chunkExpandedStates[uiItem.chunkIndex] = newState
                                    ExpandedStatePrefs.saveChunkExpanded(
                                        context = context,
                                        sourceIndex = sourceIndex,
                                        chunkIndex = uiItem.chunkIndex,
                                        isExpanded = newState
                                    )
                                }
                                .padding(vertical = 12.dp, horizontal = 4.dp)
                        )
                    }
                    is LessonUiItem.Lesson -> {
                        val displayText = "${uiItem.index + 1}. ${uiItem.item.title}"

                        val styledDisplayText = remember(displayText) {
                            buildStyledChineseText(
                                text = displayText,
                                chineseFontFamily = KaitiFontFamily
                            )
                        }

                        Text(
                            text = styledDisplayText,
                            fontSize = 18.sp,
                            color = Color.Yellow,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onLessonClick(uiItem.index) }
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}
