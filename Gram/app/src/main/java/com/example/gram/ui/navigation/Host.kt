package com.example.gram.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gram.model.SourceItem
import com.example.gram.ui.lessonsscreen.LessonContentScreen
import com.example.gram.ui.lessonsscreen.LessonsRouteContent
import com.example.gram.ui.sourcesscreen.SourcesScreen

@Composable
fun Host(sources: List<SourceItem>) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Sources.route,
        modifier = Modifier.fillMaxSize()
    ) {
        // 1. Sources List Route
        composable(Screen.Sources.route) {
            BackHandler(enabled = true) {}
            SourcesScreen(sources = sources) { sourceItem ->
                navController.navigate(Screen.Lessons.createRoute(sourceItem.index))
            }
        }

        // 2. Lessons List Route
        composable(
            route = Screen.Lessons.route,
            arguments = listOf(navArgument("sourceIndex") { type = NavType.IntType })
        ) { backStackEntry ->
            val sourceIndex = backStackEntry.arguments?.getInt("sourceIndex") ?: 0
            LessonsRouteContent(
                sources = sources,
                sourceIndex = sourceIndex,
                onLessonClick = { lessonName ->
                    navController.navigate(Screen.LessonContent.createRoute(sourceIndex, lessonName))
                }
            )
        }

        // 3. Lesson Content Route
        composable(
            route = Screen.LessonContent.route,
            arguments = listOf(
                navArgument("sourceIndex") { type = NavType.IntType },
                navArgument("lessonName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val sourceIndex = backStackEntry.arguments?.getInt("sourceIndex") ?: 0
            val lessonName = backStackEntry.arguments?.getString("lessonName") ?: ""

            LessonContentScreen(
                sourceIndex = sourceIndex,
                lessonName = lessonName
            )
        }
    }
}
