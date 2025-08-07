package com.example.pawmepetadoptionapp.admin.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pawmepetadoptionapp.databinding.ActivityVaccinationDialogBinding
import com.example.pawmepetadoptionapp.admin.model.Vaccination
import com.google.firebase.firestore.FirebaseFirestore

class VaccinationDialogFragment(private val onSaved: () -> Unit) : DialogFragment() {
    private lateinit var binding: ActivityVaccinationDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = ActivityVaccinationDialogBinding.inflate(layoutInflater)
        return AlertDialog.Builder(requireContext())
            .setTitle("Add Vaccination")
            .setView(binding.root)
            .setPositiveButton("Save") { _, _ ->
                val dogName = binding.etDogName.text.toString().trim()
                val vaccine = binding.etVaccine.text.toString().trim()
                val date = binding.etDate.text.toString().trim()
                val dueDate = binding.etDueDate.text.toString().trim()
                if (dogName.isEmpty() || vaccine.isEmpty() || date.isEmpty() || dueDate.isEmpty()) {
                    Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                val vaccination = Vaccination(dogName, vaccine, date, dueDate)
                FirebaseFirestore.getInstance().collection("vaccinations")
                    .add(vaccination)
                    .addOnSuccessListener { onSaved() }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Save failed", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancel", null)
            .create()
    }
}