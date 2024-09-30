package com.my.todo.view.screens.addtask

import com.my.todo.model.Task
import java.time.LocalDate

sealed interface AddTaskUiEvent {
    class ChangeMonth(val month: Int) : AddTaskUiEvent
    class DateSelected(val date: LocalDate) : AddTaskUiEvent
    class NameValueChanged(val name: String) : AddTaskUiEvent
    class DescriptionValueChanged(val description: String) : AddTaskUiEvent
    object ScheduleClicked : AddTaskUiEvent
    object AddTaskClicked : AddTaskUiEvent
    class UpdateTaskClicked(val task: Task) : AddTaskUiEvent
    class CancelTaskClicked(val task: Task) : AddTaskUiEvent
    object BackClicked : AddTaskUiEvent
    object PopDismissed : AddTaskUiEvent
}