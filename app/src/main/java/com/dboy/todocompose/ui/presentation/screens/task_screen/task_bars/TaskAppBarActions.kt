package com.dboy.todocompose.ui.presentation.screens.task_screen.task_bars

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dboy.todocompose.R
import com.dboy.todocompose.ui.theme.topAppBarContentColor
import com.dboy.todocompose.utils.Action

@Composable
fun BackAction(
    onBackClick: (Action) -> Unit
) {
    IconButton(onClick = {
        onBackClick(Action.NO_ACTION)
    }
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.icon_navigate_back),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun UpsertAction(
    onAddClick: (Action) -> Unit
) {
    IconButton(onClick = {
        onAddClick(Action.UPSERT)
    }
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(id = R.string.icon_check),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun DeleteAction(
    onDeleteClick: (Action) -> Unit
) {
    IconButton(onClick = {
        onDeleteClick(Action.DELETE)
    }
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(id = R.string.icon_delete),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}