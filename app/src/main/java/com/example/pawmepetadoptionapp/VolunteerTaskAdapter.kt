package com.example.pawmepetadoptionapp

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class VolunteerTaskAdapter(private val taskList: List<VolunteerTask>) :
    RecyclerView.Adapter<VolunteerTaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.taskTitle)
        val location: TextView = view.findViewById(R.id.taskLocation)
        val time: TextView = view.findViewById(R.id.taskTime)
        val duration: TextView = view.findViewById(R.id.taskDuration)
        val urgency: TextView = view.findViewById(R.id.taskUrgency)
        val applyButton: Button = view.findViewById(R.id.applyButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_card, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        val context = holder.itemView.context

        holder.title.text = task.title
        holder.location.text = context.getString(R.string.location_label, task.location)
        holder.time.text = context.getString(R.string.time_label, task.time)
        holder.duration.text = context.getString(R.string.duration_label, task.duration)
        holder.urgency.text = task.urgency

        holder.applyButton.setOnClickListener {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_task_details, null)

            val title = dialogView.findViewById<TextView>(R.id.taskDetailTitle)
            val desc = dialogView.findViewById<TextView>(R.id.taskDescription)
            val skills = dialogView.findViewById<TextView>(R.id.taskSkills)
            val location = dialogView.findViewById<TextView>(R.id.taskLocation)
            val confirmBtn = dialogView.findViewById<Button>(R.id.confirmApplyButton)

            title.text = task.title
            desc.text = context.getString(R.string.task_detail_description, task.title.lowercase())
            skills.text = context.getString(R.string.required_skills_label)
            location.text = context.getString(R.string.location_label, task.location)

            val dialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .create()

            confirmBtn.setOnClickListener {
                Toast.makeText(context, context.getString(R.string.task_added_to_schedule), Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            dialog.show()
        }
    }


    override fun getItemCount(): Int = taskList.size
}
