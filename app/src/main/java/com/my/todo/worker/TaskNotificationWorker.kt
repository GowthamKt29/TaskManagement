package com.my.todo.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.my.todo.database.TaskDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class TaskNotificationWorker @AssistedInject constructor(
    @Assisted private val taskDao: TaskDao,
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        Log.i("TaskNotificationWorker", "iam done")
        val taskId = inputData.getInt("task_id", -1)
        val isScheduled = inputData.getBoolean("isScheduled", false)
        val taskName = inputData.getString("task_name") ?: return@withContext Result.failure()
        val taskDescription =
            inputData.getString("task_description") ?: return@withContext Result.failure()
        // Show Notification
        if (isScheduled) {
            showNotification(taskId, taskName, taskDescription)
        }
        try {
            taskDao.updateTaskStatusById(taskId, "Done")
        } catch (_: Exception) {

        }
        Result.success()
    }

    private fun showNotification(taskId: Int, taskName: String, taskDescription: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "task_channel_id"

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Task Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(taskName)
            .setContentText(taskDescription)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        notificationManager.notify(taskId, notification)
    }
}