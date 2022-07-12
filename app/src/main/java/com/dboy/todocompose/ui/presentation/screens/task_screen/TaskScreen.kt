package com.dboy.todocompose.ui.presentation.screens.task_screen

import android.util.Log
import android.widget.Toast
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.dboy.todocompose.R
import com.dboy.todocompose.data.models.Priority
import com.dboy.todocompose.data.models.ToDoTask
import com.dboy.todocompose.ui.presentation.screens.task_screen.content.UpsertTaskContent
import com.dboy.todocompose.ui.presentation.screens.task_screen.task_bars.TaskAppBar
import com.dboy.todocompose.ui.presentation.view_model.SharedViewModel
import com.dboy.todocompose.utils.Action
import com.dboy.todocompose.utils.Constants.TASK_TITLE_MAX_CHARS
import com.dboy.todocompose.utils.DateFormater

@OptIn(ExperimentalComposeUiApi::class)
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
    val upsertTaskId: Int = if (taskId == -1) 0 else taskId
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val toastString = stringResource(id = R.string.task_saved)


    Scaffold(topBar = {
        TaskAppBar(
            taskId = taskId, editMode = editMode, taskTitle = upsertTaskTitle
        ) { action ->
            when (action) {
                Action.NO_ACTION -> {
                    navController.popBackStack()
                }
//                Action.DELETE -> viewModel DELETE SINGLE TASK
                Action.UPSERT -> {
                    val task = ToDoTask(
                        title = upsertTaskTitle,
                        description = upsertTaskDescription,
                        priority = upsertTaskPriority,
                        timeStamp = DateFormater.getTimeStampAsLong(),
                        id = upsertTaskId
                    )
                    viewModel.upSertTask(task)
                    viewModel.editMode.value = false
                    keyboardController?.hide()

                    Toast.makeText(context, toastString, Toast.LENGTH_SHORT).show()
                    if (taskId == -1) navController.popBackStack()
                }
            }
        }
    }) {
        //criar ToDoTask ou editar uma...
            UpsertTaskContent(
                taskTitle = upsertTaskTitle,
                taskDescription = upsertTaskDescription,
                taskPriority = upsertTaskPriority,
                onTitleChange = {
                    if (it.length <= TASK_TITLE_MAX_CHARS) {
                        viewModel.upsertTaskTitle.value = it
                    }
                },
                onDescriptionChange = {
                    viewModel.upsertTaskDescription.value = it
                },
                onPriorityChange = {
                    viewModel.upsertTaskPriority.value = it
                    viewModel.editMode.value = true
                },
                onFocusChangeToEditMode = {
                    viewModel.editMode.value = true
                },
                onBackButtonPressed = {
                    navController.popBackStack()
                }
            )
    }


}