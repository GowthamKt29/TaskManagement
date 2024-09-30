package com.my.todo.repository

import com.my.todo.database.TaskDao
import com.my.todo.model.Task
import com.my.todo.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class TaskRepository @Inject constructor(val taskDao: TaskDao) {

    suspend fun addTask(task: Task): DataState<Long> {
        return try {
            val id = taskDao.insertTask(task)
            DataState.Success(id)
        } catch (e: Exception) {
            DataState.Error(e)
        }
    }

    suspend fun getTasks(status: String): Flow<DataState<List<Task>>> = flow {
        try {
            taskDao.getTasks(status)
                .catch { e ->
                    emit(DataState.Error(Exception(e))) // Emit error state if an exception occurs
                }
                .collect { tasks ->
                    if (tasks.isNotEmpty()) {
                        emit(DataState.Success(tasks)) // Emit success state with the collected list of tasks
                        return@collect
                    }
                    emit(DataState.Idle)
                }
        } catch (e: Exception) {
            emit(DataState.Error(Exception(e)))
        }
    }
}