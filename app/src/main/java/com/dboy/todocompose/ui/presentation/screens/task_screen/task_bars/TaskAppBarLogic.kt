package com.dboy.todocompose.ui.presentation.screens.task_screen.task_bars

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dboy.todocompose.R
import com.dboy.todocompose.utils.Action

@Composable
fun TaskAppBar(
    taskId: Int,
    editMode: Boolean,
    taskTitle: String,
    onActionClick: (Action) -> Unit
) {
    UpsertTaskAppBar(
        taskName = if (taskId == -1) stringResource(id = R.string.new_task) else taskTitle,
        onActionClick = onActionClick,
        editMode = editMode,
        newTask = taskId == -1
    )
}