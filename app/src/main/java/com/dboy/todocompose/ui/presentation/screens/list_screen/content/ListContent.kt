package com.dboy.todocompose.ui.presentation.screens.list_screen.content

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.dboy.todocompose.data.models.ToDoTask
import com.dboy.todocompose.utils.RequestState

@Composable
fun ListContent(
    taskList: RequestState<List<ToDoTask>>,
    navController: NavHostController
) {
    when (taskList) {
        is RequestState.Success -> {
            if (taskList.data.isEmpty()) {
                EmptyContent()
            } else {
                LazyColumn {
                    items(items = taskList.data,
                        key = {
                            it.id
                        }) {
                        TaskItem(toDoTask = it, navController = navController)
                    }
                }
            }
        }
        is RequestState.Loading -> ""
        is RequestState.Error -> ErrorContent()
        is RequestState.Idle -> ""
    }

}