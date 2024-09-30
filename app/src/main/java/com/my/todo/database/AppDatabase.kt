package com.my.todo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.my.todo.model.Task

@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}