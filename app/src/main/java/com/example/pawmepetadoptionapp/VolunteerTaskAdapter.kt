package com.example.pawmepetadoptionapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import android.widget.Filter
import android.widget.Filterable

class VolunteerTaskAdapter(private val taskList: List<VolunteerTask>) :
    RecyclerView.Adapter<VolunteerTaskAdapter.TaskViewHolder>(), Filterable {

    private var filteredTaskList = taskList.toMutableList()

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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = filteredTaskList[position]
        val context = holder.itemView.context

        holder.title.text = task.title
        holder.location.text = "Location: ${task.location}"
        holder.time.text = "Time: ${task.time}"
        holder.duration.text = "Duration: ${task.duration}"
        holder.urgency.text = "Urgency: ${task.urgency}"

        holder.applyButton.setOnClickListener {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_task_details, null)

            val title = dialogView.findViewById<TextView>(R.id.taskDetailTitle)
            val desc = dialogView.findViewById<TextView>(R.id.taskDescription)
            val skills = dialogView.findViewById<TextView>(R.id.taskSkills)
            val location = dialogView.findViewById<TextView>(R.id.taskLocation)
            val confirmBtn = dialogView.findViewById<Button>(R.id.confirmApplyButton)

            title.text = task.title
            desc.text = "Help us with ${task.title.lowercase()} at ${task.location}."
            skills.text = "Required Skills: Compassion, Responsibility"
            location.text = "Location: ${task.location}"

            val dialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .create()

            confirmBtn.setOnClickListener {
                val assignedTask = AssignedTask(task.title, task.time, task.location, "Confirmed")
                AssignedTasksStore.addTask(assignedTask)
                Toast.makeText(context, "Task added to your schedule!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()

                context.startActivity(Intent(context, MyScheduleActivity::class.java))
            }

            dialog.show()
        }
    }

    override fun getItemCount(): Int = filteredTaskList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val filtered = if (query.isNullOrBlank()) {
                    taskList
                } else {
                    taskList.filter {
                        it.title.contains(query, ignoreCase = true) ||
                                it.location.contains(query, ignoreCase = true) ||
                                it.urgency.contains(query, ignoreCase = true)
                    }
                }
                return FilterResults().apply { values = filtered }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val resultList = results?.values
                if (resultList is List<*>) {
                    filteredTaskList = resultList.filterIsInstance<VolunteerTask>().toMutableList()
                    notifyDataSetChanged()
                }
            }
        }
    }
}

