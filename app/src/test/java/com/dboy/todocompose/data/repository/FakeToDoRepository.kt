package com.dboy.todocompose.data.repository

import com.dboy.todocompose.data.models.ToDoTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeToDoRepository : ToDoRepository {
    private val listOfTasks = mutableListOf<ToDoTask>()

    override fun getAllTasks(): Flow<List<ToDoTask>> {
        return flow {
            emit(listOfTasks) //utilizar o emit ou não? Dá pra colocar a lista sem o emit, mas não fica suspend
        }
    }


    override fun getSingleTask(id: Int): Flow<ToDoTask> {
//        val task = listOfTasks[id]
        val task = listOfTasks.filter {
            it.id == id
        }
        return flow {
            emit(task.first())
        }
    }

    override suspend fun upSertTask(task: ToDoTask) {
        listOfTasks.add(task.id, task)
//        listOfTasks[task.id] = task
    }

    override suspend fun deleteTask(taskId: Int) {
        listOfTasks.removeAt(taskId)
    }

    override suspend fun deleteAllTasks() {
        listOfTasks.removeAll {
            true
        }
    }

    override fun searchDatabase(searchQuery: String): Flow<List<ToDoTask>> {
        TODO("Not yet implemented")
    }

    override fun sortByLowPriority(): Flow<List<ToDoTask>> {
        TODO("Not yet implemented")
    }

    override fun sortByHighPriority(): Flow<List<ToDoTask>> {
        TODO("Not yet implemented")
    }

    override fun sortByDateStartingFromFirst(): Flow<List<ToDoTask>> {
        TODO("Not yet implemented")
    }

    override fun sortByDateStartingFromLatest(): Flow<List<ToDoTask>> {
        TODO("Not yet implemented")
    }
}