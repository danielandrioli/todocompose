package com.dboy.todocompose.ui.presentation.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dboy.todocompose.UnsuccessfulRequestException
import com.dboy.todocompose.data.models.Priority
import com.dboy.todocompose.data.models.ToDoTask
import com.dboy.todocompose.data.repository.DataStoreRepository
import com.dboy.todocompose.data.repository.FakeToDoRepository
import com.dboy.todocompose.data.repository.ToDoRepository
import com.dboy.todocompose.ui.presentation.DispatcherProvider
import com.dboy.todocompose.ui.presentation.FakeDispatchers
import com.dboy.todocompose.utils.RequestState
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock


@OptIn(ExperimentalCoroutinesApi::class)
class SharedViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SharedViewModel
    private lateinit var fakeDispatcher: DispatcherProvider
    private lateinit var fakeRepository: ToDoRepository
    private lateinit var taskExample: ToDoTask
    private lateinit var dataStore: DataStoreRepository


    @Before
    fun setup() {
        fakeDispatcher = FakeDispatchers()
        fakeRepository = FakeToDoRepository()
//        dataStore = mock(DataStoreRepository::class.java)
        viewModel = SharedViewModel(fakeRepository, fakeDispatcher, dataStore)
        taskExample = ToDoTask(
            title = "Testar o código",
            description = "Esse é apenas um simples teste.",
            priority = Priority.HIGH,
            timeStamp = 1010L
        )
    }

//    @Test
//    fun `inserting a task works`() {
//        runTest {
//            viewModel.upSertTask(taskExample)
//            viewModel.getAllTasks()
//
//            val taskRequest = viewModel.taskRequisitionState.value
//            if (taskRequest is RequestState.Success) {
//                assertThat(viewModel.nonePriorityTaskList.toList().first()).isEqualTo(taskExample)
//            } else {
//                throw UnsuccessfulRequestException("The request made from ViewModel was not successful!")
//            }
//        }
//    }
//
//    @Test
//    fun `deleting selected tasks works`() {//NAO TA ATUALIZANDO COM A LISTA DO REPOSITORIO FAKE
//        runTest {
//            viewModel.upSertTask(taskExample)
//            viewModel.upSertTask(taskExample.copy(title = "Another one", id = 1))
//            viewModel.getAllTasks()
//            viewModel.selectedTasks.add(0)
//            viewModel.selectedTasks.add(1)
//
//            viewModel.deleteMultipleSelectedTasks()
//
//            val taskRequest = viewModel.taskRequisitionState.value
//            if (taskRequest is RequestState.Success) {
//                println("aaa" + viewModel.nonePriorityTaskList.toList())
//                assertThat(viewModel.nonePriorityTaskList.toList()).isEmpty()
//            } else {
//                throw UnsuccessfulRequestException("The request made from ViewModel was not successful!")
//            }
//        }
//    }
//
//    @Test
//    fun `replacing a tasks works`() {
//        runTest {
//            viewModel.upSertTask(taskExample)
//            val newTask = taskExample.copy(title = "The new") //keeping the same id
//            viewModel.upSertTask(newTask)
//            viewModel.getAllTasks()
//
//            val taskRequest = viewModel.taskRequisitionState.value
//            if (taskRequest is RequestState.Success) {
//                assertThat(viewModel.nonePriorityTaskList.toList().first()).isEqualTo(newTask)
//            } else {
//                throw UnsuccessfulRequestException("The request made from ViewModel was not successful!")
//            }
//        }
//    }
}