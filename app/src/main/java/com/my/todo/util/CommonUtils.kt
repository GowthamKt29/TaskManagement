package com.my.todo.util

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.my.todo.ui.theme.Purple80
import com.my.todo.ui.theme.blue50
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.random.Random


fun verticalGradientBrush(): Brush {
    return Brush.verticalGradient(listOf(Purple80, blue50, blue50, Purple80))
}

fun generateRandomColor(): Color {
    val colors = listOf(
        Color.Red,
        Color.Yellow,
        Color.Blue,
        Color(0xFFFFC0CB), // Pink
        Color(0xFFEE82EE), // Violet
        Color(0xFF90EE90)  // Light Green
    )
    return colors[Random.nextInt(colors.size)]
}

fun getTimeFormat(): SimpleDateFormat {
    return SimpleDateFormat("hh:mm a", Locale.getDefault())
}

fun getDateTimeFormat(): SimpleDateFormat {
    return SimpleDateFormat("yyyy-MM-dd h:mm a", Locale.getDefault())
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

@RequiresApi(Build.VERSION_CODES.O)
fun generateDates(currentDate: LocalDate?): List<LocalDate> {
    val currentMonth = currentDate?.month
    val currentYear = currentDate?.year
    val now = LocalDate.now()

    val startDate: LocalDate
    val endDate: LocalDate

    if (now.month == currentMonth && now.year == currentYear) {
        startDate = currentDate
        endDate = YearMonth.of(currentYear, currentMonth).atEndOfMonth()
    } else {
        startDate = LocalDate.of(currentYear!!, currentMonth, 1)
        endDate = startDate.plusDays(30)
    }

    return generateSequence(startDate) { date ->
        if (date.isBefore(endDate)) date.plusDays(1) else null
    }.toList()
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateTimeDifference(startTime: String, date: String): Duration {
    // Define the date and time format
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    // Parse the start date and time
    val startDate = LocalDate.parse(date, dateFormatter)
    val startTime = LocalTime.parse(startTime, timeFormatter)
    val startDateTime = LocalDateTime.of(startDate, startTime)

    // Get the current date and time
    val now = LocalDateTime.now()

    // Calculate the duration between the start time and now
    val duration = Duration.between(now, startDateTime)

    // Get the difference in hours
    return duration
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateDelay(date: String, time: String): Long {
    // Define the format for the date and time
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    // Parse the date and time
    val localDate = LocalDate.parse(date, dateFormatter)
    val localTime = LocalTime.parse(time, timeFormatter)

    // Combine date and time into LocalDateTime
    val localDateTime = LocalDateTime.of(localDate, localTime)

    // Specify the Indian Standard Time Zone (IST)
    val zoneId = ZoneId.of("Asia/Kolkata")

    // Convert LocalDateTime to ZonedDateTime in IST
    val zonedDateTime = ZonedDateTime.of(localDateTime, zoneId)

    // Convert ZonedDateTime to milliseconds since epoch
    val targetTimeMillis = zonedDateTime.toInstant().toEpochMilli()

    // Get the current time in milliseconds (UTC)
    val currentTimeMillis = System.currentTimeMillis()

    // Calculate delay in milliseconds
    val delayInMillis = targetTimeMillis - currentTimeMillis

    // Convert milliseconds to seconds
    return TimeUnit.MILLISECONDS.toSeconds(delayInMillis)
}

