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
    val editMode = mutableStateOf(false)

    val upsertTaskTitle = mutableStateOf("")
    val upsertTaskDescription = mutableStateOf("")
    val upsertTaskPriority = mutableStateOf(Priority.LOW)

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

    fun deleteTask(id: Int) {
        viewModelScope.launch(dispatcher.default) {
//            repository.deleteTask()
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
            if (id <= 0) _task.value = null else {
                repository.getSingleTask(id).collect() {
                    _task.value = it
                }
            }
        }
    }

    fun updateTextFields(task: ToDoTask) {
        upsertTaskTitle.value = task.title
        upsertTaskDescription.value = task.description
        upsertTaskPriority.value = task.priority
    }

    fun cleanCurrentTextFields() {
        upsertTaskTitle.value = ""
        upsertTaskDescription.value = ""
        upsertTaskPriority.value = Priority.LOW
    }
}