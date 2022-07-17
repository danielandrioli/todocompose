package com.dboy.todocompose.ui.presentation.screens.task_screen

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.dboy.todocompose.R
import com.dboy.todocompose.data.models.Priority
import com.dboy.todocompose.ui.presentation.screens.task_screen.content.UpsertTaskContent
import com.dboy.todocompose.ui.presentation.screens.task_screen.task_bars.TaskAppBar
import com.dboy.todocompose.ui.presentation.view_model.SharedViewModel
import com.dboy.todocompose.utils.Action
import com.dboy.todocompose.utils.Constants.TASK_TITLE_MAX_CHARS

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TaskScreen(
    navController: NavHostController,
    taskId: Int,
    viewModel: SharedViewModel
) {
    Log.i("TaskScreen", "ENTERING task screen")
    val editMode by viewModel.editMode
    val upsertTaskTitle by viewModel.upsertTaskTitle
    val upsertTaskDescription by viewModel.upsertTaskDescription
    val upsertTaskPriority: Priority by viewModel.upsertTaskPriority
    val upsertTaskId: Int by viewModel.upsertTaskId

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val toastStringTaskSaved = stringResource(id = R.string.task_saved)
    val toastStringTaskDeleted = stringResource(id = R.string.task_deleted)

    Log.i("TaskScreen", "Edit Mode: $editMode | task title: $upsertTaskTitle | task id: $taskId")
    Scaffold(topBar = {
        TaskAppBar(
            taskId = taskId, editMode = editMode, taskTitle = upsertTaskTitle
        ) { action ->
            when (action) {
                Action.NO_ACTION -> {//não quero salvar se id for -1
                    if (taskId != -1 && upsertTaskTitle.isEmpty() && upsertTaskDescription.isEmpty()) {
                        viewModel.deleteTask(upsertTaskId)
                        Toast.makeText(context, toastStringTaskDeleted, Toast.LENGTH_SHORT).show()
                    } else if (taskId != -1) {
                        val currentTask = viewModel.getCurrentTask()
                        viewModel.upSertTask(currentTask)
                    }
                    navController.popBackStack()
                }
                Action.DELETE -> {
                    viewModel.deleteTask(upsertTaskId)
                    Toast.makeText(context, toastStringTaskDeleted, Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
                Action.UPSERT -> {
                    keyboardController?.hide()
                    if (taskId == -1) {
                        if (upsertTaskTitle.isNotEmpty() || upsertTaskDescription.isNotEmpty()) {
                            val currentTask = viewModel.getCurrentTask()
                            viewModel.upSertTask(currentTask)
                            Toast.makeText(context, toastStringTaskSaved, Toast.LENGTH_SHORT).show()
                        }
                        navController.popBackStack()
                    } else {
                        if (upsertTaskTitle.isEmpty() && upsertTaskDescription.isEmpty()) {
                            viewModel.deleteTask(upsertTaskId)
                            Toast.makeText(context, toastStringTaskDeleted, Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        } else {
                            val currentTask = viewModel.getCurrentTask()
                            viewModel.upSertTask(currentTask)
                            Toast.makeText(context, toastStringTaskSaved, Toast.LENGTH_SHORT).show()
                        }
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
            },
            onFocusChangeToEditMode = {
                viewModel.editMode.value = it
            }
        )
    }

    BackHandler {
        if (taskId != -1 && upsertTaskTitle.isEmpty() && upsertTaskDescription.isEmpty()) {
            viewModel.deleteTask(upsertTaskId)
            Toast.makeText(context, toastStringTaskDeleted, Toast.LENGTH_SHORT).show()
        } else if (taskId != -1) {
            val currentTask = viewModel.getCurrentTask()
            viewModel.upSertTask(currentTask)
        }
        navController.popBackStack()
    }
}
