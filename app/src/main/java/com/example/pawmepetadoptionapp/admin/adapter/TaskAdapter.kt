package com.example.pawmepetadoptionapp.admin.adapter



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmepetadoptionapp.R
import com.example.pawmepetadoptionapp.admin.model.VolunteerTask

class TaskAdapter(
    private val tasks: List<VolunteerTask>,
    private val onClick: (VolunteerTask) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvTaskName)
        val tvStatus: TextView = view.findViewById(R.id.tvTaskStatus)
        val tvUrgency: TextView = view.findViewById(R.id.tvTaskUrgency)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.tvName.text = task.name
        holder.tvStatus.text = "Status: ${task.status}"
        holder.tvUrgency.text = "Urgency: ${task.urgency}"

        holder.itemView.setOnClickListener {
            onClick(task)
        }
    }

    override fun getItemCount(): Int = tasks.size
}
