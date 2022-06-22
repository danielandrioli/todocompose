package com.dboy.todocompose.ui.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dboy.todocompose.data.models.ToDoTask
import com.dboy.todocompose.data.repository.ToDoRepository
import com.dboy.todocompose.ui.presentation.DispatcherProvider
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
    private val _taskList = MutableStateFlow<List<ToDoTask>>(emptyList())
    val taskList: StateFlow<List<ToDoTask>> = _taskList

    fun getAllTasks() {
        viewModelScope.launch(dispatcher.default) {
            repository.getAllTasks().collect {
                _taskList.value = it
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
}