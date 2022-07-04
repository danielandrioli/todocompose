package com.dboy.todocompose.ui.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dboy.todocompose.ui.presentation.screens.list_screen.ListScreen
import com.dboy.todocompose.ui.presentation.screens.task_screen.TaskScreen
import com.dboy.todocompose.ui.presentation.view_model.SharedViewModel

@Composable
fun SetupNavHost(
    navController: NavHostController,
    viewModel: SharedViewModel
) {
    NavHost(navController = navController, startDestination = Screen.List.route) {
        composable(
            route = Screen.List.route,
            arguments = listOf(navArgument(LIST_ARGUMENT_KEY) {
                type = NavType.StringType
            })
        ) {
            ListScreen(navController = navController, viewModel)
        }

        composable(
            route = Screen.Task.route,
            arguments = listOf(navArgument(TASK_ARGUMENT_KEY) {
                type = NavType.IntType
            })
        ) {
            val taskArgument = it.arguments?.getInt(TASK_ARGUMENT_KEY)
            taskArgument?.let { taskId ->
                TaskScreen(navController = navController, taskId = taskId)
            }
        }
    }
}