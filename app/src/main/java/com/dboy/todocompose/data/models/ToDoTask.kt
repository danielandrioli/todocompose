package com.dboy.todocompose.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dboy.todocompose.utils.Constants.DATABASE_TODO_TABLE

@Entity(tableName = DATABASE_TODO_TABLE)
data class ToDoTask(
    val title: String,
    val description: String,
    val priority: Priority,
    val timeStamp: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
