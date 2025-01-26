package com.reham11203.todoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.reham11203.todoapp.database.dao.TasksDao
import com.reham11203.todoapp.database.entity.Task

@Database(entities = [Task::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tasksDao(): TasksDao

    companion object {
        private const val DATABASE_NAME = "todo_tasks"
        private var appDatabase: AppDatabase? = null

        fun init(applicatonContext: Context) {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(
                    applicatonContext,
                    AppDatabase::class.java,
                    DATABASE_NAME

                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }

        fun getInstance(): AppDatabase {

            return appDatabase!!
        }

    }

}