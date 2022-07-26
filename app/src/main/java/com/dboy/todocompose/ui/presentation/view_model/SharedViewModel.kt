package com.dboy.todocompose.ui.presentation.view_model

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dboy.todocompose.data.models.Priority
import com.dboy.todocompose.data.models.ToDoTask
import com.dboy.todocompose.data.repository.ToDoRepository
import com.dboy.todocompose.ui.presentation.DispatcherProvider
import com.dboy.todocompose.utils.DateFormater
import com.dboy.todocompose.utils.RequestState
import com.dboy.todocompose.utils.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: ToDoRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {
    private val _taskList = MutableStateFlow<RequestState<MutableList<ToDoTask>>>(RequestState.Idle)
    val taskList: StateFlow<RequestState<MutableList<ToDoTask>>> = _taskList
    var lista = mutableStateListOf<ToDoTask>()
    val searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
    val searchTextState: MutableState<String> = mutableStateOf("")
    private val _task = MutableStateFlow<ToDoTask?>(null)
    val task: StateFlow<ToDoTask?> = _task
    val editMode = mutableStateOf(false)
    val selectMode = mutableStateOf(false)
    val selectedTasks = mutableStateListOf<Int>()
    val upsertTaskId = mutableStateOf(0)
    val upsertTaskTitle = mutableStateOf("")
    val upsertTaskDescription = mutableStateOf("")
    val upsertTaskPriority = mutableStateOf(Priority.LOW)

    init {
        getAllTasks()
    }

    fun getAllTasks() {
        _taskList.value = RequestState.Loading
        viewModelScope.launch(dispatcher.io) {
            try {
                repository.getAllTasks().collect {
                    _taskList.value = RequestState.Success(it.toMutableList())//MUDAR ESSA SEALED CLASS. DESNECESSÁRIO SEGURAR DADO AGORA

                    withContext(dispatcher.main) {//snapshots are transactional and run on ui thread, this is why I need to change dispatcher
                        lista.clear()
                        lista.addAll(it.toMutableStateList())
                    }
                }
            } catch (e: Exception) {
                _taskList.value = RequestState.Error(e)
                Log.d("DBGViewModel", "error: ${e.localizedMessage}")
            }
        }
        Log.d("DBGViewModel", "getAllTasks function")
    }
    /*
    PROBLEM I HAD: When the user selected multiple tasks on the ListScreen and pressed the delete icon, not always the LazyColumn
    got refreshed. Always the data were deleted (verified by logs).
    After the unsuccessful recomposition, if the user left the app (or entered another screen) and went back to ListScreen,
    then the UI would get recomposed and the list successfully refreshed.

    Solution: the sealed class responsible for the requisition state (success, error, idle) is not holding anymore the data from
    the repository. Now this object is responsible just for telling the UI about the success or not of the requisition.
    Now there's a property that is a mutableStateListOf<ToDoTask> and it holds the collected data from the repository.

    I believe this is a Compose bug.
     */
    fun deleteTask(id: Int) {
        viewModelScope.launch(dispatcher.io) {
            try {
                repository.deleteTask(id)
            } catch (e: Exception) {
                Log.e("DBGViewModel", "task deletion error: ${e.localizedMessage}")
            }
        }
    }


    fun upSertTask(task: ToDoTask) {
        viewModelScope.launch(dispatcher.io) {
            repository.upSertTask(task)
        }
    }

    fun deleteMultipleSelectedTasks() {
        viewModelScope.launch(dispatcher.io) {
            repository.deleteSelectedTasks(tasksId = selectedTasks.toIntArray())
            selectedTasks.clear()
        }
        Log.d("DBGViewModel", "deleteMultipleTasks function.")
    }

    fun searchDatabase(query: String) {
        viewModelScope.launch(dispatcher.io) {
            repository.searchDatabase(query).collect() {
                _taskList.value = RequestState.Success(it.toMutableList())
            }
        }
    }

    fun getSingleTaskFromDb(id: Int) {
        viewModelScope.launch(dispatcher.io) {
            if (id <= 0) _task.value = null else {
                repository.getSingleTask(id).collect() {
                    _task.value = it
                }
            }
        }
    }

    fun getCurrentTask(): ToDoTask {
        return ToDoTask(
            title = upsertTaskTitle.value,
            description = upsertTaskDescription.value,
            priority = upsertTaskPriority.value,
            timeStamp = DateFormater.getTimeStampAsLong(),
            id = upsertTaskId.value
        )
    }

    fun updateTextFields(task: ToDoTask) {
        upsertTaskTitle.value = task.title
        upsertTaskDescription.value = task.description
        upsertTaskPriority.value = task.priority
        upsertTaskId.value = task.id
    }

    fun cleanCurrentTextFields() {
        upsertTaskTitle.value = ""
        upsertTaskDescription.value = ""
        upsertTaskPriority.value = Priority.LOW
        upsertTaskId.value = 0
    }
}