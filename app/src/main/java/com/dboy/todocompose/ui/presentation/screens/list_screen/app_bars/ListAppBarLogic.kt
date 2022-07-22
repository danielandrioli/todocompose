package com.dboy.todocompose.ui.presentation.screens.list_screen.app_bars

import androidx.compose.runtime.Composable
import com.dboy.todocompose.ui.presentation.view_model.SharedViewModel
import com.dboy.todocompose.utils.SearchAppBarState

@Composable
fun ListAppBar(
    viewModel: SharedViewModel
) {
    if (viewModel.selectMode.value) {
        SelectTasksAppBar(
            selectedTasksQuantity = viewModel.selectedTasks.size,
            onDeleteTasks = { /*TODO*/ },
            onCloseAppBar = {
                viewModel.selectMode.value = false
                viewModel.selectedTasks.clear()
            })
    } else if (viewModel.searchAppBarState.value == SearchAppBarState.CLOSED) {
        DefaultAppBar(
            onSearchClicked = {
                viewModel.searchAppBarState.value = SearchAppBarState.OPENED
            },
            onSortClicked = {},
            onDeleteAll = {}
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
