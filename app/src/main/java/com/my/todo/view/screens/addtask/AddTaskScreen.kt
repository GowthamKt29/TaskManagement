package com.my.todo.view.screens.addtask


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.my.todo.R
import com.my.todo.model.Task
import com.my.todo.ui.theme.Purple80
import com.my.todo.ui.theme.ToDoTheme
import com.my.todo.util.TimePickerDialog
import com.my.todo.util.generateDates
import com.my.todo.util.getTimeFormat
import com.my.todo.util.showToast
import com.my.todo.util.verticalGradientBrush
import com.my.todo.view.component.CustomAlertDialog
import com.my.todo.view.component.CustomOutlinedTextField
import com.my.todo.view.screens.home.ButtonItem
import java.time.LocalDate
import java.util.Calendar


var taskAdded: Int = 0

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddTaskScreen(
    navController: NavController, addTaskViewModel: AddTaskViewModel?, task: Task?
) {

    LaunchedEffect(Unit) {
        if (task != null) {
            addTaskViewModel?.updateTaskDetails(task)
        }
    }
    HandleUIEvents(addTaskViewModel, popup = {
        navController.popBackStack()
    })
    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(color = Color.Black)
                .padding(horizontal = 20.dp)
                .padding(top = 30.dp),
        ) {
            HeaderTitle(onBackClick = {
                navController.popBackStack()
            })
            Spacer(modifier = Modifier.height(30.dp))
            LazyColumn(modifier = Modifier.weight(1f)) {
                item {
                    DatePickerView(addTaskViewModel)
                    Spacer(modifier = Modifier.height(10.dp))

                    ScheduleSwitchView(addTaskViewModel)
                    TaskInputFields(addTaskViewModel)
                    addTaskViewModel?.let { it1 -> ScheduledTimeView(it1) }
                    Spacer(modifier = Modifier.height(20.dp))
                    PriorityView(task?.priority, selectedPriority = {
                        addTaskViewModel?.priority = it
                    })

                }
            }
            AddTaskButtons(isUpdate = task != null,
                onAddClick = {
                    taskAdded = 0
                    addTaskViewModel?.onEvent(AddTaskUiEvent.AddTaskClicked)
                }, onCancelClick = {
                    if (task != null)
                        addTaskViewModel?.onEvent(AddTaskUiEvent.CancelTaskClicked(task))
                    taskAdded = 2
                }, onUpdateClick = {
                    if (task != null)
                        addTaskViewModel?.onEvent(AddTaskUiEvent.UpdateTaskClicked(task))
                    taskAdded = 1
                })
            ShowCustomePopUp(addTaskViewModel)
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HandleUIEvents(addTaskViewModel: AddTaskViewModel?, popup: () -> Unit) {
    val context = LocalContext.current
    val uiEvent = addTaskViewModel?.uiEvent?.value
    // Separate the side-effects from the UI rendering
    LaunchedEffect(key1 = uiEvent) {
        uiEvent?.let {
            when (uiEvent) {
                AddTaskUiEffect.NavigateBack -> {
                    popup()
                }

                is AddTaskUiEffect.ShowToast -> {
                    context.showToast(uiEvent.message)
                }
            }
            addTaskViewModel.clearEvent()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ShowCustomePopUp(addTaskViewModel: AddTaskViewModel?) {
    if (addTaskViewModel?.showDialog?.value == true) {
        val msg = when (taskAdded) {
            0 -> "You have successfully created a task"
            1 -> "You have successfully updated a task"
            2 -> "You have successfully cancelled a task"
            else -> ""
        }
        CustomAlertDialog(msg, onDismiss = {
            addTaskViewModel.onEvent(AddTaskUiEvent.PopDismissed)
        })
    }
}

@Composable
fun AddTaskButtons(
    isUpdate: Boolean = false,
    onCancelClick: () -> Unit,
    onUpdateClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        horizontalArrangement = if (isUpdate) Arrangement.SpaceBetween else Arrangement.Center
    ) {
        if (isUpdate) {
            Button(
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                onClick = { onCancelClick() },
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f)
                    .background(brush = verticalGradientBrush(), shape = RoundedCornerShape(30.dp))
            ) {
                Text(text = "Cancel", color = Color.Black, fontSize = 15.sp)
            }
        }

        Button(
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            onClick = {
                if (isUpdate) onUpdateClick() else onAddClick()

            },
            modifier = Modifier
                .padding(5.dp)
                .weight(1f)
                .background(brush = verticalGradientBrush(), shape = RoundedCornerShape(30.dp))
        ) {
            Text(
                text = if (isUpdate) "Update Task" else "Add Task",
                color = Color.Black,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun PriorityView(Priority: String?, selectedPriority: (String) -> Unit) {
    var buttonItems by remember {
        mutableStateOf(
            listOf(
                ButtonItem(
                    "High", null, strokeColor = Color.Red,
                ),
                ButtonItem("Medium", null, strokeColor = Color.Magenta),
                ButtonItem("Low", null, strokeColor = Color.Green)
            )
        )
    }
    LaunchedEffect(Unit) {
        if (!Priority.isNullOrEmpty()) {
            buttonItems.find {
                Priority.equals(it.text, true)
            }?.apply {
                isSelected = true
            }
        }
    }
    Text(
        text = "Priority", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        buttonItems.forEach { item ->
            RoundedPriorityButton(item, onButtonClicked = {
                buttonItems = buttonItems.map {
                    it.copy(isSelected = it == item)
                }
                selectedPriority(buttonItems.first { it.isSelected }.text)
            })
        }
    }
}

@Composable
private fun RoundedPriorityButton(item: ButtonItem, onButtonClicked: () -> Unit) {
    Button(
        onClick = {
            onButtonClicked()
        },
        colors = ButtonDefaults.buttonColors(containerColor = (if (item.isSelected) item.strokeColor else Color.Black)!!),
        border = BorderStroke(1.dp, item.strokeColor!!),
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .height(48.dp)
            .width(120.dp)
            .padding(5.dp)
    ) {
        Text(
            text = item.text, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(3.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduledTimeView(addTaskViewModel: AddTaskViewModel) {
    val currentCalendar = Calendar.getInstance()
    val timePickerState =
        rememberTimePickerState(
            initialHour = currentCalendar.get(Calendar.HOUR_OF_DAY),
            initialMinute = currentCalendar.get(Calendar.MINUTE) + 10
        )
    var showTimePicker by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TimeCard(label = "Start Time",
            time = getTimeFormat().format(addTaskViewModel.startTime.value),
            onClick = {
                showTimePicker = true
            })
        TimeCard(label = "End Time",
            time = getTimeFormat().format(addTaskViewModel.endTime.value),
            onClick = {
                showTimePicker = true
            })
    }
    if (showTimePicker) {
        TimePickerDialog(onDismissRequest = { /*TODO*/ }, confirmButton = {
            TextButton(onClick = {

                val selectedCalendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                    set(Calendar.MINUTE, timePickerState.minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                // Compare the selected time with the current time
                if (selectedCalendar.before(currentCalendar)) {
                    errorMessage = "You cannot select a time before the current time"
                } else {
                    // Update the ViewModel with the selected time
                    addTaskViewModel.startTime.value = selectedCalendar.time
                    showTimePicker = false
                    errorMessage = null
                }
            }) { Text("OK", color = Color.Black) }
        }, dismissButton = {
            TextButton(onClick = {
                errorMessage = null
                showTimePicker = false
            }) { Text("Cancel", color = Color.Black) }
        }) {
            TimePicker(state = timePickerState)
            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun TimeCard(label: String, time: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier.width(150.dp),

        ) {
        Text(
            text = label, color = Color.White, fontSize = 16.sp, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onClick() }
                .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .padding(10.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_access_time_24),
                contentDescription = null,
                tint = Color.Cyan,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = time, color = Color.White, fontSize = 16.sp, textAlign = TextAlign.Center
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun TaskInputFields(
    addTaskViewModel: AddTaskViewModel?
) {
    val nameState = addTaskViewModel?.nameUiState?.value
    val descriptionState = addTaskViewModel?.descriptionUiState?.value
    CustomOutlinedTextField(
        value = nameState?.inputValue.toString(),
        label = "Name",
        onValueChange = {
            addTaskViewModel?.onEvent(AddTaskUiEvent.NameValueChanged(it))
        },
        borderColor = Color.White,
        textStyle = TextStyle(color = Color.White, fontSize = 15.sp),
        modifier = Modifier
            .padding(top = 5.dp)
            .fillMaxWidth()
            .height(60.dp),
        isError = nameState?.showError == true,
        errorMsg = if (nameState?.showError == true) "Please Enter Task Name" else ""
    )

    CustomOutlinedTextField(
        value = descriptionState?.inputValue.toString(),
        label = "Description",
        onValueChange = {
            addTaskViewModel?.onEvent(AddTaskUiEvent.DescriptionValueChanged(it))
        },
        borderColor = Color.White,
        textStyle = TextStyle(color = Color.White, fontSize = 15.sp),
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
            .height(80.dp),
        isError = descriptionState?.showError == true,
        errorMsg = if (descriptionState?.showError == true) "Please Enter Description Name" else ""
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ScheduleSwitchView(addTaskViewModel: AddTaskViewModel?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Schedule",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Switch(
            checked = addTaskViewModel?.isChecked?.value == true,
            onCheckedChange = { addTaskViewModel?.onEvent(AddTaskUiEvent.ScheduleClicked) },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                uncheckedThumbColor = Color.Black,
                checkedTrackColor = Purple80,
                uncheckedTrackColor = Color.White
            )
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DatePickerView(
    addTaskViewModel: AddTaskViewModel?
) {
    val currentDate = addTaskViewModel?.currentDateState?.value
    val selectedItem = addTaskViewModel?.selectedDate?.value
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "${
                selectedItem?.month?.getDisplayName(
                    java.time.format.TextStyle.SHORT, java.util.Locale.getDefault()
                )
            }  ${selectedItem?.dayOfMonth}",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = "left Icon",
                tint = Color.White,
                modifier = Modifier
                    .clickable {
                        addTaskViewModel?.onEvent(AddTaskUiEvent.ChangeMonth(-1))
                    }
                    .background(color = Purple80, shape = CircleShape)
                    .size(30.dp))
            Text(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(30.dp))
                    .padding(horizontal = 20.dp, vertical = 5.dp),
                text = currentDate?.month?.getDisplayName(
                    java.time.format.TextStyle.SHORT, java.util.Locale.getDefault()
                ).toString(),
                color = Color.Black,
                fontSize = 18.sp
            )
            Icon(imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "right Icon",
                tint = Color.White,
                modifier = Modifier
                    .clickable {
                        addTaskViewModel?.onEvent(AddTaskUiEvent.ChangeMonth(1))
                    }
                    .background(color = Purple80, shape = CircleShape)
                    .size(30.dp))
        }

    }
    Spacer(modifier = Modifier.height(20.dp))
    CustomDateList(generateDates(currentDate), selectedItem, onDateSelected = {
        addTaskViewModel?.onEvent(AddTaskUiEvent.DateSelected(it))
    })

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun CustomDateList(
    dateList: List<LocalDate>?, selectedItem: LocalDate?, onDateSelected: (LocalDate) -> Unit
) {
    if (dateList.isNullOrEmpty()) {
        // Handle empty or null list appropriately
        Text("No dates available")
        return
    }
    val listState = rememberLazyListState()
    LazyRow(
        state = listState, modifier = Modifier.fillMaxWidth()
    ) {
        items(dateList.size) { index ->
            val date = dateList[index]
            val isSelected = selectedItem == date
            val backgroundModifier = if (isSelected) {
                Modifier.background(
                    brush = verticalGradientBrush(), shape = RoundedCornerShape(15.dp)
                )
            } else {
                Modifier
            }
            val textColor = if (isSelected) Color.Black else Color.White
            DateItem(date, modifier = backgroundModifier, textColor, onItemClick = {
                onDateSelected(date)
            })
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DateItem(
    date: LocalDate, modifier: Modifier, textColor: Color, onItemClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .size(width = 60.dp, height = 70.dp)
            .padding(vertical = 10.dp)

            .clickable { onItemClick() }) {
        Text(
            text = date.dayOfWeek.getDisplayName(
                java.time.format.TextStyle.SHORT, java.util.Locale.getDefault()
            ), color = textColor, fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "${date.dayOfMonth}", color = textColor, fontSize = 14.sp
        )
    }
}

@Composable
private fun HeaderTitle(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(30.dp)

    ) {
        IconButton(
            onClick = {
                onBackClick()
            },
            modifier = Modifier
                .size(35.dp)
                .border(width = 0.5.dp, color = Color.White, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Up Arrow",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = "Create New Task",
            color = Color.White, fontSize = 23.sp,
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun AddTaskScreenPreview() {
    ToDoTheme {
        AddTaskScreen(
            navController = rememberNavController(), addTaskViewModel = null, task = null
        )
    }

}