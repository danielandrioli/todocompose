package com.dboy.todocompose.di

import android.content.Context
import androidx.room.Room
import com.dboy.todocompose.data.repository.ToDoRepository
import com.dboy.todocompose.data.repository.ToDoRepositoryImpl
import com.dboy.todocompose.data.source.ToDoDatabase
import com.dboy.todocompose.ui.presentation.DispatcherProvider
import com.dboy.todocompose.ui.presentation.StandardDispatchers
import com.dboy.todocompose.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ToDoDatabase =
        Room.databaseBuilder(context, ToDoDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideRepository(db: ToDoDatabase) : ToDoRepository = ToDoRepositoryImpl(db.toDoDao())

    @Singleton
    @Provides
    fun provideDispatcherProvider() : DispatcherProvider = StandardDispatchers()
}