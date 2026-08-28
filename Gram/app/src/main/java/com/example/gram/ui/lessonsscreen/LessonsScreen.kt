package com.example.gram.ui.lessonsscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed // Changed from items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gram.model.LessonItem

@Composable
fun LessonsScreen(
    sourceTitle: String,
    lessons: List<LessonItem>, // Updated type
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
                // Combine index + 1 with the JSON title
                val displayText = "${index + 1}. ${lessonItem.title}"

                Text(
                    text = displayText,
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
