package com.example.gram.ui.lessonsscreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gram.model.SourceItem
import com.example.gram.ui.viewmodel.LessonsViewModel
import com.example.gram.ui.viewmodel.ScrollStateViewModel

@Composable
fun LessonsRouteContent(
    sources: List<SourceItem>,
    sourceIndex: Int,
    onLessonClick: (Int) -> Unit
) {
    val lessonsViewModel: LessonsViewModel = viewModel(
        factory = LessonsViewModel.provideFactory(sourceIndex)
    )
    val lessons by lessonsViewModel.lessons.collectAsState()

    val scrollViewModel: ScrollStateViewModel = viewModel(
        factory = ScrollStateViewModel.Factory
    )
    val listState = scrollViewModel.getListScrollState(sourceIndex)

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect {
                scrollViewModel.saveListScrollState(sourceIndex, listState)
            }
    }

    LessonsScreen(
        sourceTitle = sources.find { it.index == sourceIndex }?.title ?: "Lessons",
        lessons = lessons,
        listState = listState,
        onLessonClick = onLessonClick
    )
}
