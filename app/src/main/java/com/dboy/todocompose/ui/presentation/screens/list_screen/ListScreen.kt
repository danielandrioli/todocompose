package com.dboy.todocompose.ui.presentation.screens.list_screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
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
import kotlinx.coroutines.launch

@Composable
fun ListScreen(
    navController: NavHostController,
    viewModel: SharedViewModel,
    endActivity: () -> Unit
) {
    LaunchedEffect(key1 = true) {
        viewModel.getAllTasks()
    }
    val allTasksState by viewModel.taskList.collectAsState()

    Scaffold(
        floatingActionButton = {
            ListFab(navController = navController)
        }, topBar = {
            ListAppBar(viewModel)
        }) {
        ListContent(allTasksState, navController, viewModel)
    }

    BackHandler {
        if (viewModel.selectMode.value) {
            viewModel.selectMode.value = false
            viewModel.selectedTasks.clear()
        } else
        if (viewModel.searchAppBarState.value == SearchAppBarState.OPENED) {
            viewModel.searchAppBarState.value = SearchAppBarState.CLOSED
            if (viewModel.searchTextState.value.isNotEmpty()) viewModel.getAllTasks()
            viewModel.searchTextState.value = ""
        } else {
//            navController.popBackStack() //this is the last screen in navigation, so popBackStack doesn't work properly
            endActivity()
        }
    }
}

@Composable
fun ListFab(navController: NavHostController) {
    FloatingActionButton(onClick = {
        Screen.Task.goToTaskScreen(navController, -1)
    }) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.fab_add_button)
        )
    }
}

@Composable
fun DisplaySnackbar(
    scaffoldState: ScaffoldState
) {
    val snackbarMessage = stringResource(id = R.string.task_deleted)
    val snackbarActionLabel = stringResource(id = R.string.undo_deletion)
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = true) {
        scope.launch {
            val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                message = snackbarMessage,
                actionLabel = snackbarActionLabel
            )
        }
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