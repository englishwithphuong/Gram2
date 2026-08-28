package com.example.gram.ui.lessoncontentscreen.leftbar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gram.ui.navigation.Screen

@Composable
fun PreviousButton(
    navController: NavController,
    sourceIndex: Int,
    lessonIndex: Int
) {
    val previousLessonIndex = lessonIndex - 1

    BaseButton(
        onClick = {
            if (previousLessonIndex > -1) {
                navController.navigate(
                    Screen.LessonContent.createRoute(sourceIndex, previousLessonIndex)
                ) {
                    popUpTo(Screen.Lessons.createRoute(sourceIndex)) { inclusive = false }
                }
            }
        },
        enabled = lessonIndex > 0
    ) {
        Text(text = "▲", fontSize = 20.sp, modifier = Modifier.offset(y = (-2).dp))
    }
}

@Composable
fun LessonNumberButton(
    navController: NavController,
    sourceIndex: Int,
    lessonIndex: Int
) {
    BaseButton(
        onClick = {
            navController.navigate(Screen.Lessons.createRoute(sourceIndex))
        }
    ) {
        Text(
            text = "${lessonIndex + 1}",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.offset(x = (-1).dp)
        )
    }
}

@Composable
fun NextButton(
    navController: NavController,
    sourceIndex: Int,
    lessonIndex: Int,
    lessonCount: Int
) {
    val nextLessonIndex = lessonIndex + 1

    BaseButton(
        onClick = {
            if (nextLessonIndex < lessonCount) {
                navController.navigate(
                    Screen.LessonContent.createRoute(sourceIndex, nextLessonIndex)
                ) {
                    popUpTo(Screen.Lessons.createRoute(sourceIndex)) { inclusive = false }
                }
            }
        },
        enabled = lessonIndex < lessonCount - 1
    ) {
        Text(text = "▼", fontSize = 20.sp, modifier = Modifier.offset(y = (-2).dp))
    }
}
