package com.reham11203.todoapp.ui.util

import android.app.DatePickerDialog
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.reham11203.todoapp.R
import java.util.Calendar

fun showDatePickerDialog(context: Context, callback: (String, Calendar) -> Unit) {

    val dateDialog = DatePickerDialog(context)

    dateDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        calendar.clearTime()
        callback("$dayOfMonth/${month + 1}/$year", calendar)

    }
    dateDialog.show()
}

fun showTimePickerDialog(
    hour: Int,
    minute: Int,
    title: String,
    fragmentManager: FragmentManager,
    callback: (Int, Int) -> Unit
) {
    val timeDialog = MaterialTimePicker.Builder()
        .setTimeFormat(TimeFormat.CLOCK_12H)
        .setHour(hour)
        .setMinute(minute)
        .setTitleText(title)
        .setTheme(R.style.customTimePickerTheme)
        .build()
    timeDialog.show(fragmentManager, "")
    timeDialog.addOnPositiveButtonClickListener {
        callback(timeDialog.hour, timeDialog.minute)
    }
    timeDialog.addOnNegativeButtonClickListener {
        timeDialog.dismiss()
    }
}