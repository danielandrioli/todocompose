package com.dboy.todocompose.ui.presentation.screens.list_screen.content

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.dboy.todocompose.R
import com.dboy.todocompose.data.models.Priority
import com.dboy.todocompose.data.models.ToDoTask
import com.dboy.todocompose.ui.presentation.view_model.SharedViewModel
import com.dboy.todocompose.utils.RequestState
import com.dboy.todocompose.utils.SearchAppBarState

@Composable
fun ListContent(
    taskListState: RequestState,
    navController: NavHostController,
    viewModel: SharedViewModel
) {
    val sortState by viewModel.sortRequestState.collectAsState()
    val priority by viewModel.mPriority.collectAsState()
    val lowPriorityTasks by viewModel.lowPriorityTasksOrderList.collectAsState()
    val highPriorityTasks by viewModel.highPriorityTasksOrderList.collectAsState()
    val nonePriorityTasks = viewModel.nonePriorityTaskList
    val lowPriorityTasksSearch = viewModel.lowPriorityTasksToHighSearch
    val highPriorityTasksSearch = viewModel.highPriorityTasksToLowSearch
    val nonePriorityTasksSearch = viewModel.nonePriorityTasksSearch

    val selectedTasks = viewModel.selectedTasks
//    Log.i("DBGListContent", "${selectedTasks.toList()}, SelectMode: ${viewModel.selectMode.value}")

    val taskList = when (sortState) {
        is RequestState.Success -> {
            if (viewModel.searchAppBarState.value == SearchAppBarState.OPENED && viewModel.searchTextState.value.isNotEmpty())  {
                when(priority) {
                    Priority.LOW -> lowPriorityTasksSearch
                    Priority.HIGH -> highPriorityTasksSearch
                    else -> nonePriorityTasksSearch
                }
            } else {
                when(priority) {
                    Priority.LOW -> lowPriorityTasks
                    Priority.HIGH -> highPriorityTasks
                    else -> {nonePriorityTasks}
                }
            }
        }
        else -> {
            emptyList<ToDoTask>()
        }
    }
    Log.i("DBGListContent", "TASK LIST: ${taskList.toList()}")


    when (taskListState) {
        is RequestState.Success -> {
//            val taskList = viewModel.taskList

            if (taskList.isEmpty()) {
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
                    items(items = taskList,
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
        is RequestState.Loading -> Log.i("DBGListContent", "Loading") //CRIAR AQUI UMA TELA DE LOADING
        is RequestState.Error -> {
            EmptyContent(
                emptyContentText = stringResource(id = R.string.error_message),
                contentDescription = stringResource(id = R.string.sad_face_icon),
                painterResource = R.drawable.ic_sentiment_dissatisfied
            )
            Log.i("DBGListContent", "Error")
        }
        is RequestState.Idle -> Log.i("DBGListContent", "Idle") //MUDAR
    }
}