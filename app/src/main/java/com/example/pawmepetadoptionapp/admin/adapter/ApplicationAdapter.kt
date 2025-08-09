package com.example.pawmepetadoptionapp.admin.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmepetadoptionapp.R
import com.example.pawmepetadoptionapp.admin.model.Application

class ApplicationAdapter(
    private val applications: List<Application>,
    private val onApprove: (Application) -> Unit,
    private val onReject: (Application) -> Unit
) : RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder>() {

    inner class ApplicationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvVolunteerId: TextView = view.findViewById(R.id.tvVolunteerId)
        val tvTaskId: TextView = view.findViewById(R.id.tvTaskId)
        val btnApprove: Button = view.findViewById(R.id.btnApprove)
        val btnReject: Button = view.findViewById(R.id.btnReject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_application, parent, false)
        return ApplicationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ApplicationViewHolder, position: Int) {
        val app = applications[position]
        holder.tvVolunteerId.text = "Volunteer: ${app.volunteerId}"
        holder.tvTaskId.text = "Task: ${app.taskId}"

        holder.btnApprove.setOnClickListener {
            onApprove(app)
        }
        holder.btnReject.setOnClickListener {
            onReject(app)
        }
    }

    override fun getItemCount(): Int = applications.size
}
