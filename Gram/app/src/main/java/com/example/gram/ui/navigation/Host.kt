package com.example.gram.ui.navigation

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gram.data.NavigationPrefs
import com.example.gram.model.SourceItem
import com.example.gram.ui.lessonsscreen.LessonsRouteContent
import com.example.gram.ui.sourcesscreen.SourcesScreen
import com.example.gram.ui.viewmodel.LessonsViewModel
import androidx.compose.runtime.collectAsState
import com.example.gram.ui.lessoncontentscreen.LessonContentScreen

@Composable
fun Host(sources: List<SourceItem>) {
    val navController = rememberNavController()
    val context = LocalContext.current

    LaunchedEffect(navController) {
        saveRoute(navController, context)
    }

    val startRoute = NavigationPrefs.load(context) ?: Screen.Sources.route

    LaunchedEffect(Unit) {
        restoreBackstack(navController, startRoute)
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Sources.route,
        modifier = Modifier.fillMaxSize()
    ) {
        sourcesListRoute(sources, navController)
        lessonsListRoute(sources, navController)
        lessonContentRoute(navController)
    }
}

private fun saveRoute(navController: NavController, context: Context) {
    navController.addOnDestinationChangedListener { _, destination, arguments ->
        val route = destination.route?.let { baseRoute ->
            if (arguments != null && arguments.keySet().isNotEmpty()) {
                arguments.keySet().fold(baseRoute) { acc, key ->
                    val value = arguments.get(key)?.toString() ?: ""
                    acc.replace("{$key}", value)
                }
            } else baseRoute
        }
        route?.let { NavigationPrefs.save(context, it) }
    }
}

private fun restoreBackstack(navController: NavHostController, startRoute: String) {
    when {
        startRoute.startsWith("lesson_content/") -> {
            val parts = startRoute.split("/")
            val sourceIndex = parts.getOrNull(1)?.toIntOrNull()
            val lessonIndex = parts.getOrNull(2)?.toIntOrNull() // Changed to Int
            if (sourceIndex != null && lessonIndex != null) {
                navController.navigate(Screen.Lessons.createRoute(sourceIndex))
                navController.navigate(Screen.LessonContent.createRoute(sourceIndex, lessonIndex))
            }
        }
        startRoute.startsWith("lessons/") -> {
            val sourceIndex = startRoute.removePrefix("lessons/").toIntOrNull()
            if (sourceIndex != null) {
                navController.navigate(Screen.Lessons.createRoute(sourceIndex))
            }
        }
    }
}

private fun NavGraphBuilder.sourcesListRoute(sources: List<SourceItem>, navController: NavController) {
    composable(Screen.Sources.route) {
        SourcesScreen(sources = sources) { sourceItem ->
            navController.navigate(Screen.Lessons.createRoute(sourceItem.index))
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
            onLessonClick = { lessonIndex ->
                navController.navigate(Screen.LessonContent.createRoute(sourceIndex, lessonIndex))
            }
        )
    }
}

private fun NavGraphBuilder.lessonContentRoute(navController: NavHostController) {
    composable(
        route = Screen.LessonContent.route,
        arguments = listOf(
            navArgument("sourceIndex") { type = NavType.IntType },
            navArgument("lessonIndex") { type = NavType.IntType } // Changed type
        )
    ) { backStackEntry ->
        val sourceIndex = backStackEntry.arguments?.getInt("sourceIndex") ?: 0
        val lessonIndex = backStackEntry.arguments?.getInt("lessonIndex") ?: 0 // Read as Int

        val viewModel: LessonsViewModel = viewModel(
            factory = LessonsViewModel.provideFactory(sourceIndex)
        )
        LessonContentScreen(
            navController = navController,
            sourceIndex = sourceIndex,
            lessonIndex = lessonIndex,
            lessonCount = viewModel.lessons.collectAsState().value.count()
        )
    }
}
