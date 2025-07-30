package com.example.pawmepetadoptionapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryTaskAdapter(private val taskList: List<VolunteerTask>) :
    RecyclerView.Adapter<HistoryTaskAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.taskTitle)
        val location: TextView = view.findViewById(R.id.taskLocation)
        val time: TextView = view.findViewById(R.id.taskTime)
        val duration: TextView = view.findViewById(R.id.taskDuration)
        val status: TextView = view.findViewById(R.id.taskUrgency) // Use this as 'Completed'
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_card, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val task = taskList[position]
        val context = holder.itemView.context

        holder.title.text = task.title
        holder.location.text = context.getString(R.string.location_label, task.location)
        holder.time.text = context.getString(R.string.time_label, task.time)
        holder.duration.text = context.getString(R.string.duration_label, task.duration)
        holder.status.text = context.getString(R.string.completed_label)
        holder.status.setTextColor(context.getColor(android.R.color.holo_green_dark))


        holder.itemView.findViewById<View>(R.id.applyButton)?.visibility = View.GONE
    }

    override fun getItemCount(): Int = taskList.size
}
