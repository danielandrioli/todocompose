package com.dboy.todocompose.ui.presentation.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dboy.todocompose.data.models.Priority
import com.dboy.todocompose.data.models.ToDoTask
import com.dboy.todocompose.data.repository.FakeToDoRepository
import com.dboy.todocompose.data.repository.ToDoRepository
import com.dboy.todocompose.ui.presentation.DispatcherProvider
import com.dboy.todocompose.ui.presentation.FakeDispatchers
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
class SharedViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SharedViewModel
    private lateinit var fakeDispatcher: DispatcherProvider
    private lateinit var fakeRepository: ToDoRepository
    private lateinit var taskExample: ToDoTask

    @Before
    fun setup() {
        fakeDispatcher = FakeDispatchers()
        fakeRepository = FakeToDoRepository()
        viewModel = SharedViewModel(fakeRepository, fakeDispatcher)
        taskExample = ToDoTask(
            title = "Testar o código",
            description = "Esse é apenas um simples teste.",
            priority = Priority.HIGH,
            timeStamp = 1010L
        )
    }

    @Test
    fun `inserting a task works`() {
        runTest {
            viewModel.upSertTask(taskExample)
            viewModel.getAllTasks()

            assertThat(viewModel.taskList.value.first()).isEqualTo(taskExample)
        }
    }

    @Test
    fun `deleting all tasks works`() {
        runTest {
            viewModel.upSertTask(taskExample)
            viewModel.upSertTask(taskExample.copy(title = "Another one", id = 1))
            viewModel.getAllTasks()

            assertThat(viewModel.taskList.value).isNotEmpty()
            viewModel.deleteAllTasks()
            assertThat(viewModel.taskList.value).isEmpty()
        }
    }

    @Test
    fun `replacing a tasks works`() {
        runTest {
            viewModel.upSertTask(taskExample)
            val newTask = taskExample.copy(title = "The new") //keeping the same id
            viewModel.upSertTask(newTask)
            viewModel.getAllTasks()

            assertThat(viewModel.taskList.value.first()).isEqualTo(newTask)
        }
    }
}