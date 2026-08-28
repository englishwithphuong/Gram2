package com.example.gram.ui.navigation

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gram.model.SourceItem
import com.example.gram.ui.viewmodel.LessonsViewModel

@Composable
fun Host(sources: List<SourceItem>) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Sources.route,
        modifier = Modifier.fillMaxSize()
    ) {
        // 1. Sources List Screen
        composable(Screen.Sources.route) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 32.dp, horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(sources) { sourceItem ->
                    SourceItemRow(sourceItem = sourceItem) {
                        // Navigate to lessons screen passing the sourceIndex
                        navController.navigate(Screen.Lessons.createRoute(sourceItem.index))
                    }
                }
            }
        }

        // 2. Lessons List Screen
        composable(
            route = Screen.Lessons.route,
            arguments = listOf(navArgument("sourceIndex") { type = NavType.IntType })
        ) { backStackEntry ->
            val sourceIndex = backStackEntry.arguments?.getInt("sourceIndex") ?: 0
            val context = LocalContext.current
            val application = context.applicationContext as Application

            val lessonsViewModel: LessonsViewModel = viewModel(
                factory = LessonsViewModel.provideFactory(application, sourceIndex)
            )
            val lessons by lessonsViewModel.lessons.collectAsState()

            LessonsScreen(
                sourceTitle = sources.find { it.index == sourceIndex }?.title ?: "Lessons",
                lessons = lessons,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun SourceItemRow(sourceItem: SourceItem, onClick: () -> Unit) {
    Text(
        text = sourceItem.title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Yellow,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 4.dp)
    )
}

@Composable
fun LessonsScreen(
    sourceTitle: String,
    lessons: List<String>,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = onBack) {
            Text("Back")
        }
        Spacer(modifier = Modifier.height(16.dp))
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
                        .padding(8.dp)
                )
            }
        }
    }
}
