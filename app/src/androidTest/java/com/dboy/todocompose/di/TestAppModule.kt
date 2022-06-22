package com.dboy.todocompose.di

import android.content.Context
import androidx.room.Room
import com.dboy.todocompose.data.source.ToDoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Named("test_db")
    @Provides
    fun providesInMemoryDb(@ApplicationContext context: Context) : ToDoDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            ToDoDatabase::class.java
        ).allowMainThreadQueries().build()
    }
}