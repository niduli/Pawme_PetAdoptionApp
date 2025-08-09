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

class VaccinationAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val groupedItems = mutableListOf<Any>() // Can hold String (header) or Vaccine

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_VACCINE = 1
    }

    fun updateData(vaccines: List<VaccinationTrackerFragment.Vaccine>) {
        groupedItems.clear()

        val groupedMap = vaccines.groupBy { it.dogName }
        for ((dogName, vaccineList) in groupedMap) {
            groupedItems.add(dogName) // Add header
            groupedItems.addAll(vaccineList) // Add vaccines
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (groupedItems[position] is String) TYPE_HEADER else TYPE_VACCINE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dog_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.vaccination_item, parent, false)
            VaccineViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.txtDogName.text = groupedItems[position] as String
        } else if (holder is VaccineViewHolder) {
            val vaccine = groupedItems[position] as VaccinationTrackerFragment.Vaccine
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val dueText = if (vaccine.isCompleted) "✔ Completed"
            else "⏳ Due: ${dateFormat.format(Date(vaccine.date))}"

            holder.txtName.text = vaccine.name
            holder.txtDue.text = dueText
        }
    }

    override fun getItemCount(): Int = groupedItems.size

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtDogName: TextView = view.findViewById(R.id.txtDogHeader)
    }

    class VaccineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.findViewById(R.id.txtVaccineName)
        val txtDue: TextView = view.findViewById(R.id.txtVaccineDue)
    }
}

