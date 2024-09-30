package com.my.todo.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val date: String,
    val isScheduled: Boolean = false,
    val name: String,
    val description: String,
    var startTime: String,
    val endTime: String = "",
    val priority: String,
    var status: String,
    var notifyTime: String,
)