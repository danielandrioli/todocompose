package com.dboy.todocompose.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dboy.todocompose.data.models.ToDoTask

@Database(entities = [ToDoTask::class], version = 1, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {

    abstract fun toDoDao() : ToDoDao
}