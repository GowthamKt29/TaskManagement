package com.my.todo.view.screens.addtask

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.todo.model.Task
import com.my.todo.repository.TaskRepository
import com.my.todo.util.DataState
import com.my.todo.util.TaskNotificationManager
import com.my.todo.util.getDateTimeFormat
import com.my.todo.util.getTimeFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class AddTaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val taskNotificationManager: TaskNotificationManager
) : ViewModel() {


    private val _currentDateUiState = mutableStateOf(LocalDate.now())
    val currentDateState = _currentDateUiState

    private val _selectedDate = mutableStateOf(LocalDate.now())
    val selectedDate = _selectedDate

    private val _nameUiState = mutableStateOf(TextInputUIState())
    val nameUiState: State<TextInputUIState> = _nameUiState

    private val _descriptionUiState = mutableStateOf(TextInputUIState())
    val descriptionUiState: State<TextInputUIState> = _descriptionUiState

    private val _isChecked = mutableStateOf(false)
    val isChecked: State<Boolean> = _isChecked

    private val _startTime = mutableStateOf(Calendar.getInstance().apply {
        add(Calendar.MINUTE, 15)
    }.time)
    var startTime = _startTime

    private val _endTime = mutableStateOf(Calendar.getInstance().apply {
        time = _startTime.value
        add(Calendar.MINUTE, 15)
    }.time)
    var endTime = _endTime

    var priority: String = ""

    private val _uiEvent = mutableStateOf<AddTaskUiEffect?>(null)
    val uiEvent: State<AddTaskUiEffect?> = _uiEvent

    private val _showDialog = mutableStateOf(false)
    val showDialog: State<Boolean> = _showDialog


    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(addTaskUiEvent: AddTaskUiEvent) {
        when (addTaskUiEvent) {
            is AddTaskUiEvent.ChangeMonth -> {
                val newDate = when (addTaskUiEvent.month) {
                    -1 -> _currentDateUiState.value.minusMonths(1)
                    else -> _currentDateUiState.value.plusMonths(1)
                }
                _currentDateUiState.value = newDate
            }

            is AddTaskUiEvent.DateSelected -> {
                _selectedDate.value = addTaskUiEvent.date
            }

            is AddTaskUiEvent.DescriptionValueChanged -> {
                _descriptionUiState.value = _descriptionUiState.value.copy(
                    inputValue = addTaskUiEvent.description,
                    showError = false
                )
            }

            is AddTaskUiEvent.NameValueChanged -> {
                _nameUiState.value =
                    _nameUiState.value.copy(inputValue = addTaskUiEvent.name, showError = false)
            }

            AddTaskUiEvent.ScheduleClicked -> {
                _isChecked.value = !_isChecked.value
            }

            AddTaskUiEvent.AddTaskClicked -> {
                if (_nameUiState.value.inputValue.isEmpty()) {
                    _nameUiState.value = _nameUiState.value.copy(showError = true)
                } else if (_descriptionUiState.value.inputValue.isEmpty()) {
                    _descriptionUiState.value = _descriptionUiState.value.copy(showError = true)
                } else if (priority.isEmpty()) {
                    sendEvent(AddTaskUiEffect.ShowToast("Please select a priority"))
                } else {
                    addTaskToDatabase(null)
                }
            }

            AddTaskUiEvent.PopDismissed -> {
                _showDialog.value = false
                sendEvent(AddTaskUiEffect.NavigateBack)
            }

            AddTaskUiEvent.BackClicked -> {
                sendEvent(AddTaskUiEffect.NavigateBack)
            }

            is AddTaskUiEvent.CancelTaskClicked -> cancelTask(addTaskUiEvent.task)
            is AddTaskUiEvent.UpdateTaskClicked -> {
                addTaskToDatabase(addTaskUiEvent.task.id)
            }
        }
    }

    private fun addTaskToDatabase(taskId: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val dateString = _selectedDate.value?.format(formatter)
            val task = if (taskId != null) {
                Task(
                    id = taskId,
                    date = dateString.toString(),
                    name = _nameUiState.value.inputValue,
                    description = _descriptionUiState.value.inputValue,
                    isScheduled = _isChecked.value,
                    startTime = getTimeFormat().format(_startTime.value),
                    endTime = getTimeFormat().format(_endTime.value),
                    priority = priority,
                    status = "OnGoing",
                    notifyTime = getDateTimeFormat().format(_startTime.value)
                )
            } else {
                Task(
                    date = dateString.toString(),
                    name = _nameUiState.value.inputValue,
                    description = _descriptionUiState.value.inputValue,
                    isScheduled = _isChecked.value,
                    startTime = getTimeFormat().format(_startTime.value),
                    endTime = getTimeFormat().format(_endTime.value),
                    priority = priority,
                    status = "OnGoing",
                    notifyTime = getDateTimeFormat().format(_startTime.value)
                )
            }
            when (val result = taskRepository.addTask(task)) {
                is DataState.Success -> {
                    _showDialog.value = true
                    taskNotificationManager.scheduleTaskNotification(task.also {
                        it.id = result.data.toInt()
                    })
                }

                is DataState.Error -> {
                    sendEvent(AddTaskUiEffect.ShowToast(result.exception.message.toString()))
                }

                else -> {}
            }
        }
    }

    private fun cancelTask(task: Task) {
        task.also {
            it.status = "Halted"
        }
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = taskRepository.addTask(task)) {
                is DataState.Success -> {
                    _showDialog.value = true
                    taskNotificationManager.cancelTaskNotification(taskId = task.id)
                }

                is DataState.Error -> {
                    sendEvent(AddTaskUiEffect.ShowToast(result.exception.message.toString()))
                }

                else -> {}
            }
        }
    }

    fun updateTaskDetails(task: Task) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val parsedDate = LocalDate.parse(task.date, formatter)
        _currentDateUiState.value = parsedDate
        _selectedDate.value = parsedDate
        _nameUiState.value.inputValue = task.name
        _descriptionUiState.value = _descriptionUiState.value.copy(inputValue = task.description)
        _isChecked.value = task.isScheduled
        _startTime.value = getTimeFormat().parse(task.startTime)!!
        _endTime.value = getTimeFormat().parse(task.endTime)!!
        priority = task.priority
    }

    fun sendEvent(event: AddTaskUiEffect) {
        _uiEvent.value = event
    }

    fun clearEvent() {
        _uiEvent.value = null
    }
}

sealed class AddTaskUiEffect {
    data class ShowToast(val message: String) : AddTaskUiEffect()
    object NavigateBack : AddTaskUiEffect()
}