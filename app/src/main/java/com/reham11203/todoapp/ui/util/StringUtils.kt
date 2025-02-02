package com.reham11203.todoapp.ui.util

fun getFormattedTime(hour: Int, minute: Int): String {
    val minutesString = if (minute == 0) "00" else minute.toString()
    return "${getHoursIn12(hour)}:$minutesString ${getAmPm(hour)}"

}

fun getHoursIn12(hour: Int): Int {
    return if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
}

fun getAmPm(hour: Int): String {
    return if (hour < 12) "am" else "pm"
}