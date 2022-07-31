package com.dboy.todocompose.data.source

import androidx.test.filters.SmallTest
import com.dboy.todocompose.data.models.Priority
import com.dboy.todocompose.data.models.ToDoTask
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
@HiltAndroidTest
class ToDoDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_db")
    lateinit var toDoDatabase: ToDoDatabase
    private lateinit var dao: ToDoDao
    private lateinit var task: ToDoTask

    @Before
    fun setup() {
        hiltRule.inject()
        dao = toDoDatabase.toDoDao()
        task = ToDoTask(
            "My test", "Just a simple test.",
            Priority.HIGH, 1010L, id = 1
        )
        /* id = 0 não dá certo para testes.
        * Acho que o id que o room começa a gerar começa a partir do 1. O valor padrão igual a 0 é para gerar automaticamente. */
    }

    @After
    fun teardown() {
        toDoDatabase.close()
    }

    @Test
    fun insertTask() {
        runTest {
            dao.upSertTask(task)
            val taskRetrieved = dao.getAllTasks().first()[0]
            assertThat(task).isEqualTo(taskRetrieved)
        }
    }

    @Test
    fun getSingleTask() {
        runTest {
            dao.upSertTask(task)
            val taskRetrieved = dao.getSingleTask(task.id).first()
            assertThat(task).isEqualTo(taskRetrieved)
        }
    }

    @Test
    fun deleteTask() {
        runTest {
            dao.upSertTask(task)
            dao.deleteSingleTask(task.id)
            val allTasks = dao.getAllTasks().first()
            assertThat(allTasks).isEmpty()
        }
    }

    @Test
    fun deleteAllTasks() {
        runTest {
            dao.upSertTask(task)
            dao.upSertTask(task.copy(title = "Newwww", id = 2))
            dao.deleteAllTasks()

            val allTasks = dao.getAllTasks().first()
            assertThat(allTasks).isEmpty()
        }
    }

    @Test
    fun searchTaskWithQuery() {
        runTest {
            dao.upSertTask(task)
            dao.upSertTask(task.copy(title = "Luva de pedreiro", id = 2))

            val queryResult = dao.searchDatabaseNonePriorityOrder("pedre").first()[0]
            assertThat(queryResult.title).contains("Luva de pedreiro")
        }
    }

    @Test
    fun sortByLowPriority() {
        runTest {
            val newTask = task.copy(id = 0)

            dao.upSertTask(newTask)
            dao.upSertTask(newTask.copy(title = "Mah oe"))
            dao.upSertTask(newTask.copy(title = "Hihihi", priority = Priority.LOW))
            dao.upSertTask(newTask.copy(title = "Cabelo twonight", priority = Priority.MEDIUM))

            val taskList = dao.sortByLowPriority().first()
            assertThat(taskList[0].priority).isEqualTo(Priority.LOW)
        }
    }

    @Test
    fun sortByHighPriority() {
        runTest {
            val newTask = task.copy(id = 0)

            dao.upSertTask(newTask)
            dao.upSertTask(newTask.copy(title = "Mah oe"))
            dao.upSertTask(newTask.copy(title = "Hihihi", priority = Priority.LOW))
            dao.upSertTask(newTask.copy(title = "Cabelo twonight", priority = Priority.MEDIUM))

            val taskList = dao.sortByHighPriority().first()
            assertThat(taskList[0].priority).isEqualTo(Priority.HIGH)
        }
    }

    @Test
    fun sortByDateStartingFromFirst() {
        runTest {
            val newTask = task.copy(id = 0)

            dao.upSertTask(newTask)
            dao.upSertTask(newTask.copy(title = "Mah oe", timeStamp = 3L))
            dao.upSertTask(newTask.copy(title = "Hihihi", timeStamp = 2L))
            dao.upSertTask(newTask.copy(title = "Cabelo twonight", timeStamp = 9999L))

            val taskList = dao.sortByDateStartingFromOlder().first()
            assertThat(taskList[0].timeStamp).isEqualTo(2L)
        }
    }

    @Test
    fun sortByDateStartingFromLatest() {
        runTest {
            val newTask = task.copy(id = 0)

            dao.upSertTask(newTask)
            dao.upSertTask(newTask.copy(title = "Mah oe", timeStamp = 3L))
            dao.upSertTask(newTask.copy(title = "Hihihi", timeStamp = 2L))
            dao.upSertTask(newTask.copy(title = "Cabelo twonight", timeStamp = 9999L))

            val taskList = dao.sortByDateStartingFromLatest().first()
            assertThat(taskList[0].timeStamp).isEqualTo(9999L)
        }
    }
}