package com.reham11203.todoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.reham11203.todoapp.database.dao.TasksDao
import com.reham11203.todoapp.database.entity.Task

@Database(entities = [Task::class], version = 1, exportSchema = true)
abstract class TasksDatabase : RoomDatabase() {

    abstract fun tasksDao(): TasksDao

    companion object {
        private const val DATABASE_NAME = "todo_tasks"
        private var tasksDatabase: TasksDatabase? = null

        fun init(applicatonContext: Context) {
            if (tasksDatabase == null) {
                tasksDatabase = Room.databaseBuilder(
                    applicatonContext,
                    TasksDatabase::class.java,
                    DATABASE_NAME

                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }

        fun getInstance(): TasksDatabase {

            return tasksDatabase!!
        }

    }

}