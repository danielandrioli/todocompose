package com.dboy.todocompose.ui.presentation.navigation

import androidx.navigation.NavHostController
import com.dboy.todocompose.utils.Actions

const val LIST_SCREEN_BASE_ROUTE = "list_screen"
const val TASK_SCREEN_BASE_ROUTE = "task_screen"
const val LIST_ARGUMENT_KEY = "action"
const val TASK_ARGUMENT_KEY = "taskId"

sealed class Screen(val route: String) {
    object List : Screen(route = "$LIST_SCREEN_BASE_ROUTE/{$LIST_ARGUMENT_KEY}") {
        fun goToListScreen(navController: NavHostController, action: Actions) { //ARRUMAR, PROVAVELMENTE
            navController.navigate(route = "$LIST_SCREEN_BASE_ROUTE/${action.name}") {
                popUpTo(route) {
                    inclusive = true
                }
            }
        }
    }
    object Task : Screen(route = "$TASK_SCREEN_BASE_ROUTE/{$TASK_ARGUMENT_KEY}") {
        fun goToTaskScreen(navController: NavHostController, taskId: Int) {
            navController.navigate(route = "$TASK_SCREEN_BASE_ROUTE/$taskId")
        }
    }
}
