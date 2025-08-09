package com.example.pawmepetadoptionapp.admin.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmepetadoptionapp.R
import com.example.pawmepetadoptionapp.admin.model.User

class VolunteerAdapter(
    private val volunteers: List<User>,
    private val onClick: (User) -> Unit
) : RecyclerView.Adapter<VolunteerAdapter.VolunteerViewHolder>() {

    inner class VolunteerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvVolunteerName)
        val tvEmail: TextView = view.findViewById(R.id.tvVolunteerEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VolunteerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_volunteer, parent, false)
        return VolunteerViewHolder(view)
    }

    override fun onBindViewHolder(holder: VolunteerViewHolder, position: Int) {
        val volunteer = volunteers[position]
        holder.tvName.text = volunteer.username
        holder.tvEmail.text = volunteer.email

        holder.itemView.setOnClickListener {
            onClick(volunteer)
        }
    }

    override fun getItemCount(): Int = volunteers.size
}
