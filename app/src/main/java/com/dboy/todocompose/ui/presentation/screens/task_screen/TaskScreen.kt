package com.dboy.todocompose.ui.presentation.screens.task_screen

import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.dboy.todocompose.ui.presentation.screens.task_screen.task_bars.TaskAppBar
import com.dboy.todocompose.ui.presentation.view_model.SharedViewModel
import com.dboy.todocompose.utils.Action

@Composable
fun TaskScreen(
    navController: NavHostController,
    taskId: Int,
    viewModel: SharedViewModel
) {
    LaunchedEffect(key1 = true) {
        if (taskId != -1) viewModel.getSingleTask(taskId)
        viewModel.editMode.value = false
    }

    val currentTask by viewModel.task.collectAsState()
    val editMode by viewModel.editMode.collectAsState()

    Scaffold(topBar = {
        TaskAppBar(
            taskId = taskId, editMode = editMode, currentTask = currentTask
        ) { action ->
            when (action) {
                Action.NO_ACTION -> {
                    navController.popBackStack()
                }
                Action.EDIT -> viewModel.editMode.value = true
            }
        }
    }) {
        if (editMode) {
            //um tipo
        } else {
            //mostrar o content sem editText e dropdown proproty
        }
    }

    BackHandler {
        if (editMode) {
            viewModel.editMode.value = false
        } else {
            navController.popBackStack()
        }
    }
}