package com.reham11203.todoapp.ui.util

import androidx.appcompat.app.AppCompatDelegate

fun applyModeChange(isDark: Boolean) {
    if (isDark)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    else
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
}