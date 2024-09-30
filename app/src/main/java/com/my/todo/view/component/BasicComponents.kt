package com.my.todo.view.component


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.my.todo.ui.theme.black30


@Composable
fun CustomOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit,
    label: String = "",
    placeholder: String = "",
    isError: Boolean = false,
    errorMsg: String = "",
    borderColor: Color = black30,
    textStyle: TextStyle = MaterialTheme.typography.displayMedium,
    labelStyle: TextStyle = MaterialTheme.typography.labelMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val borderColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = borderColor,
        unfocusedBorderColor = borderColor
    )
    Column {
        OutlinedTextField(
            singleLine = true,
            keyboardOptions = keyboardOptions,
            enabled = enabled,
            value = value,
            onValueChange = onValueChange,
            isError = isError,
            modifier = modifier,
            colors = borderColors,
            textStyle = textStyle, maxLines = 3,
            trailingIcon = {
                if (isError) {
                    Icon(Icons.Filled.Info, "error", tint = MaterialTheme.colorScheme.error)
                }

            },
            label = {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    color = borderColor,
                    style = labelStyle
                )
            },
            placeholder = {
                Text(
                    text = placeholder, fontSize = 16.sp
                )
            }
        )
        if (isError) {
            Text(
                text = errorMsg,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 5.dp, top = 10.dp),
                color = Color.Red,
                style = textStyle
            )
        }
    }

}

@Composable
fun StateMessageView(
    message: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.White,
    fontSize: TextUnit = 24.sp
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = textColor,
            textAlign = TextAlign.Center,
            fontSize = fontSize
        )
    }
}