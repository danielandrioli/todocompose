package com.dboy.todocompose.ui.presentation.screens.list_screen.app_bars

import android.util.Log
import androidx.compose.runtime.Composable
import com.dboy.todocompose.data.models.ToDoTask
import com.dboy.todocompose.ui.presentation.view_model.SharedViewModel
import com.dboy.todocompose.utils.RequestState
import com.dboy.todocompose.utils.SearchAppBarState

@Composable
fun ListAppBar(
    viewModel: SharedViewModel
) {
    if (viewModel.selectMode.value) {
        SelectTasksAppBar(
            selectedTasksQuantity = viewModel.selectedTasks.size,
            onDeleteTasks = {
                viewModel.deleteMultipleSelectedTasks()
                viewModel.selectMode.value = false
            },
            onCloseAppBar = {
                viewModel.selectMode.value = false
                viewModel.selectedTasks.clear()
            })
    } else if (viewModel.searchAppBarState.value == SearchAppBarState.CLOSED) {
        DefaultAppBar(
            onSearchClicked = {
                viewModel.searchAppBarState.value = SearchAppBarState.OPENED
            },
            onSortClicked = {
                if (viewModel.taskList.value is RequestState.Success) {
                    Log.d("DBGViewModel", "${(viewModel.taskList.value as RequestState.Success<List<ToDoTask>>).data}")
                }
            }
        )
    } else {
        SearchAppbar(
            text = viewModel.searchTextState.value,
            onTextChange = {
                viewModel.searchTextState.value = it
                viewModel.searchDatabase(it)
            },
            onCloseClicked = {
                if (viewModel.searchTextState.value.isNotEmpty()) {
                    viewModel.searchTextState.value = ""
                    viewModel.getAllTasks()
                }
                viewModel.searchAppBarState.value = SearchAppBarState.CLOSED
            },
            onSearchClicked = { }
        )
    }
}
