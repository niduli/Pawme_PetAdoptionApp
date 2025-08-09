package com.example.pawmepetadoptionapp.admin.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pawmepetadoptionapp.databinding.ActivityVaccinationDialogBinding
import com.example.pawmepetadoptionapp.admin.model.Vaccination
import com.example.pawmepetadoptionapp.admin.model.Dog
import com.google.firebase.firestore.FirebaseFirestore

class VaccinationDialogFragment(
    private val dogList: List<Dog>,
    private val onSaved: (dogId: String) -> Unit
) : DialogFragment() {
    private lateinit var binding: ActivityVaccinationDialogBinding
    private var selectedDogId: String? = null
    private var selectedStatus: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = ActivityVaccinationDialogBinding.inflate(layoutInflater)

        // Setup dog selector spinner
        val dogNames = dogList.map { it.name }
        val dogAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, dogNames)
        binding.spinnerDogName.adapter = dogAdapter

        binding.spinnerDogName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                selectedDogId = dogList[position].id
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedDogId = null
            }
        }

        // Setup status spinner
        val statusOptions = listOf("completed", "due")
        val statusAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, statusOptions)
        binding.spinnerStatus.adapter = statusAdapter

        binding.spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                selectedStatus = statusOptions[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedStatus = null
            }
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Add Vaccination")
            .setView(binding.root)
            .setPositiveButton("Save") { _, _ ->
                val date = binding.etDate.text.toString().trim()
                val vaccineName = binding.etVaccine.text.toString().trim()
                val status = selectedStatus ?: ""
                val dogId = selectedDogId ?: dogList.getOrNull(0)?.id // fallback

                if (dogId.isNullOrEmpty() || date.isEmpty() || vaccineName.isEmpty() || status.isEmpty()) {
                    Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val vaccination = Vaccination(date, status, vaccineName)
                FirebaseFirestore.getInstance()
                    .collection("dogs").document(dogId)
                    .collection("vaccination")
                    .add(vaccination)
                    .addOnSuccessListener { onSaved(dogId!!) }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Save failed", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancel", null)
            .create()
    }
}