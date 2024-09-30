package com.my.todo.util

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.my.todo.model.Task
import com.my.todo.worker.TaskNotificationWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val workManager = WorkManager.getInstance(context)

    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleTaskNotification(task: Task) {
        val delay = calculateTimeDifference(date = task.date, startTime = task.startTime)

        val workRequest = OneTimeWorkRequestBuilder<TaskNotificationWorker>()
            .setInitialDelay(delay)
            .setInputData(
                workDataOf(
                    "task_id" to task.id,
                    "task_name" to task.name,
                    "task_description" to task.description,
                    "isScheduled" to task.isScheduled
                )
            )
            .build()

        workManager.enqueueUniqueWork(task.id.toString(), ExistingWorkPolicy.REPLACE, workRequest)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateTaskNotification(task: Task) {
        scheduleTaskNotification(task)
    }

    fun cancelTaskNotification(taskId: Int) {
        workManager.cancelUniqueWork(taskId.toString())
    }
}