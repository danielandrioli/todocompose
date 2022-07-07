package com.dboy.todocompose.ui.presentation.screens.task_screen.task_bars

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.dboy.todocompose.ui.theme.topAppBarBackgroundColor
import com.dboy.todocompose.ui.theme.topAppBarContentColor
import com.dboy.todocompose.utils.Action

@Composable
fun UpsertTaskAppBar(
    taskName: String,
    onActionClick: (Action) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            BackAction(onBackClick = onActionClick)
        },
        title = {
            Text(
                text = taskName,
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = {
            UpsertAction(onAddClick = onActionClick)
        }
    )
}


@Composable
@Preview
fun PreviewNewTaskAppbar() {
    val navController = rememberNavController()
    UpsertTaskAppBar("") {}
}