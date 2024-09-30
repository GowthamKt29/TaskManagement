package com.my.todo.repository


import com.google.common.truth.Truth.assertThat
import com.my.todo.data.getTask
import com.my.todo.database.TaskDao
import com.my.todo.util.DataState
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class TaskRepositoryTest {

    @Mock
    lateinit var taskDao: TaskDao

    private lateinit var taskRepository: TaskRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        taskRepository = TaskRepository(taskDao)
    }

    @Test
    fun addTask_success_returnsTaskId(): Unit = runTest {
        // Given
        val task = getTask()
        `when`(taskDao.insertTask(task)).thenReturn(1L)

        // When
        val result = taskRepository.addTask(task)

        // Then
        assertThat(result).isInstanceOf(DataState.Success::class.java)
        assertThat((result as DataState.Success).data).isEqualTo(1L)
        verify(taskDao).insertTask(task)
    }

    @Test
    fun addTask_failure_returnsError(): Unit = runTest {
        // Given
        val task = getTask()
        `when`(taskDao.insertTask(task)).thenThrow(RuntimeException("Database Error"))

        // When
        val result = taskRepository.addTask(task)

        // Then
        assertThat(result).isInstanceOf(DataState.Error::class.java)
        assertThat((result as DataState.Error).exception).hasMessageThat()
            .contains("Database Error")
        verify(taskDao).insertTask(task)
    }

    @Test
    fun getTasks_success_returnsTaskList(): Unit = runTest {
        // Given
        val status = "OnGoing"
        val taskList = listOf(getTask())
        `when`(taskDao.getTasks(status)).thenReturn(flowOf(taskList))

        // When
        val result = taskRepository.getTasks(status).toList()

        // Then
        assertThat(result[0]).isInstanceOf(DataState.Success::class.java)
        assertThat((result[0] as DataState.Success).data).isEqualTo(taskList)
        verify(taskDao).getTasks(status)
    }

    @Test
    fun getTasks_empty_returnsIdleState(): Unit = runTest {
        // Given
        val status = "Done"
        `when`(taskDao.getTasks(status)).thenReturn(flowOf(emptyList()))

        // When
        val result = taskRepository.getTasks(status).toList()

        // Then
        assertThat(result[0]).isInstanceOf(DataState.Idle::class.java)
        verify(taskDao).getTasks(status)
    }

    @Test
    fun getTasks_failure_returnsError(): Unit = runTest {
        // Given
        val status = "OnGoing"
        `when`(taskDao.getTasks(status)).thenThrow(RuntimeException("Database Error"))

        // When
        val result = taskRepository.getTasks(status).toList()

        // Then
        assertThat(result[0]).isInstanceOf(DataState.Error::class.java)
        assertThat((result[0] as DataState.Error).exception).hasMessageThat()
            .contains("Database Error")
        verify(taskDao).getTasks(status)
    }
}