package com.reham11203.todoapp.ui.home.fragments.tasks_fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.reham11203.todoapp.R
import com.reham11203.todoapp.database.TasksDatabase
import com.reham11203.todoapp.database.dao.TasksDao
import com.reham11203.todoapp.database.entity.Task
import com.reham11203.todoapp.databinding.FragmentTasksBinding
import com.reham11203.todoapp.ui.home.EditTaskActivity
import com.reham11203.todoapp.ui.util.Constants
import com.reham11203.todoapp.ui.util.clearTime
import java.util.Calendar

class TasksFragment : Fragment() {

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!
    private val adapter = TasksAdapter()
    private lateinit var dao: TasksDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dao = TasksDatabase.getInstance().tasksDao()
        initRecyclerView()
        initCalendarView()

    }

    private fun initCalendarView() {
        binding.calendarView.selectedDate = CalendarDay.today()
        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, date.year)
            calendar.set(Calendar.MONTH, date.month - 1)
            calendar.set(Calendar.DAY_OF_MONTH, date.day)
            calendar.clearTime()
            if (selected) {
                val tasks = dao.getAllTasksByDate(calendar.timeInMillis).toMutableList()
                adapter.setTasksList(tasks)
            }

        }
    }

    private fun initRecyclerView() {
        binding.tasksRecycler.adapter = adapter
        adapter.onDeleteBtnClickListener = TasksAdapter.OnTaskClickListener { position, task ->
            showDeleteDialog(position, task)
        }
        adapter.onDoneBtnClickListener = TasksAdapter.OnTaskClickListener { position, task ->
            task.isCompleted = !task.isCompleted
            dao.updateTask(task)
            adapter.updateTask(position, task)
        }
        val editTaskLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    loadAllTasksOfDate(getSelectedDate().timeInMillis)
                }
            }
        adapter.onTaskClickListener = TasksAdapter.OnTaskClickListener { position, task ->
            val intent = Intent(requireContext(), EditTaskActivity::class.java)
            intent.putExtra(Constants.TASK_KEY, task)
            editTaskLauncher.launch(intent)
        }


    }

    private fun showDeleteDialog(position: Int, task: Task) {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setTitle(getString(R.string.delete))
            .setMessage(getString(R.string.delete_confirmation_message))
            .setPositiveButton(getString(R.string.delete)) { dialog, _ ->
                dao.deleteTask(task)
                adapter.deleteTask(position)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY)
    }


    private fun getSelectedDate(): Calendar {
        val calendar = Calendar.getInstance()
        binding.calendarView.selectedDate?.let { date ->
            calendar.set(Calendar.YEAR, date.year)
            calendar.set(Calendar.MONTH, date.month - 1)
            calendar.set(Calendar.DAY_OF_MONTH, date.day)
            calendar.clearTime()
        }

        return calendar
    }

    fun loadAllTasksOfDate(date: Long) {
        val tasks = dao.getAllTasksByDate(date).toMutableList()
        adapter.setTasksList(tasks)

    }


    override fun onStart() {
        super.onStart()
        loadAllTasksOfDate(getSelectedDate().timeInMillis)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter.onDeleteBtnClickListener = null
        adapter.onDoneBtnClickListener = null
        adapter.onTaskClickListener = null
    }
}