package com.dboy.todocompose.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dboy.todocompose.data.models.ToDoTask
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {
    @Query("SELECT * FROM todo_table ORDER BY timeStamp DESC")
    fun getAllTasks(): Flow<List<ToDoTask>>

    @Query("SELECT * FROM todo_table WHERE id = :id")
    fun getSingleTask(id: Int): Flow<ToDoTask>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upSertTask(task: ToDoTask)

    @Query("DELETE FROM todo_table WHERE id = :id")
    suspend fun deleteSingleTask(id: Int)

    @Query("DELETE FROM todo_table WHERE id IN (:tasksId)")
    suspend fun deleteTasks(vararg tasksId: Int)

    @Query("SELECT * FROM todo_table WHERE title LIKE '%' || :searchQuery || '%' " +
                "OR description LIKE '%' || :searchQuery || '%' ORDER BY timeStamp DESC"
    )
    fun searchDatabaseNonePriorityOrder(searchQuery: String): Flow<List<ToDoTask>>

    @Query(
        "SELECT * FROM todo_table WHERE title LIKE '%' || :searchQuery || '%' " +
                "OR description LIKE '%' || :searchQuery || '%' ORDER BY CASE WHEN priority " +
                "LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END, timeStamp DESC"
    )
    fun searchDatabaseLowPriorityOrder(searchQuery: String): Flow<List<ToDoTask>>

    @Query(
        "SELECT * FROM todo_table WHERE title LIKE '%' || :searchQuery || '%' " +
                "OR description LIKE '%' || :searchQuery || '%' ORDER BY CASE WHEN priority LIKE 'H%' " +
                "THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END, timeStamp DESC"
    )
    fun searchDatabaseHighPriorityOrder(searchQuery: String): Flow<List<ToDoTask>>

    @Query(
        "SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'L%' " +
                "THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END, timeStamp DESC"
    )
    fun sortByLowPriority(): Flow<List<ToDoTask>>

    @Query(
        "SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'H%' " +
                "THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END, timeStamp DESC"
    )
    fun sortByHighPriority(): Flow<List<ToDoTask>>
}