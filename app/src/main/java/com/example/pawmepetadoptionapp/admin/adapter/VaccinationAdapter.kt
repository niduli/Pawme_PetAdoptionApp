package com.example.pawmepetadoptionapp.admin.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmepetadoptionapp.admin.model.Vaccination
import com.example.pawmepetadoptionapp.databinding.ItemVaccinationBinding
import com.google.firebase.firestore.FirebaseFirestore

class VaccinationAdapter(
    private val list: List<Vaccination>,
    private val context: Context
) : RecyclerView.Adapter<VaccinationAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemVaccinationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemVaccinationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.tvDogName.text = item.dogName
        holder.binding.tvVaccine.text = item.vaccine
        holder.binding.tvDate.text = "Date: ${item.date}"
        holder.binding.tvDue.text = "Due: ${item.dueDate}"

        // Remove edit button logic for now
        holder.binding.btnEdit.setOnClickListener {
            // Optionally implement edit using dialog fragment
        }

        holder.binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this vaccination?")
                .setPositiveButton("Yes") { _, _ ->
                    FirebaseFirestore.getInstance().collection("vaccinations").document(item.id!!)
                        .delete()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    override fun getItemCount(): Int = list.size
}