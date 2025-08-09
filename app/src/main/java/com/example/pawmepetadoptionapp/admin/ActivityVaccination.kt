package com.example.pawmepetadoptionapp.admin

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawmepetadoptionapp.databinding.ActivityVaccinationBinding
import com.example.pawmepetadoptionapp.admin.adapter.VaccinationAdapter
import com.example.pawmepetadoptionapp.admin.model.Vaccination
import com.example.pawmepetadoptionapp.admin.model.Dog
import com.example.pawmepetadoptionapp.admin.dialog.VaccinationDialogFragment
import com.google.firebase.firestore.FirebaseFirestore

class ActivityVaccination : AppCompatActivity() {

    private lateinit var binding: ActivityVaccinationBinding
    private lateinit var adapter: VaccinationAdapter
    private val db = FirebaseFirestore.getInstance()
    private val vaccinationList = mutableListOf<Vaccination>()
    private val dogList = mutableListOf<Dog>()
    private var selectedDogId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVaccinationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = VaccinationAdapter(vaccinationList, this)
        binding.recyclerViewVaccinations.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewVaccinations.adapter = adapter

        loadDogs()
    }

    private fun loadDogs() {
        db.collection("dogs")
            .get()
            .addOnSuccessListener { result ->
                dogList.clear()
                for (document in result) {
                    val name = document.getString("name") ?: ""
                    val breed = document.getString("breed") ?: ""
                    val age = document.getLong("age")?.toInt() ?: 0
                    val duration = document.getString("duration") ?: ""
                    val needs = document.getString("needs") ?: ""
                    val isAvailable = document.getBoolean("available") ?: true
                    val imageName = document.getString("imageName") ?: ""
                    val imageUrl = document.getString("imageUrl")
                    val type = document.getString("type") ?: ""
                    val id = document.id
                    dogList.add(Dog(id, name, breed, age, duration, needs, isAvailable, imageName, imageUrl, type))
                }

                // If you want to filter by dog, add a Spinner to your activity layout and set it up here.
                // Otherwise, just show the first dog's vaccinations:
                if (dogList.isNotEmpty()) {
                    selectedDogId = dogList[0].id
                    loadVaccinations()
                }

                // If you want to let the user select which dog's vaccinations to show:
                // Uncomment the following section and add a Spinner to your XML layout (above the RecyclerView):

                val dogNames = dogList.map { it.name }
                val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dogNames)
                binding.spinnerDogSelector.adapter = spinnerAdapter
                binding.spinnerDogSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                        selectedDogId = dogList[position].id
                        loadVaccinations()
                    }
                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }


                // Set up the add button only after dogs are loaded
                binding.btnAddVaccination.setOnClickListener {
                    VaccinationDialogFragment(dogList) { dogId ->
                        selectedDogId = dogId
                        loadVaccinations()
                    }.show(supportFragmentManager, "AddVaccinationDialog")
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load dogs", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadVaccinations() {
        val dogId = selectedDogId ?: return
        db.collection("dogs").document(dogId)
            .collection("vaccination")
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
        if (selectedDogId != null) {
            loadVaccinations()
        }
    }
}