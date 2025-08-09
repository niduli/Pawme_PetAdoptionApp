package com.example.pawmepetadoptionapp.admin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmepetadoptionapp.R
import com.example.pawmepetadoptionapp.admin.model.Task

class TaskAdapter(
    private val tasks: List<Task>,
    private val onClick: ((Task) -> Unit)? = null
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvTaskName)
        val tvTime: TextView = view.findViewById(R.id.tvTaskTime)
        val tvLocation: TextView = view.findViewById(R.id.tvTaskLocation)
        val tvStatus: TextView = view.findViewById(R.id.tvTaskStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task_unassigned, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.tvName.text = task.name
        holder.tvTime.text = "Time: ${task.time}"
        holder.tvLocation.text = "Location: ${task.location}"
        holder.tvStatus.text = "Status: ${task.status}"

        holder.itemView.setOnClickListener {
            onClick?.invoke(task)
        }
    }

    override fun getItemCount(): Int = tasks.size
}
