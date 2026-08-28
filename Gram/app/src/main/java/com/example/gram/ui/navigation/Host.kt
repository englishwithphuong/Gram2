package com.example.gram.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gram.model.SourceItem
import com.example.gram.ui.lessoncontentScreen.LessonContentScreen
import com.example.gram.ui.lessonsscreen.LessonsRouteContent
import com.example.gram.ui.sourcesscreen.SourcesScreen

@Composable
fun Host(sources: List<SourceItem>) {
    Log.d("HostDebug", "Sources list content: $sources")
    Log.d("HostDebug", Screen.Sources.route)
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Sources.route,
        modifier = Modifier.fillMaxSize()
    ) {
        sourcesListRoute(sources, navController)
        lessonsListRoute(sources, navController)
        lessonContentRoute()
    }
}

private fun NavGraphBuilder.sourcesListRoute(sources: List<SourceItem>, navController: NavController) {
    composable(Screen.Sources.route) {
        SourcesScreen(sources = sources) { sourceItem ->
            navController.navigate(Screen.Lessons.createRoute(sourceItem.index))
            Log.d("HostDebug", Screen.Lessons.createRoute(sourceItem.index))
        }
    }
}

private fun NavGraphBuilder.lessonsListRoute(sources: List<SourceItem>, navController: NavController) {
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
}

private fun NavGraphBuilder.lessonContentRoute() {
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
