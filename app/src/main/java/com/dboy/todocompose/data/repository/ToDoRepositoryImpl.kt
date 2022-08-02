package com.dboy.todocompose.data.repository

import com.dboy.todocompose.data.models.ToDoTask
import com.dboy.todocompose.data.source.ToDoDao
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow

@ViewModelScoped //it tells Hilt this class instances will be alive as long the view model is
class ToDoRepositoryImpl(private val dao: ToDoDao) : ToDoRepository {
    override fun getAllTasks(): Flow<List<ToDoTask>> = dao.getAllTasks()

    override fun getSingleTask(id: Int): Flow<ToDoTask> = dao.getSingleTask(id)

    override suspend fun upSertTask(task: ToDoTask) = dao.upSertTask(task)

    override suspend fun deleteTask(id: Int) = dao.deleteSingleTask(id)
    override suspend fun deleteSelectedTasks(vararg tasksId: Int) = dao.deleteTasks(tasksId = tasksId)

    override fun searchDatabaseNonePriorityOrder(searchQuery: String): Flow<List<ToDoTask>> =
        dao.searchDatabaseNonePriorityOrder(searchQuery)

    override fun searchDatabaseLowPriorityOrder(searchQuery: String): Flow<List<ToDoTask>> =
        dao.searchDatabaseLowPriorityOrder(searchQuery)

    override fun searchDatabaseHighPriorityOrder(searchQuery: String): Flow<List<ToDoTask>> =
        dao.searchDatabaseHighPriorityOrder(searchQuery)

    override fun sortByLowPriority(): Flow<List<ToDoTask>> = dao.sortByLowPriority()

    override fun sortByHighPriority(): Flow<List<ToDoTask>> = dao.sortByHighPriority()
}