package com.reham11203.todoapp.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Task(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String,
    var description: String? = null,
    var isCompleted: Boolean = false,
    var date: Long,
    var time: Long
) : Parcelable