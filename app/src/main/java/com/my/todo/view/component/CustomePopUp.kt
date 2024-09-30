package com.my.todo.view.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties

@Composable
fun CustomAlertDialog(msg: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text(text = "Success")
        },
        text = {
            Text(msg)
        },
        confirmButton = {
            Button(colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                onClick = {
                    onDismiss()
                }) {
                Text("Ok")
            }
        },
        properties = DialogProperties()
    )

}
