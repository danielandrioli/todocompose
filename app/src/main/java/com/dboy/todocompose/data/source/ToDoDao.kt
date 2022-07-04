package com.dboy.todocompose.data.source

import androidx.room.*
import com.dboy.todocompose.data.models.ToDoTask
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllTasks() : Flow<List<ToDoTask>>

    @Query("SELECT * FROM todo_table WHERE id = :id")
    fun getSingleTask(id: Int) : Flow<ToDoTask>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upSertTask(task: ToDoTask)

    @Delete
    suspend fun deleteTask(task: ToDoTask)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM todo_table WHERE title LIKE '%' || :searchQuery || '%' " +
            "OR description LIKE '%' || :searchQuery || '%'")
    fun searchDatabase(searchQuery: String) : Flow<List<ToDoTask>>

    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'L%' " +
            "THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority() : Flow<List<ToDoTask>>

    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'H%' " +
            "THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority() : Flow<List<ToDoTask>>

    @Query("SELECT * FROM todo_table ORDER BY timeStamp ASC")
    fun sortByDateStartingFromFirst() : Flow<List<ToDoTask>>

    @Query("SELECT * FROM todo_table ORDER BY timeStamp DESC")
    fun sortByDateStartingFromLatest() : Flow<List<ToDoTask>>  //NEM PRECISA DESSES DOIS MÉTODOS. O ITEM JÁ É SALVO NO BANCO DE DADOS POR ORDEM DE CRIACAO.
}