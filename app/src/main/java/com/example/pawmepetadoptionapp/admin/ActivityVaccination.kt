package com.example.pawmepetadoptionapp.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawmepetadoptionapp.databinding.ActivityVaccinationBinding
import com.example.pawmepetadoptionapp.admin.adapter.VaccinationAdapter
import com.example.pawmepetadoptionapp.admin.model.Vaccination
import com.example.pawmepetadoptionapp.admin.dialog.VaccinationDialogFragment
import com.google.firebase.firestore.FirebaseFirestore

class ActivityVaccination : AppCompatActivity() {

    private lateinit var binding: ActivityVaccinationBinding
    private lateinit var adapter: VaccinationAdapter
    private val db = FirebaseFirestore.getInstance()
    private val vaccinationList = mutableListOf<Vaccination>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVaccinationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = VaccinationAdapter(vaccinationList, this)
        binding.recyclerViewVaccinations.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewVaccinations.adapter = adapter

        binding.btnAddVaccination.setOnClickListener {
            VaccinationDialogFragment {
                loadVaccinations()
            }.show(supportFragmentManager, "AddVaccinationDialog")
        }

        loadVaccinations()
    }

    private fun loadVaccinations() {
        db.collection("vaccinations")
            .get()
            .addOnSuccessListener { result ->
                vaccinationList.clear()
                for (document in result) {
                    val vaccination = document.toObject(Vaccination::class.java)
                    vaccination.id = document.id
                    vaccinationList.add(vaccination)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onResume() {
        super.onResume()
        loadVaccinations()
    }
}