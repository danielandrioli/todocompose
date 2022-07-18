package com.dboy.todocompose.ui.presentation.screens.list_screen

import androidx.activity.compose.BackHandler
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dboy.todocompose.R
import com.dboy.todocompose.ui.presentation.navigation.Screen
import com.dboy.todocompose.ui.presentation.screens.list_screen.app_bars.ListAppBar
import com.dboy.todocompose.ui.presentation.screens.list_screen.content.ListContent
import com.dboy.todocompose.ui.presentation.view_model.SharedViewModel
import com.dboy.todocompose.ui.theme.ToDoComposeTheme
import com.dboy.todocompose.utils.SearchAppBarState

@Composable
fun ListScreen(
    navController: NavHostController,
    viewModel: SharedViewModel
) {
    LaunchedEffect(key1 = true) {
        viewModel.getAllTasks()
    }
    val allTasksState by viewModel.taskList.collectAsState()
    Scaffold(floatingActionButton = {
        ListFab(navController = navController)
    }, topBar = {
        ListAppBar(viewModel)
    }) {
        ListContent(allTasksState, navController)
    }

    BackHandler {
        if (viewModel.searchAppBarState.value == SearchAppBarState.OPENED) {
            viewModel.searchAppBarState.value = SearchAppBarState.CLOSED
            if (viewModel.searchTextState.value.isNotEmpty()) viewModel.getAllTasks()
            viewModel.searchTextState.value = ""
        } else {
            navController.popBackStack()
        }
    }
}

@Composable
fun ListFab(navController: NavHostController) {
    FloatingActionButton(onClick = {
        Screen.Task.goToTaskScreen(navController, -1)
    }) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = stringResource(id = R.string.fab_add_button))
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewListScreen() {
    val navController = rememberNavController()
    ToDoComposeTheme {
//        ListScreen(navController = navController, action = action.name)
    }
}