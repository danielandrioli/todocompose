package com.dboy.todocompose.ui.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
            Log.i("TaskNavHost", "taskArgument: $taskArgument")
            taskArgument?.let { taskId ->
                viewModel.getSingleTaskFromDb(taskId)
                val taskState = viewModel.task.collectAsState()
                LaunchedEffect(key1 = taskState) {//como esse bloco depende do taskState, ele deve ser a key.
                    Log.i("TaskScreen", "Launched effect - id: $taskId")
                    viewModel.cleanCurrentTextFields()
                    taskState.value?.let { task ->
                        viewModel.updateTextFields(task)
                    }
                    viewModel.editMode.value = false
                }
                TaskScreen(navController = navController, taskId = taskId, viewModel)
            }
        }
    }
}