package com.dboy.todocompose.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dboy.todocompose.data.models.ToDoTask

@Database(entities = [ToDoTask::class], version = 2, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {

    abstract fun toDoDao() : ToDoDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE todo_table ADD COLUMN isDone INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}