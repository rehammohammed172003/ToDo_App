package com.reham11203.todoapp.ui.util

import java.util.Calendar

fun Calendar.clearTime() {
    set(Calendar.HOUR, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}
