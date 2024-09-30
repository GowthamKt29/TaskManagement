package com.my.todo.view.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.my.todo.ui.theme.Purple80
import com.my.todo.ui.theme.ToDoTheme
import com.my.todo.util.NotificationPermissionCheck
import com.my.todo.view.navigation.RootScreen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    Log.i("SplashScreen--", "Called")
    var permissionChecked by remember { mutableStateOf(false) }
    var permissionGranted by remember { mutableStateOf(false) }

    // Call the separate permission check composable
    NotificationPermissionCheck(
        onPermissionResult = { isGranted ->
            permissionGranted = isGranted
            permissionChecked = true
        }
    )
    LaunchedEffect(permissionChecked) {
        if (permissionChecked) {
            delay(1000)
            navController.navigate(route = RootScreen.Home.route) {
                popUpTo(RootScreen.SPLASH.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Purple80),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Task manager", color = Color.Black, fontSize = 30.sp)
    }

}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    ToDoTheme {
        SplashScreen(navController = rememberNavController())
    }
}
