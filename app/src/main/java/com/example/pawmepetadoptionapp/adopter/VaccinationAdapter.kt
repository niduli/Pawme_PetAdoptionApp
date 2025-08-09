package com.example.pawmepetadoptionapp.adopter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmepetadoptionapp.R

class VaccinationAdapter(private var vaccinationList: List<VaccinationAdoption>) :
    RecyclerView.Adapter<VaccinationAdapter.VaccinationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaccinationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vaccination_adoption, parent, false)
        return VaccinationViewHolder(view)
    }

    override fun onBindViewHolder(holder: VaccinationViewHolder, position: Int) {
        val vaccination = vaccinationList[position]
        holder.vaccineName.text = vaccination.vaccineName
        holder.vaccineDate.text = vaccination.date

        if (vaccination.status == "completed") {
            holder.statusIcon.setImageResource(R.drawable.check)
            holder.statusIcon.setColorFilter(holder.itemView.context.getColor(android.R.color.holo_green_dark))
        } else {
            holder.statusIcon.setImageResource(R.drawable.close_icon)
            holder.statusIcon.setColorFilter(holder.itemView.context.getColor(android.R.color.holo_orange_dark))
        }
    }

    override fun getItemCount() = vaccinationList.size

    fun updateData(newList: List<VaccinationAdoption>) {
        vaccinationList = newList
        notifyDataSetChanged()
    }

    class VaccinationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val statusIcon: ImageView = itemView.findViewById(R.id.statusIcon)
        val vaccineName: TextView = itemView.findViewById(R.id.vaccineNameText)
        val vaccineDate: TextView = itemView.findViewById(R.id.vaccineDateText)
    }
}
