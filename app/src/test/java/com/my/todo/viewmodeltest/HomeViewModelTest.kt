package com.my.todo.viewmodeltest

import com.google.common.truth.Truth.assertThat
import com.my.todo.data.getTask
import com.my.todo.repository.TaskRepository
import com.my.todo.util.DataState
import com.my.todo.view.screens.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class HomeViewModelTest {
    @Mock
    private lateinit var repository: TaskRepository

    private lateinit var viewModel: HomeViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = HomeViewModel(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getTasks should update tasks with success state when repository returns tasks`(): Unit =
        runTest {
            val status = "OnGoing"
            val taskList = listOf(getTask())

            `when`(repository.getTasks(status)).thenReturn(flow {
                emit(DataState.Success(taskList))
            })
            viewModel.getTasks(status)
            advanceUntilIdle()
            val state = viewModel.tasks.value
            assertThat(state).isInstanceOf(DataState.Success::class.java)
            val successState = state as DataState.Success
            assertThat(successState.data).isEqualTo(taskList)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getTasks should update tasks with error state when repository throws exception`() =
        runTest {
            val status = "OnGoing"
            val exceptionMessage = "Something went wrong"
            `when`(repository.getTasks(status)).thenReturn(
                flow {
                    emit(DataState.Error(Exception(exceptionMessage)))
                }
            )
            viewModel.getTasks(status)
            advanceUntilIdle()
            val state = viewModel.tasks.value
            assertThat(state).isInstanceOf(DataState.Error::class.java)
            val errorState = state as DataState.Error
            assertThat(errorState.exception.message).isEqualTo(exceptionMessage)
        }

}