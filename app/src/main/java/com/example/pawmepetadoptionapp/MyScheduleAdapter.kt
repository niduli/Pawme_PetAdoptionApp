package com.example.pawmepetadoptionapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyScheduleAdapter(
    private val taskList: MutableList<AssignedTask>,
    private val onActionClick: (String, AssignedTask) -> Unit
) : RecyclerView.Adapter<MyScheduleAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.taskName)
        val time: TextView = view.findViewById(R.id.taskTime)
        val location: TextView = view.findViewById(R.id.taskLocation)
        val status: TextView = view.findViewById(R.id.taskStatus)
        val cancelButton: Button = view.findViewById(R.id.cancelButton)
        val startButton: Button = view.findViewById(R.id.startButton)
        val completeButton: Button = view.findViewById(R.id.completeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_schedule, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        val context = holder.itemView.context

        holder.name.text = task.name
        holder.time.text = context.getString(R.string.task_time, task.time)
        holder.location.text = context.getString(R.string.task_location, task.location)
        holder.status.text = context.getString(R.string.task_status, task.status)

        holder.cancelButton.setOnClickListener {
            onActionClick("Cancel", task)
        }

        holder.startButton.setOnClickListener {
            onActionClick("Start", task)
        }

        holder.completeButton.setOnClickListener {
            onActionClick("Complete", task)
        }
    }

    override fun getItemCount(): Int = taskList.size

    fun removeTask(task: AssignedTask) {
        val index = taskList.indexOf(task)
        if (index != -1) {
            taskList.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}