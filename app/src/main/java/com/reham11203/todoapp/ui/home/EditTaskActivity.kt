package com.reham11203.todoapp.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.reham11203.todoapp.R
import com.reham11203.todoapp.database.TasksDatabase
import com.reham11203.todoapp.database.dao.TasksDao
import com.reham11203.todoapp.database.entity.Task
import com.reham11203.todoapp.databinding.ActivityEditTaskBinding
import com.reham11203.todoapp.ui.util.Constants
import com.reham11203.todoapp.ui.util.clearDate
import com.reham11203.todoapp.ui.util.clearSeconds
import com.reham11203.todoapp.ui.util.clearTime
import com.reham11203.todoapp.ui.util.getFormattedTime
import com.reham11203.todoapp.ui.util.showDatePickerDialog
import com.reham11203.todoapp.ui.util.showTimePickerDialog
import java.util.Calendar

class EditTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditTaskBinding
    private lateinit var intentTask: Task
    private lateinit var newTask: Task
    private var dateCalendar = Calendar.getInstance()
    private var timeCalendar = Calendar.getInstance()
    lateinit var dao: TasksDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intentTask =
            IntentCompat.getParcelableExtra(intent, Constants.TASK_KEY, Task::class.java) as Task
        newTask = intentTask.copy()
        dao = TasksDatabase.getInstance().tasksDao()

        setupToolbar()
        initViews()
        onSelectDateClick()
        onSelectTimeClick()
        onSaveBtnClick()
    }

    private fun onSaveBtnClick() {
        binding.saveBtn.setOnClickListener {
            if (!validateInput())
                return@setOnClickListener
            updateTaskAndFinish()
        }

    }

    private fun updateTaskAndFinish() {
        if (!validateInput()) {
            return
        }
        newTask.apply {
            title = binding.taskTitle.text.toString()
            description = binding.taskDescription.text.toString()
        }
        dao.updateTask(newTask)
        setResult(RESULT_OK)
        finish()
    }

    private fun validateInput(): Boolean {
        var isValid = true
        if (binding.taskTitle.text.isNullOrBlank()) {
            isValid = false
            binding.taskTitle.error = getString(R.string.required_field)
        } else {
            binding.taskTitle.error = null
        }
        return isValid


    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun initViews() {
        binding.taskTitle.setText(intentTask.title)
        binding.taskDescription.setText(intentTask.description)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = intentTask.date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        binding.selectDateTv.text = "$day/${month + 1}/$year"

        calendar.timeInMillis = intentTask.time
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        binding.selectTimeTv.text = getFormattedTime(hour, minute)

    }

    private fun onSelectTimeClick() {
        binding.selectTimeTv.setOnClickListener {
            val calendar = Calendar.getInstance()
            showTimePickerDialog(
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE),
                "Select Time",
                supportFragmentManager
            ) { hour, minute ->
                binding.selectTimeTv.text = getFormattedTime(hour, minute)
                timeCalendar.set(Calendar.HOUR, hour)
                timeCalendar.set(Calendar.MINUTE, minute)
                timeCalendar.clearDate()
                timeCalendar.clearSeconds()
                newTask.time = timeCalendar.timeInMillis
            }

        }
    }

    private fun onSelectDateClick() {
        binding.selectDateTv.setOnClickListener {

            showDatePickerDialog(this) { date, calendar ->
                binding.selectDateTv.text = date
                dateCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                dateCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                dateCalendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
                dateCalendar.clearTime()
                newTask.date = dateCalendar.timeInMillis
            }
        }
    }
}