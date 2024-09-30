package com.my.todo.util

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun NotificationPermissionCheck(
    onPermissionResult: (Boolean) -> Unit
) {
    Log.i("NotificationPermissionCheck", "inside")
    val context = LocalContext.current

    // Permission launcher for API 33+
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            onPermissionResult(isGranted)
        }
    )

    LaunchedEffect(Unit) {
        // Check if permission is required for this Android version (API 33+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Check if permission is already granted
            val isPermissionGranted = ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!isPermissionGranted) {
                // Request the notification permission
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                // Notify that permission is already granted
                onPermissionResult(true)
            }
        } else {
            // No need to request permission for API levels below 33
            onPermissionResult(true)
        }
    }
}
