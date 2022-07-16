package com.dboy.todocompose.ui.presentation.screens.task_screen

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
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
    Log.i("TaskScreen", "ENTERING task screen")
//    LaunchedEffect(key1 = true) {
//        Log.i("TaskScreen", "Launched effect - id: $taskId")
//        if (taskId != -1) viewModel.getSingleTask(taskId)
//        viewModel.editMode.value = false
//    }

//    val currentTask by viewModel.task.collectAsState()
    val editMode by viewModel.editMode
    val upsertTaskTitle by viewModel.upsertTaskTitle
    val upsertTaskDescription by viewModel.upsertTaskDescription
    val upsertTaskPriority: Priority by viewModel.upsertTaskPriority
    val upsertTaskId: Int = if (taskId == -1) 0 else taskId
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val toastString = stringResource(id = R.string.task_saved)

    Log.i("TaskScreen", "Edit Mode: $editMode | task title: $upsertTaskTitle | task id: $taskId")
    Scaffold(topBar = {
        TaskAppBar(
            taskId = taskId, editMode = editMode, taskTitle = upsertTaskTitle
        ) { action ->
            when (action) {
                Action.NO_ACTION -> {
                    if (taskId != -1) {
                        val task = ToDoTask(
                            title = upsertTaskTitle,
                            description = upsertTaskDescription,
                            priority = upsertTaskPriority,
                            timeStamp = DateFormater.getTimeStampAsLong(),
                            id = upsertTaskId
                        )
                        viewModel.upSertTask(task)
                    }
                    navController.popBackStack()
//                    viewModel.cleanCurrentTask()
                }
                Action.DELETE -> {
                    // TODO:
                }
                Action.UPSERT -> {
                    val task = ToDoTask(
                        title = upsertTaskTitle,
                        description = upsertTaskDescription,
                        priority = upsertTaskPriority,
                        timeStamp = DateFormater.getTimeStampAsLong(),
                        id = upsertTaskId
                    )
                    viewModel.upSertTask(task)
//                    viewModel.editMode.value = false
                    keyboardController?.hide()
                    Log.i("TaskScreen", "SAVING. TaskId: $taskId - title: $upsertTaskTitle")
                    Toast.makeText(context, toastString, Toast.LENGTH_SHORT).show()
                    if (taskId == -1) {
                        navController.popBackStack()
//                        viewModel.cleanCurrentTask()
                    }
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
//                viewModel.editMode.value = true
            },
            onFocusChangeToEditMode = {
                viewModel.editMode.value = it
            }
        )
    }

    BackHandler {
        if (taskId != -1) {
            val task = ToDoTask(
                title = upsertTaskTitle,
                description = upsertTaskDescription,
                priority = upsertTaskPriority,
                timeStamp = DateFormater.getTimeStampAsLong(),
                id = upsertTaskId
            )
            viewModel.upSertTask(task)
        }
        Log.i("TaskScreen", "task id: $taskId")
        navController.popBackStack()
//        viewModel.cleanCurrentTask()
    }
}