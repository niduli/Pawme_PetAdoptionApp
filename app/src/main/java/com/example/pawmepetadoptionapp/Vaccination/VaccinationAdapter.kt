package com.example.pawmepetadoptionapp.Vaccination

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmepetadoptionapp.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VaccinationAdapter (private val items: List<VaccinationTrackerFragment.Vaccine>,
                          private val onReminderSet: (VaccinationTrackerFragment.Vaccine) -> Unit
) : RecyclerView.Adapter<VaccinationAdapter.VaccineViewHolder>() {
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
        val vaccine = items[position]
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val dueText = if (vaccine.isCompleted) "Completed ✅" else "Due: ${dateFormat.format(Date(vaccine.date))}"

        holder.txtName.text = vaccine.name
        holder.txtDue.text = dueText

        holder.btnReminder.visibility = if (vaccine.isCompleted || vaccine.isReminderSet) View.GONE else View.VISIBLE
        holder.btnReminder.setOnClickListener {
            onReminderSet(vaccine)
        }
    }

    override fun getItemCount(): Int = items.size


}