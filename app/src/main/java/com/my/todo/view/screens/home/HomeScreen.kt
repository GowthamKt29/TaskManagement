package com.my.todo.view.screens.home

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.gson.Gson
import com.my.todo.model.Task
import com.my.todo.ui.theme.Purple80
import com.my.todo.ui.theme.ToDoTheme
import com.my.todo.util.DataState
import com.my.todo.util.calculateTimeDifference
import com.my.todo.util.verticalGradientBrush
import com.my.todo.view.component.StateMessageView
import com.my.todo.view.navigation.RootScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel?) {
    homeViewModel?.getTasks("OnGoing")
    var taskSize by remember {
        mutableIntStateOf(0)
    }
    var status by remember {
        mutableStateOf("OnGoing")
    }
    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = {
                navController.navigate(RootScreen.AddTask.route)
            },
            shape = RoundedCornerShape(30.dp),
            containerColor = Color.White, elevation = FloatingActionButtonDefaults.elevation(10.dp)
        ) {
            Icon(Icons.Filled.Add, tint = Color.Black, contentDescription = "Add")
        }
    }, floatingActionButtonPosition = FabPosition.Center) { innerPading ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPading)
                .background(color = Color.Black)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = verticalGradientBrush(),
                        shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
                    )
                    .padding(10.dp)
                    .padding(top = 30.dp)
            ) {
                ProfileDetails(taskSize)
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                ButtonRow(homeViewModel, selectedStatus = {
                    status = it
                })
                homeViewModel?.let {
                    TaskList(it, status, taskCount = {
                        taskSize = it
                    }, itemClicked = { task ->
                        val taskJson = Uri.encode(Gson().toJson(task))
                        navController.navigate(RootScreen.AddTask.route + "?task=$taskJson")
                    })
                }
            }
        }

    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskList(
    homeViewModel: HomeViewModel,
    status: String,
    taskCount: (Int) -> Unit,
    itemClicked: (Task) -> Unit
) {
    val itemsResult by homeViewModel.tasks.collectAsStateWithLifecycle()

    when (val result = itemsResult) {
        is DataState.Error -> {
            StateMessageView(message = "Something Went Wrong")
        }

        DataState.Idle -> {
            if ("OnGoing".equals(status, true)) {
                taskCount(0)
            }
            StateMessageView(message = "No Tasks")
        }

        is DataState.Success -> {
            if ("OnGoing".equals(status, true)) {
                taskCount(result.data.size)
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(result.data.size) {
                    TaskItem(result.data[it], itemClicked = {
                        itemClicked(result.data[it])
                    })
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskItem(task: Task, itemClicked: () -> Unit = {}) {
    val taskStartTime = calculateTimeDifference(task.startTime, task.date)
    val borderColor = when (task.priority) {
        "High" -> Color.Red
        "Medium" -> Color.Blue
        "Low" -> Color.Green
        else -> Color.DarkGray // Default color if priority is unknown
    }
    Card(
        shape = RoundedCornerShape(50.dp),
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 15.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Purple80, contentColor = Color.Black)
    ) {
        Column(modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                Text(
                    text = task.status, fontSize = 12.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .border(
                            BorderStroke(1.dp, Color.DarkGray),
                            shape = RoundedCornerShape(25.dp)
                        )
                        .padding(8.dp)
                )
                if (task.status.equals("OnGoing", true)) {
                    IconButton(
                        onClick = {
                            itemClicked()
                        },
                        modifier = Modifier
                            .background(Color.Black, shape = CircleShape)
                            .size(35.dp)
                            .rotate(140f)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Up Arrow",
                            tint = Color.White,
                            modifier = Modifier
                                .size(15.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = task.description,
                color = Color.Black,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "Created for : ${task.name}",
                color = Color.DarkGray,
                fontSize = 15.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = task.priority,
                    fontSize = 12.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .border(
                            BorderStroke(1.dp, borderColor),
                            shape = RoundedCornerShape(25.dp)
                        )
                        .padding(horizontal = 25.dp, vertical = 8.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                if (task.status.equals("OnGoing", true)) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = "${if (taskStartTime.toHours() < 1) "${taskStartTime.toMinutes()} Minutes" else "${taskStartTime.toHours()} hours"} ",
                        fontSize = 12.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .border(
                                BorderStroke(1.dp, Color.DarkGray),
                                shape = RoundedCornerShape(25.dp)
                            )
                            .padding(8.dp)
                    )
                }
            }
        }

    }

}

@Composable
fun ProfileDetails(taskSize: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),

        ) {
        Text(text = "Hello,", fontSize = 25.sp, color = Color.DarkGray)
        Text(
            text = "You Have $taskSize task in this Month",
            style = TextStyle(lineHeight = 40.sp),
            modifier = Modifier
                .padding(top = 15.dp, bottom = 20.dp),
            fontSize = 25.sp, color = Color.Black, fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ButtonRow(homeViewModel: HomeViewModel?, selectedStatus: (String) -> Unit) {
    var buttonItems by remember {
        mutableStateOf(
            listOf(
                ButtonItem(
                    "OnGoing",
                    {
                        Icon(
                            Icons.Default.Star,
                            modifier = Modifier.size(15.dp),
                            contentDescription = null
                        )
                    },
                    isSelected = true
                ),
                ButtonItem(
                    "Done",
                    {
                        Icon(
                            Icons.Default.Done,
                            modifier = Modifier.size(15.dp),
                            contentDescription = null
                        )
                    }),
                ButtonItem(
                    "Halted",
                    {
                        Icon(
                            Icons.Default.Clear,
                            modifier = Modifier.size(15.dp),
                            contentDescription = null
                        )
                    })
            )
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        buttonItems.forEach { item ->
            RoundedButton(item, onButtonClicked = { status ->
                selectedStatus(status)
                homeViewModel?.getTasks(status)
                buttonItems = buttonItems.map { it.copy(isSelected = it == item) }
            })
        }
    }
}

@Composable
private fun RoundedButton(item: ButtonItem, onButtonClicked: (String) -> Unit) {
    Log.i("RoundedButton--", item.isSelected.toString())
    Button(
        onClick = { onButtonClicked(item.text) },
        colors = ButtonDefaults.buttonColors(containerColor = if (item.isSelected) Purple80 else Color.Black),
        border = BorderStroke(1.dp, Purple80),
        shape = RoundedCornerShape(30),
        modifier = Modifier
            .height(48.dp)
            .padding(5.dp)
    ) {
        Text(
            text = item.text,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(3.dp))
        item.icon?.invoke()
    }
}

data class ButtonItem(
    val text: String,
    val icon: @Composable (() -> Unit)?,
    var isSelected: Boolean = false,
    val strokeColor: Color? = null
)

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    ToDoTheme {
        TaskItem(
            task = Task(
                id = 1,
                date = "2024-08-22",
                isScheduled = true,
                name = "hello",
                description = "test",
                startTime = "",
                endTime = "",
                priority = "High",
                status = "",
                notifyTime = ""
            )
        )
    }
}
