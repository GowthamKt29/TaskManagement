package com.my.todo.view.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.todo.model.Task
import com.my.todo.repository.TaskRepository
import com.my.todo.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {


    private val _tasks = MutableStateFlow<DataState<List<Task>>>(DataState.Idle)
    val tasks: StateFlow<DataState<List<Task>>> = _tasks.asStateFlow()


    fun getTasks(status: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTasks(status)
                .collect {
//                    Log.i("HomeViewModel",""+it.toString())
                    _tasks.value = it
                } // Directly collect and update the state
        }

    }
}