package com.reham11203.todoapp.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var title: String,
    var description: String? = null,
    var isCompleted: Boolean = false,
    var date: Long,
    var time: Long
)