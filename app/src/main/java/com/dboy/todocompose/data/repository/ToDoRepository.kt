package com.dboy.todocompose.data.repository

import com.dboy.todocompose.data.models.ToDoTask
import kotlinx.coroutines.flow.Flow

interface ToDoRepository {

    fun getAllTasks() : Flow<List<ToDoTask>>
    fun getSingleTask(id: Int) : Flow<ToDoTask>
    suspend fun upSertTask(task: ToDoTask)
    suspend fun deleteTask(taskId: Int)
    suspend fun deleteSelectedTasks(vararg tasksId: Int)
    fun searchDatabaseNonePriorityOrder(searchQuery: String) : Flow<List<ToDoTask>>
    fun searchDatabaseLowPriorityOrder(searchQuery: String) : Flow<List<ToDoTask>>
    fun searchDatabaseHighPriorityOrder(searchQuery: String) : Flow<List<ToDoTask>>
    fun sortByLowPriority() : Flow<List<ToDoTask>>
    fun sortByHighPriority() : Flow<List<ToDoTask>>
    fun sortByDateStartingFromOlder() : Flow<List<ToDoTask>>
    fun sortByDateStartingFromLatest() : Flow<List<ToDoTask>>
}