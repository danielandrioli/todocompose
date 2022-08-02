package com.dboy.todocompose.ui.presentation.screens.list_screen.app_bars

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dboy.todocompose.R
import com.dboy.todocompose.ui.theme.topAppBarBackgroundColor
import com.dboy.todocompose.ui.theme.topAppBarContentColor

@Composable
fun SelectTasksAppBar(
    selectedTasksQuantity: Int,
    onDeleteTasks: () -> Unit,
    onCloseAppBar: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "$selectedTasksQuantity " + stringResource(
                    id = selectedTasksString(
                        selectedTasksQuantity
                    )
                ),
                color = MaterialTheme.colors.topAppBarContentColor
            )
        }, backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = {
            SelectAppBarActions(onDeleteTasks = onDeleteTasks, onCloseAppBar = onCloseAppBar)
        }
    )
}

@Composable
fun SelectAppBarActions(
    onDeleteTasks: () -> Unit,
    onCloseAppBar: () -> Unit
) {
    DeleteTasksAction(onDeleteTasks)
    CloseAppBarAction(onCloseAppBar)
}

@Composable
fun DeleteTasksAction(
    onDeleteClick: () -> Unit
) {
    IconButton(onClick = onDeleteClick) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(id = R.string.delete),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun CloseAppBarAction(
    onCloseClick: () -> Unit
) {
    IconButton(onClick = onCloseClick) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = stringResource(id = R.string.close_action),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}


fun selectedTasksString(quantity: Int): Int {
    return if (quantity > 1) R.string.selected_tasks else R.string.selected_task
}

@Preview
@Composable
fun PreviewSelectedTasksAppBar() {
    SelectTasksAppBar(selectedTasksQuantity = 2, onDeleteTasks = { /*TODO*/ }) {

    }
}