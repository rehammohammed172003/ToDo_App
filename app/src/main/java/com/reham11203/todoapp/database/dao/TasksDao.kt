package com.reham11203.todoapp.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.reham11203.todoapp.database.entity.Task

@Dao
interface TasksDao {

    @Insert
    fun addNewTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Query("SELECT * FROM Task")
    fun getAllTasks(): List<Task>

    @Query("SELECT * FROM Task WHERE date = :date")
    fun getAllTasksByDate(date: Long): List<Task>

    @Query("SELECT * FROM Task WHERE id = :id")
    fun getTaskById(id: Int): Task?

}