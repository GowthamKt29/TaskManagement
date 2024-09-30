package com.my.todo


//package com.my.todo.view.screens.addtask
//
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.KeyboardArrowLeft
//import androidx.compose.material.icons.filled.KeyboardArrowRight
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Switch
//import androidx.compose.material3.SwitchDefaults
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.material3.TimePicker
//import androidx.compose.material3.rememberTimePickerState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
//import com.my.todo.R
//import com.my.todo.ui.theme.Purple80
//import com.my.todo.ui.theme.ToDoTheme
//import com.my.todo.util.TimePickerDialog
//import com.my.todo.util.generateDates
//import com.my.todo.util.getTimeFormat
//import com.my.todo.util.showToast
//import com.my.todo.util.verticalGradientBrush
//import com.my.todo.view.component.CustomAlertDialog
//import com.my.todo.view.component.CustomOutlinedTextField
//import com.my.todo.view.screens.home.ButtonItem
//import java.time.LocalDate
//import java.util.Calendar
//import java.util.Date
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun AddTaskScreen(
//    navController: NavController,
//    uiEvent: AddTaskUiEffect?,
//    currentDate: LocalDate?,
//    selectedDate: LocalDate?,
//    isChecked: Boolean,
//    nameState: TextInputUIState,
//    descriptionState: TextInputUIState,
//    startTime: Date,
//    endTime: Date,
//    showDialog: Boolean?,
//    onEvent: (AddTaskUiEvent) -> Unit,
//    clearEvent: () -> Unit,
//    selectPriority: (String) -> Unit
//) {
//    HandleUIEvents(uiEvent, popup = {
//        navController.popBackStack()
//    }, clearEvent = {
//        clearEvent()
//    })
//    Scaffold {
//        Column(
//            modifier = Modifier
//                .padding(it)
//                .fillMaxSize()
//                .background(color = Color.Black)
//                .padding(horizontal = 20.dp)
//                .padding(top = 20.dp),
//        ) {
//            HeaderTitle(onBackClick = {
//                navController.popBackStack()
//            })
//            Spacer(modifier = Modifier.height(20.dp))
//            LazyColumn(modifier = Modifier.weight(1f)) {
//                item {
//                    DatePickerView(currentDate, selectedDate, onEvent)
//                    Spacer(modifier = Modifier.height(10.dp))
//                    ScheduleSwitchView(isChecked, onEvent)
//                    TaskInputFields(nameState, descriptionState, onEvent)
//                    if (isChecked) {
//                        ScheduledTimeView(startTime, endTime)
//                    }
//                    Spacer(modifier = Modifier.height(20.dp))
//                    PriorityView(selectedPriority = {
//                        selectPriority(it)
//                    })
//
//                }
//            }
//            AddTaskBtn(onClick = {
//                onEvent(AddTaskUiEvent.AddTaskClicked)
//            })
//            ShowCustomePopUp(showDialog, onEvent)
//        }
//    }
//
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun HandleUIEvents(uiEvent: AddTaskUiEffect?, popup: () -> Unit, clearEvent: () -> Unit) {
//    val context = LocalContext.current
//    // Separate the side-effects from the UI rendering
//    LaunchedEffect(key1 = uiEvent) {
//        uiEvent?.let {
//            when (uiEvent) {
//                AddTaskUiEffect.NavigateBack -> {
//                    popup()
//                }
//
//                is AddTaskUiEffect.ShowToast -> {
//                    context.showToast(uiEvent.message)
//                }
//            }
//            clearEvent()
//        }
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//private fun ShowCustomePopUp(showDialog: Boolean?, onEvent: (AddTaskUiEvent) -> Unit) {
//    if (showDialog == true) {
//        CustomAlertDialog(onDismiss = {
//            onEvent(AddTaskUiEvent.PopDismissed)
//        })
//    }
//}
//
//@Composable
//fun AddTaskBtn(onClick: () -> Unit) {
//    Button(
//        colors = ButtonDefaults.buttonColors(Color.Transparent),
//        onClick = {
//            onClick()
//        },
//        modifier = Modifier
//            .padding(bottom = 20.dp)
//            .fillMaxWidth()
//            .background(brush = verticalGradientBrush(), shape = RoundedCornerShape(30.dp))
//    ) {
//        Text(text = "Add TasK", color = Color.Black, fontSize = 15.sp)
//    }
//}
//
//@Composable
//fun PriorityView(selectedPriority: (String) -> Unit) {
//    var buttonItems by remember {
//        mutableStateOf(
//            listOf(
//                ButtonItem(
//                    "High", null, strokeColor = Color.Red,
//                ),
//                ButtonItem("Medium", null, strokeColor = Color.Magenta),
//                ButtonItem("Low", null, strokeColor = Color.Green)
//            )
//        )
//    }
//    Text(
//        text = "Priority",
//        color = Color.White,
//        fontSize = 17.sp,
//        fontWeight = FontWeight.ExtraBold
//    )
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 10.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        buttonItems.forEach { item ->
//            RoundedPriorityButton(item, onButtonClicked = {
//                buttonItems = buttonItems.map {
//                    it.copy(isSelected = it == item)
//                }
//                selectedPriority(buttonItems.first { it.isSelected }.text)
//            })
//        }
//    }
//}
//
//@Composable
//private fun RoundedPriorityButton(item: ButtonItem, onButtonClicked: () -> Unit) {
//    Button(
//        onClick = { onButtonClicked() },
//        colors = ButtonDefaults.buttonColors(containerColor = (if (item.isSelected) item.strokeColor else Color.Black)!!),
//        border = BorderStroke(1.dp, item.strokeColor!!),
//        shape = RoundedCornerShape(50),
//        modifier = Modifier
//            .height(48.dp)
//            .width(120.dp)
//            .padding(5.dp)
//    ) {
//        Text(
//            text = item.text,
//            color = Color.White,
//            fontSize = 12.sp,
//            fontWeight = FontWeight.Bold
//        )
//        Spacer(modifier = Modifier.width(3.dp))
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun ScheduledTimeView(startTime: Date, endTime: Date) {
//    val timePickerState = rememberTimePickerState()
//    var showTimePicker by remember { mutableStateOf(false) }
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 20.dp),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        TimeCard(
//            label = "Start Time",
//            time = getTimeFormat().format(startTime), onClick = {
//                showTimePicker = true
//            }
//        )
//        TimeCard(
//            label = "End Time",
//            time = getTimeFormat().format(endTime),
//            onClick = {
//                showTimePicker = true
//            })
//    }
//    if (showTimePicker) {
//        TimePickerDialog(
//            onDismissRequest = { /*TODO*/ },
//            confirmButton = {
//                TextButton(
//                    onClick = {
//                        showTimePicker = false
//                    }
//                ) { Text("OK", color = Color.Black) }
//            },
//            dismissButton = {
//                TextButton(
//                    onClick = {
//                        showTimePicker = false
//                    }
//                ) { Text("Cancel", color = Color.Black) }
//            }
//        )
//        {
//            TimePicker(state = timePickerState)
//        }
//    }
//}
//
//@Composable
//fun TimeCard(label: String, time: String, onClick: () -> Unit) {
//    Column(
//        modifier = Modifier
//            .width(150.dp),
//
//        ) {
//        Text(
//            text = label,
//            color = Color.White,
//            fontSize = 16.sp,
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier
//                .clickable { onClick() }
//                .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
//                .fillMaxWidth()
//                .padding(10.dp)
//        ) {
//            Icon(
//                painter = painterResource(id = R.drawable.baseline_access_time_24),
//                contentDescription = null,
//                tint = Color.Cyan,
//                modifier = Modifier.size(24.dp)
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(
//                text = time,
//                color = Color.White,
//                fontSize = 16.sp,
//                textAlign = TextAlign.Center
//            )
//        }
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//private fun TaskInputFields(
//    nameState: TextInputUIState,
//    descriptionState: TextInputUIState,
//    onEvent: (AddTaskUiEvent) -> Unit
//) {
//    CustomOutlinedTextField(
//        value = nameState.inputValue,
//        label = "Name",
//        onValueChange = {
//            onEvent(AddTaskUiEvent.NameValueChanged(it))
//        },
//        borderColor = Color.White,
//        textStyle = TextStyle(color = Color.White, fontSize = 15.sp),
//        modifier = Modifier
//            .padding(top = 5.dp)
//            .fillMaxWidth()
//            .height(60.dp),
//        isError = nameState.showError,
//        errorMsg = if (nameState.showError) "Please Enter Task Name" else ""
//    )
//
//    CustomOutlinedTextField(
//        value = descriptionState.inputValue,
//        label = "Description",
//        onValueChange = {
//            onEvent(AddTaskUiEvent.DescriptionValueChanged(it))
//        },
//        borderColor = Color.White,
//        textStyle = TextStyle(color = Color.White, fontSize = 15.sp),
//        modifier = Modifier
//            .padding(top = 20.dp)
//            .fillMaxWidth()
//            .height(80.dp),
//        isError = descriptionState.showError,
//        errorMsg = if (descriptionState.showError) "Please Enter Description Name" else ""
//    )
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//private fun ScheduleSwitchView(isChecked: Boolean, onEvent: (AddTaskUiEvent) -> Unit) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Text(
//            text = "Schedule",
//            color = Color.White,
//            fontSize = 22.sp,
//            fontWeight = FontWeight.ExtraBold
//        )
//        Switch(
//            checked = isChecked,
//            onCheckedChange = { onEvent(AddTaskUiEvent.ScheduleClicked) },
//            colors = SwitchDefaults.colors(
//                checkedThumbColor = Color.Black,
//                uncheckedThumbColor = Color.Black,
//                checkedTrackColor = Purple80,
//                uncheckedTrackColor = Color.White
//            )
//        )
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//private fun DatePickerView(
//    currentDate: LocalDate?,
//    selectedItem: LocalDate?,
//    onEvent: (AddTaskUiEvent) -> Unit
//) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Text(
//            text = "${
//                selectedItem?.month?.getDisplayName(
//                    java.time.format.TextStyle.SHORT,
//                    java.util.Locale.getDefault()
//                )
//            }  ${selectedItem?.dayOfMonth}",
//            color = Color.White,
//            fontSize = 22.sp,
//            fontWeight = FontWeight.ExtraBold
//        )
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Icon(
//                imageVector = Icons.Filled.KeyboardArrowLeft,
//                contentDescription = "left Icon",
//                tint = Color.White,
//                modifier = Modifier
//                    .clickable {
//                        onEvent(AddTaskUiEvent.ChangeMonth(-1))
//                    }
//                    .background(color = Purple80, shape = CircleShape)
//                    .size(30.dp)
//            )
//            Text(
//                modifier = Modifier
//                    .background(Color.White, shape = RoundedCornerShape(30.dp))
//                    .padding(horizontal = 20.dp, vertical = 5.dp),
//                text = currentDate?.month?.getDisplayName(
//                    java.time.format.TextStyle.SHORT,
//                    java.util.Locale.getDefault()
//                ).toString(),
//                color = Color.Black,
//                fontSize = 18.sp
//            )
//            Icon(
//                imageVector = Icons.Filled.KeyboardArrowRight,
//                contentDescription = "right Icon",
//                tint = Color.White,
//                modifier = Modifier
//                    .clickable {
//                        onEvent(AddTaskUiEvent.ChangeMonth(1))
//                    }
//                    .background(color = Purple80, shape = CircleShape)
//                    .size(30.dp)
//            )
//        }
//
//    }
//    Spacer(modifier = Modifier.height(20.dp))
//    CustomDateList(
//        generateDates(currentDate),
//        selectedItem,
//        onDateSelected = {
//            onEvent(AddTaskUiEvent.DateSelected(it))
//        })
//
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//private fun CustomDateList(
//    dateList: List<LocalDate>, selectedItem: LocalDate?,
//    onDateSelected: (LocalDate) -> Unit
//) {
//    val listState = rememberLazyListState()
//    LazyRow(
//        state = listState,
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        items(dateList.size) { index ->
//            val date = dateList[index]
//            val isSelected = selectedItem == date
//            val backgroundModifier = if (isSelected) {
//                Modifier.background(
//                    brush = verticalGradientBrush(),
//                    shape = RoundedCornerShape(15.dp)
//                )
//            } else {
//                Modifier
//            }
//            val textColor = if (isSelected) Color.Black else Color.White
//            DateItem(date, modifier = backgroundModifier, textColor, onItemClick = {
//                onDateSelected(date)
//            })
//        }
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//private fun DateItem(
//    date: LocalDate,
//    modifier: Modifier,
//    textColor: Color,
//    onItemClick: () -> Unit
//) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = modifier
//            .size(width = 60.dp, height = 70.dp)
//            .padding(vertical = 10.dp)
//
//            .clickable { onItemClick() }
//    ) {
//        Text(
//            text = date.dayOfWeek.getDisplayName(
//                java.time.format.TextStyle.SHORT,
//                java.util.Locale.getDefault()
//            ),
//            color = textColor, fontSize = 14.sp
//        )
//        Spacer(modifier = Modifier.height(5.dp))
//        Text(
//            text = "${date.dayOfMonth}",
//            color = textColor,
//            fontSize = 14.sp
//        )
//    }
//}
//
//@Composable
//private fun HeaderTitle(onBackClick: () -> Unit) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.spacedBy(30.dp)
//
//    ) {
//        IconButton(
//            onClick = {
//                onBackClick()
//            },
//            modifier = Modifier
//                .size(35.dp)
//                .border(width = 0.5.dp, color = Color.White, shape = CircleShape)
//        ) {
//            Icon(
//                imageVector = Icons.Filled.ArrowBack,
//                contentDescription = "Up Arrow",
//                tint = Color.White,
//                modifier = Modifier
//                    .size(20.dp)
//            )
//        }
//        Text(
//            text = "Create New Task",
//            color = Color.White, fontSize = 23.sp,
//        )
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun AddTaskScreenPreview() {
//    ToDoTheme {
//        AddTaskScreen(
//            navController = rememberNavController(),
//            uiEvent = null,
//            currentDate = LocalDate.now(),
//            selectedDate = LocalDate.now(),
//            isChecked = false,
//            nameState = TextInputUIState(),
//            descriptionState = TextInputUIState(),
//            startTime = Calendar.getInstance().time,
//            endTime = Calendar.getInstance().time,
//            showDialog = null,
//            onEvent = {},
//            clearEvent = {},
//            selectPriority = {}
//        )
//    }
//
//}
