package com.my.todo.view.navigation

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.my.todo.model.Task
import com.my.todo.view.screens.SplashScreen
import com.my.todo.view.screens.addtask.AddTaskScreen
import com.my.todo.view.screens.addtask.AddTaskViewModel
import com.my.todo.view.screens.home.HomeScreen
import com.my.todo.view.screens.home.HomeViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RootNavigationGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = RootScreen.SPLASH.route
    ) {
        composable(route = RootScreen.SPLASH.route) {
            SplashScreen(navController = navController)
        }
        composable(route = RootScreen.Home.route) {
            val homeViewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(navController = navController, homeViewModel)
        }

        composable(
            route = RootScreen.AddTask.route + "?task={task}",
            arguments = listOf(navArgument("task") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val taskJson = backStackEntry.arguments?.getString("task")
            val task = taskJson?.let { Gson().fromJson(Uri.decode(it), Task::class.java) }
            val addTaskViewModel = hiltViewModel<AddTaskViewModel>()
            AddTaskScreen(
                navController = navController,
                addTaskViewModel, task
            )
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
}

sealed class RootScreen(val route: String) {
    object SPLASH : RootScreen(route = "SPLASH")
    object Home : RootScreen(route = "HOME")
    object AddTask : RootScreen(route = "ADDTASK")
}