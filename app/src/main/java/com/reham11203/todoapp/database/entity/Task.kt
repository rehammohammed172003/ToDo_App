package com.reham11203.todoapp.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo
    var title: String? = null,

    @ColumnInfo
    var description: String? = null,

    @ColumnInfo
    var isCompleted: Boolean? = false,

    @ColumnInfo
    var date: Long? = null,

    @ColumnInfo
    var time: Long? = null,


    ) {


}