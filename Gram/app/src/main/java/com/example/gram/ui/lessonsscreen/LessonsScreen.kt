package com.example.gram.ui.lessonsscreen

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gram.ui.viewmodel.LessonContentViewModel

@Composable
fun LessonsScreen(
    sourceTitle: String,
    lessons: List<String>,
    onLessonClick: (String) -> Unit // <-- Added parameter here
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
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(lessons) { lesson ->
                Text(
                    text = lesson,
                    fontSize = 18.sp,
                    color = Color.Yellow,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onLessonClick(lesson) } // <-- Connected click handler
                        .padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun LessonContentScreen(
    sourceIndex: Int,
    lessonName: String
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application

    val viewModel: LessonContentViewModel = viewModel(
        factory = LessonContentViewModel.provideFactory(application, sourceIndex, lessonName)
    )
    val content by viewModel.content.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()) // Enables scrolling for text
    ) {
        Text(
            text = lessonName,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = content,
            fontSize = 16.sp,
            color = Color.LightGray
        )
    }
}
