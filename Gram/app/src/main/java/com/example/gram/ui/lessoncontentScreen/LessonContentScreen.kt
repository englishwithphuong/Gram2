package com.example.gram.ui.lessoncontentScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gram.ui.viewmodel.LessonContentViewModel

@Composable
fun LessonContentScreen(
    sourceIndex: Int,
    lessonName: String
) {
    val viewModel: LessonContentViewModel = viewModel(
        factory = LessonContentViewModel.provideFactory(sourceIndex, lessonName)
    )
    val lesson by viewModel.lesson.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
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
                // Later you can easily add custom blocks like tables or images here:
                // "table" -> { /* Render Table composable */ }
            }
        }
    }
}
