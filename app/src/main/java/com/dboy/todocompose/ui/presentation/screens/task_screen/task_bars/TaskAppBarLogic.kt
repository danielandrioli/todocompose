package com.dboy.todocompose.ui.presentation.screens.task_screen.task_bars

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.dboy.todocompose.R
import com.dboy.todocompose.data.models.ToDoTask
import com.dboy.todocompose.ui.presentation.view_model.SharedViewModel
import com.dboy.todocompose.utils.Action
import com.dboy.todocompose.utils.RequestState

@Composable
fun TaskAppBar(
    taskId: Int,
    editMode: Boolean,
    taskTitle: String,
    onActionClick: (Action) -> Unit
) {
    if (taskId == -1) {
        UpsertTaskAppBar(
            taskName = stringResource(id = R.string.new_task),
            onActionClick = onActionClick
        )
    } else {
        if (!editMode) {
            DetailsAppBar(
                taskName = taskTitle,
                onActionClick = onActionClick
            )
        } else {
            UpsertTaskAppBar(
                taskName = taskTitle,
                onActionClick = onActionClick
            )
        }
    }
}