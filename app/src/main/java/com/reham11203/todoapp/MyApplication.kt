package com.reham11203.todoapp

import android.app.Application
import com.reham11203.todoapp.database.TasksDatabase

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TasksDatabase.init(this)
    }

}