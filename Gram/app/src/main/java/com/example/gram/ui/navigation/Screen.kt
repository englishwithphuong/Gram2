package com.example.gram.ui.navigation

sealed class Screen(val route: String) {
    object Sources : Screen("sources")
    object Lessons : Screen("lessons/{sourceIndex}") {
        fun createRoute(sourceIndex: Int) = "lessons/$sourceIndex"
    }
}
