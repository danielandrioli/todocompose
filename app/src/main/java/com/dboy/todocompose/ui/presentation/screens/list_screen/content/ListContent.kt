package com.dboy.todocompose.ui.presentation.screens.list_screen.content

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.dboy.todocompose.R
import com.dboy.todocompose.data.models.ToDoTask
import com.dboy.todocompose.ui.presentation.view_model.SharedViewModel
import com.dboy.todocompose.utils.RequestState
import com.dboy.todocompose.utils.SearchAppBarState

@Composable
fun ListContent(
    taskList: RequestState<List<ToDoTask>>,
    navController: NavHostController,
    viewModel: SharedViewModel
) {
    val selectedTasks = viewModel.selectedTasks
    Log.i("ListContent", "${selectedTasks.toList()}, SelectMode: ${viewModel.selectMode.value}")

    when (taskList) {
        is RequestState.Success -> {
            if (taskList.data.isEmpty()) {
                if (viewModel.searchTextState.value.isNotEmpty()) {
                    EmptyContent(
                        emptyContentText = stringResource(id = R.string.no_results_found),
                        contentDescription = stringResource(id = R.string.neutral_face_icon),
                        painterResource = R.drawable.ic_sentiment_neutral
                    )
                } else if (viewModel.searchAppBarState.value == SearchAppBarState.CLOSED) {
                    EmptyContent(
                        emptyContentText = stringResource(id = R.string.empty_list),
                        contentDescription = stringResource(id = R.string.empty_list_description),
                        painterResource = R.drawable.nothing
                    )
                }
            } else {
                LazyColumn {
                    items(items = taskList.data,
                        key = {
                            it.id
                        }) {
                        TaskItem(
                            toDoTask = it, navController = navController,
                            isTaskSelected = selectedTasks.contains(it.id),
                            isSelectModeOn = viewModel.selectMode.value,
                            searchAppBarState = viewModel.searchAppBarState.value,
                            selectTask = { taskId ->
                                if (selectedTasks.contains(taskId)) {
                                    selectedTasks.remove(taskId)
                                } else {
                                    selectedTasks.add(taskId)
                                }
                                viewModel.selectMode.value = !selectedTasks.isEmpty()
                            }
                        )
                    }
                }
            }
        }
        is RequestState.Loading -> "" //CRIAR AQUI UMA TELA DE LOADING
        is RequestState.Error -> {
            EmptyContent(
                emptyContentText = stringResource(id = R.string.error_message),
                contentDescription = stringResource(id = R.string.sad_face_icon),
                painterResource = R.drawable.ic_sentiment_dissatisfied
            )
        }
        is RequestState.Idle -> ""
    }

}