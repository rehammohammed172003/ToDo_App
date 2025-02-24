package com.reham11203.todoapp

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import com.reham11203.todoapp.database.TasksDatabase
import com.reham11203.todoapp.ui.util.Constants
import com.reham11203.todoapp.ui.util.applyModeChange

class MyApplication : Application() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        TasksDatabase.init(this)
        setNightMode()

    }

    private fun setNightMode() {
        sharedPreferences = getSharedPreferences(Constants.MODE_PREF, MODE_PRIVATE)
        val isDark = sharedPreferences.getBoolean(Constants.MODE_KEY, getDeviceModeState())
        applyModeChange(isDark)
    }

    private fun getDeviceModeState(): Boolean {
        val currentDeviceMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentDeviceMode == Configuration.UI_MODE_NIGHT_YES
    }

}