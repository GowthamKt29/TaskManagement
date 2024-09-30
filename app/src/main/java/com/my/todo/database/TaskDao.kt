package com.my.todo.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.my.todo.model.Task
import kotlinx.coroutines.flow.Flow


@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Query("SELECT * FROM task WHERE status = :status")
    fun getTasks(status: String): Flow<List<Task>>

    @Delete
    suspend fun deleteItem(task: Task)

    @Query("UPDATE task SET status = :status WHERE id = :taskId")
    suspend fun updateTaskStatusById(taskId: Int, status: String)
}