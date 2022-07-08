package com.dboy.todocompose.ui.presentation.screens.task_screen

import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.dboy.todocompose.data.models.Priority
import com.dboy.todocompose.ui.presentation.screens.task_screen.content.UpsertTaskContent
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
        if (taskId != -1) viewModel.getSingleTask(taskId) else viewModel.cleanCurrentTask()
        viewModel.editMode.value = false
    }

//    val currentTask by viewModel.task.collectAsState()
    val editMode by viewModel.editMode.collectAsState()
    val upsertTaskTitle by viewModel.upsertTaskTitle.collectAsState()
    val upsertTaskDescription by viewModel.upsertTaskDescription.collectAsState()
    val upsertTaskPriority: Priority by viewModel.upsertTaskPriority.collectAsState()
    val upsertTaskTimeStamp: Long by viewModel.upsertTaskTimeStamp.collectAsState()
    val upsertTaskId: Int by viewModel.upsertTaskId.collectAsState()

    Scaffold(topBar = {
        TaskAppBar(
            taskId = taskId, editMode = editMode, taskTitle = upsertTaskTitle
        ) { action ->
            when (action) {
                Action.NO_ACTION -> {
                    navController.popBackStack()
                }
                Action.EDIT -> viewModel.editMode.value = true
//                Action.DELETE -> viewModel DELETE SINGLE TASK
                Action.UPSERT -> {
                    //pegar title, description, priority, id, e timeStamp, criar um ToDoTask e salvar
                }
            }
        }
    }) {
        if (taskId != -1 && !editMode) {
            //details content
        } else { //criar ToDoTask ou editar uma...
            UpsertTaskContent(
                taskTitle = upsertTaskTitle,
                taskDescription = upsertTaskDescription,
                taskPriority = upsertTaskPriority,
                onTitleChange = {
                    viewModel.upsertTaskTitle.value = it
                },
                onDescriptionChange = {
                    viewModel.upsertTaskDescription.value = it
                },
                onPriorityChange = {
                    viewModel.upsertTaskPriority.value = it
                }
            )
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