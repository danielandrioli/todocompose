package com.dboy.todocompose.ui.presentation.screens.list_screen

import androidx.activity.compose.BackHandler
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dboy.todocompose.R
import com.dboy.todocompose.data.models.Priority
import com.dboy.todocompose.data.models.ToDoTask
import com.dboy.todocompose.ui.components.DeleteTaskBottomSheet
import com.dboy.todocompose.ui.presentation.navigation.Screen
import com.dboy.todocompose.ui.presentation.screens.list_screen.app_bars.ListAppBar
import com.dboy.todocompose.ui.presentation.screens.list_screen.content.ListContent
import com.dboy.todocompose.ui.presentation.view_model.SharedViewModel
import com.dboy.todocompose.ui.theme.ToDoComposeTheme
import com.dboy.todocompose.utils.DateFormater
import com.dboy.todocompose.utils.SearchAppBarState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListScreen(
    navController: NavHostController,
    viewModel: SharedViewModel,
    endActivity: () -> Unit
) {
    val allTasksState by viewModel.taskRequisitionState.collectAsState()
    val selectedPriorityOrder by viewModel.mPriority.collectAsState()
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
//    val sortState by viewModel.sortState.collectAsState()
//    val lowPriorityTasks by viewModel.lowPriorityTasks.collectAsState()
//    val highPriorityTasks by viewModel.highPriorityTasks.collectAsState()

    DeleteTaskBottomSheet(
        sheetState = modalBottomSheetState,
        scope = scope,
        sheetText = if (viewModel.selectedTasks.size > 1) stringResource(
            id = R.string.delete_multiple_selected_tasks,
            viewModel.selectedTasks.size
        )
        else stringResource(id = R.string.delete_selected_task),
        onConfirmDeletionClick = {
            viewModel.deleteMultipleSelectedTasks()
            viewModel.selectMode.value = false
            scope.launch {
                modalBottomSheetState.hide()
            }
        }) {
        Scaffold(
            floatingActionButton = {
                if (viewModel.searchAppBarState.value == SearchAppBarState.CLOSED && !viewModel.selectMode.value) {
                    ListFab(navController = navController, viewModel)
                }
            }, topBar = {
                ListAppBar(
                    viewModel, onDeleteTasks = {
                        scope.launch {
                            modalBottomSheetState.show()
                        }
                    },
                    selectedPriorityOrder = selectedPriorityOrder
                )
            }) {
            ListContent(allTasksState, navController, viewModel)
        }
    }


    BackHandler {
        if (modalBottomSheetState.isVisible) {
            scope.launch {
                modalBottomSheetState.hide()
            }
        } else if (viewModel.selectMode.value) {
            viewModel.selectMode.value = false
            viewModel.selectedTasks.clear()
        } else
            if (viewModel.searchAppBarState.value == SearchAppBarState.OPENED) {
                viewModel.searchAppBarState.value = SearchAppBarState.CLOSED
                viewModel.searchTextState.value = ""
            } else {
//            navController.popBackStack() //this is the last screen in navigation, so popBackStack doesn't work properly
                endActivity()
            }
    }
}

@Composable
fun ListFab(navController: NavHostController, viewModel: SharedViewModel) {
    FloatingActionButton(onClick = {
//        createTasksForTest(viewModel)
        Screen.Task.goToTaskScreen(navController, -1)
    }) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.fab_add_button)
        )
    }
}

fun createTasksForTest(viewModel: SharedViewModel) {
    for (i in 1..5) { //JUST FOR TEST
        viewModel.upSertTask(
            ToDoTask(
                title = "$i",
                description = "tst",
                priority = Priority.LOW,
                DateFormater.getTimeStampAsLong()
            )
        )
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