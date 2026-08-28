package com.example.gram.ui.lessonsscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gram.model.LessonItem
import com.example.gram.ui.lessoncontentscreen.components.buildStyledChineseText
import com.example.gram.ui.theme.KaitiFontFamily

@Composable
fun LessonsScreen(
    sourceTitle: String,
    lessons: List<LessonItem>,
    listState: LazyListState,
    onLessonClick: (Int) -> Unit
) {
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
            itemsIndexed(lessons) { index, lessonItem ->
                val displayText = "${index + 1}. ${lessonItem.title}"

                // Apply KaitiFontFamily specifically to the Chinese characters in the lesson item title
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
                        .clickable { onLessonClick(index) }
                        .padding(vertical = 8.dp)
                )
            }
        }
    }
}
