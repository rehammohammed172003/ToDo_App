package com.reham11203.todoapp.ui.home.fragments.tasks_fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.reham11203.todoapp.R
import com.reham11203.todoapp.database.entity.Task
import com.reham11203.todoapp.databinding.ItemTaskBinding
import com.reham11203.todoapp.ui.util.getFormattedTime
import com.zerobranch.layout.SwipeLayout
import com.zerobranch.layout.SwipeLayout.SwipeActionsListener
import java.util.Calendar

class TasksAdapter() : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    private var tasks = mutableListOf<Task>()

    @SuppressLint("NotifyDataSetChanged")
    fun setTasksList(tasks: MutableList<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemBinding =
            ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
        holder.bindIsDoneStatus(task)
        onDeleteBtnClickListener?.let {

            holder.itemBinding.swipeLayout.setOnActionsListener(object : SwipeActionsListener {
                override fun onOpen(direction: Int, isContinuous: Boolean) {
                    holder.itemBinding.leftView.isClickable = true
                    if (direction == SwipeLayout.RIGHT) {
                        holder.itemBinding.leftView.setOnClickListener {
                            onDeleteBtnClickListener?.onTaskClick(holder.adapterPosition, task)
                        }
                    }
                }

                override fun onClose() {
                    holder.itemBinding.leftView.isClickable = false
                }

            })
        }

        onDoneBtnClickListener?.let {
            holder.itemBinding.taskDoneBtn.setOnClickListener {
                onDoneBtnClickListener?.onTaskClick(position, task)
            }
        }

        onTaskClickListener?.let {
            holder.itemBinding.dragItem.setOnClickListener {
                onTaskClickListener?.onTaskClick(position, task)
            }
        }
    }

    var onDeleteBtnClickListener: OnTaskClickListener? = null
    var onDoneBtnClickListener: OnTaskClickListener? = null
    var onTaskClickListener: OnTaskClickListener? = null

    fun interface OnTaskClickListener {
        fun onTaskClick(position: Int, task: Task)
    }

    fun deleteTask(position: Int) {
        if (position in tasks.indices) {
            tasks.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, tasks.size - position)
        }
    }

    fun updateTask(position: Int, task: Task) {
        if (position in tasks.indices) {
            tasks[position] = task
            notifyItemChanged(position)
        }

    }

    class TaskViewHolder(val itemBinding: ItemTaskBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(task: Task) {
            itemBinding.taskTitle.text = task.title
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = task.time
            itemBinding.taskDeadline.text =
                getFormattedTime(calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE))
        }

        fun bindIsDoneStatus(task: Task) {
            if (task.isCompleted) {
                itemBinding.taskTitle.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.green
                    )
                )
                itemBinding.swapToDelete.setBackgroundResource(R.drawable.dismiss_done_mode)
                itemBinding.taskDoneBtn.setImageResource(R.drawable.done)
                itemBinding.taskDoneBtn.setBackgroundColor(Color.TRANSPARENT)
            } else {
                itemBinding.taskTitle.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.primary
                    )
                )
                itemBinding.swapToDelete.setBackgroundResource(R.drawable.dismiss)
                itemBinding.taskDoneBtn.setImageResource(R.drawable.ic_check)
                itemBinding.taskDoneBtn.setBackgroundResource(R.drawable.check_btn_background)
            }

        }
    }
}