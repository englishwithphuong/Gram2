package com.example.gram.ui.lessoncontentscreen.components.leftbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LeftButtonBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    sourceIndex: Int,
    lessonIndex: Int,
    lessonCount: Int
) {
    DisableSelection {
        Column(
            modifier = modifier.wrapContentWidth(Alignment.Start),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            PreviousButton(navController, sourceIndex, lessonIndex)
            LessonNumberButton(navController, sourceIndex, lessonIndex)
            NextButton(navController, sourceIndex, lessonIndex, lessonCount)
        }
    }
}
