package com.my.todo.di

import android.app.Application
import androidx.room.Room
import com.my.todo.database.AppDatabase
import com.my.todo.database.TaskDao
import com.my.todo.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "task_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideItemDao(db: AppDatabase): TaskDao {
        return db.taskDao()
    }

    @Provides
    @Singleton
    fun provideTaskRepo(taskDao: TaskDao): TaskRepository {
        return TaskRepository(taskDao)
    }
}