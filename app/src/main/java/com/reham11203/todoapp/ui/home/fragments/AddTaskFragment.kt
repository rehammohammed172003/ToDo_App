package com.reham11203.todoapp.ui.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.reham11203.todoapp.R
import com.reham11203.todoapp.database.TasksDatabase
import com.reham11203.todoapp.database.dao.TasksDao
import com.reham11203.todoapp.database.entity.Task
import com.reham11203.todoapp.databinding.FragmentAddTaskBinding
import com.reham11203.todoapp.ui.util.clearTime
import com.reham11203.todoapp.ui.util.getFormattedTime
import com.reham11203.todoapp.ui.util.showDatePickerDialog
import com.reham11203.todoapp.ui.util.showTimePickerDialog
import java.util.Calendar


class AddTaskFragment : BottomSheetDialogFragment() {
    lateinit var binding: FragmentAddTaskBinding
    private var dateCalendar = Calendar.getInstance()
    private var timeCalendar = Calendar.getInstance()
    var onTaskAdded: OnTaskAdded? = null
    lateinit var dao: TasksDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dao = TasksDatabase.getInstance().tasksDao()
        onSelectDateClick()
        onSelectTimeClick()
        onAddTaskClick()

    }

    private fun onAddTaskClick() {
        binding.addTaskBtn.setOnClickListener {
            if (!validateInput())
                return@setOnClickListener
            val task = createTask()
            dao.addNewTask(task)
            onTaskAdded?.onTaskAdded(task)

        }
    }

    private fun createTask(): Task {
        return Task(
            title = binding.taskTitle.text.toString(),
            date = dateCalendar.timeInMillis,
            time = timeCalendar.timeInMillis,
            description = binding.taskDescription.text.toString(),
            id = TODO(),
            isCompleted = TODO(),
        )
    }

    private fun validateInput(): Boolean {
        var isValid = true
        if (binding.taskTitle.text.isNullOrBlank()) {
            isValid = false
            binding.taskTitle.error = getString(R.string.required_field)
        }
        if (binding.selectDateTv.text.isNullOrBlank() || binding.selectDateTv.text == getString(R.string.select_date)) {
            isValid = false
            binding.selectDateTv.error = getString(R.string.required_field)
        }
        if (binding.selectTimeTv.text.isNullOrBlank() || binding.selectTimeTv.text == getString(R.string.select_time)) {
            isValid = false
            binding.selectTimeTv.error = getString(R.string.required_field)
        }
        return isValid


    }

    private fun onSelectTimeClick() {
        binding.selectTimeTv.setOnClickListener {
            val calendar = Calendar.getInstance()
            showTimePickerDialog(
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE),
                "Select Time",
                childFragmentManager
            ) { hour, minute ->
                binding.selectTimeTv.text = getFormattedTime(hour, minute)
            }

        }
    }

    private fun onSelectDateClick() {
        binding.selectDateTv.setOnClickListener {

            showDatePickerDialog(requireContext()) { date, calendar ->
                binding.selectDateTv.text = date
                dateCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                dateCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                dateCalendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
                dateCalendar.clearTime()
            }
        }
    }

    fun interface OnTaskAdded {
        fun onTaskAdded(task: Task)
    }

}