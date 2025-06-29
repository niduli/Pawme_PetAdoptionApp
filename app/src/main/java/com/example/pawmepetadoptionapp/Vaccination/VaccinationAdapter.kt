package com.example.pawmepetadoptionapp.Vaccination

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmepetadoptionapp.R

class VaccinationAdapter (private val vaccines: List<VaccinationTrackerFragment.Vaccine>) :
    RecyclerView.Adapter<VaccinationAdapter.VaccineViewHolder>() {

    class VaccineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.findViewById(R.id.txtVaccineName)
        val txtDue: TextView = view.findViewById(R.id.txtVaccineDue)
        val btnReminder: Button = view.findViewById(R.id.btnReminder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaccineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vaccination_item, parent, false)
        return VaccineViewHolder(view)
    }

    override fun onBindViewHolder(holder: VaccineViewHolder, position: Int) {
        val vaccine = vaccines[position]

        holder.txtName.text = vaccine.name
        holder.txtDue.text = if (vaccine.isCompleted) "${vaccine.name} - Completed ✅" else "${vaccine.name} - Due ${vaccine.dueDate} ❗"
        holder.btnReminder.visibility = if (vaccine.isCompleted) View.GONE else View.VISIBLE

        holder.btnReminder.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Reminder set for ${vaccine.name}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int = vaccines.size

}