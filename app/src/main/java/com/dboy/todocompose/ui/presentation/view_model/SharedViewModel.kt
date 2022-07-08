package com.dboy.todocompose.ui.presentation.view_model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dboy.todocompose.data.models.Priority
import com.dboy.todocompose.data.models.ToDoTask
import com.dboy.todocompose.data.repository.ToDoRepository
import com.dboy.todocompose.ui.presentation.DispatcherProvider
import com.dboy.todocompose.utils.RequestState
import com.dboy.todocompose.utils.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: ToDoRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {
    private val _taskList = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val taskList: StateFlow<RequestState<List<ToDoTask>>> = _taskList
    val searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
    val searchTextState: MutableState<String> = mutableStateOf("")
    private val _task = MutableStateFlow<ToDoTask?>(null)
    val task: StateFlow<ToDoTask?> = _task
    val editMode = MutableStateFlow<Boolean>(false)

    val upsertTaskTitle = MutableStateFlow<String>("")
    val upsertTaskDescription = MutableStateFlow<String>("")
    val upsertTaskTimeStamp = MutableStateFlow<Long>(0L)
    val upsertTaskPriority = MutableStateFlow<Priority>(Priority.LOW)
    val upsertTaskId = MutableStateFlow<Int>(0)

    fun getAllTasks() {
        _taskList.value = RequestState.Loading
        viewModelScope.launch(dispatcher.default) {
            try {
                repository.getAllTasks().collect {
                    _taskList.value = RequestState.Success(it)
                }
            } catch (e: Exception) {
                _taskList.value = RequestState.Error(e)
            }
        }
    }

    fun upSertTask(task: ToDoTask) {
        viewModelScope.launch(dispatcher.default) {
            repository.upSertTask(task)
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch(dispatcher.default) {
            repository.deleteAllTasks()
        }
    }

    fun searchDatabase(query: String) {
        viewModelScope.launch(dispatcher.default) {
            repository.searchDatabase(query).collect() {
                _taskList.value = RequestState.Success(it)
            }
        }
    }

    fun getSingleTask(id: Int){
        viewModelScope.launch(dispatcher.default) {
            repository.getSingleTask(id).collect() {
                upsertTaskTitle.value = it.title
                upsertTaskDescription.value = it.description
                upsertTaskPriority.value = it.priority
                upsertTaskTimeStamp.value = it.timeStamp
                upsertTaskId.value = it.id
            }
        }
    }

    fun cleanCurrentTask() {
        upsertTaskTitle.value = ""
        upsertTaskDescription.value = ""
        upsertTaskPriority.value = Priority.LOW
        upsertTaskTimeStamp.value = 0L
        upsertTaskId.value = 0
    }
}