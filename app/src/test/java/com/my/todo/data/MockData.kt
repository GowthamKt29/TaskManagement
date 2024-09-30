package com.my.todo.data

import com.my.todo.model.Task


fun getTask(): Task {
    return Task(
        id = 1,
        date = "2024-09-26",
        name = "Test Task",
        description = "Task Description",
        startTime = "10:00 AM",
        priority = "High",
        status = "OnGoing",
        notifyTime = "09:45 AM"
    )
}