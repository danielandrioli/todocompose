package com.dboy.todocompose.ui.presentation.navigation

import androidx.navigation.NavHostController

const val LIST_SCREEN_BASE_ROUTE = "list_screen"
const val TASK_SCREEN_BASE_ROUTE = "task_screen"
const val SPLASH_SCREEN_BASE_ROUTE = "splash_screen"
const val TASK_ARGUMENT_KEY = "taskId"

sealed class Screen(val route: String) {
    object Splash : Screen(route = SPLASH_SCREEN_BASE_ROUTE) {
        fun goToListScreen(navController: NavHostController) {
            navController.navigate(route = LIST_SCREEN_BASE_ROUTE) {
                popUpTo(SPLASH_SCREEN_BASE_ROUTE) {
                    inclusive = true
                }
            }
        }
    }
    object List : Screen(route = LIST_SCREEN_BASE_ROUTE)
    object Task : Screen(route = "$TASK_SCREEN_BASE_ROUTE/{$TASK_ARGUMENT_KEY}") {
        fun goToTaskScreen(navController: NavHostController, taskId: Int) {
            navController.navigate(route = "$TASK_SCREEN_BASE_ROUTE/$taskId")
        }
    }
}
