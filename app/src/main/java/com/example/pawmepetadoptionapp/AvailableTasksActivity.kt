package com.example.pawmepetadoptionapp

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawmepetadoptionapp.databinding.ActivityAvailableTasksBinding

class AvailableTasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAvailableTasksBinding
    private lateinit var adapter: VolunteerTaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAvailableTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.backButton.setOnClickListener {
            finish()
        }


        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this)

        val taskList = listOf(

            VolunteerTask("Morning Feeding - Shelter Dogs", "Colombo", "9:00 AM", "2 hours", "High"),
            VolunteerTask("Evening Feeding - Rescue Cats", "Negombo", "6:30 PM", "1.5 hours", "Medium"),


            VolunteerTask("Transport Dog to Vet", "Kandy", "11:00 AM", "3 hours", "High"),
            VolunteerTask("Pick Up Donations - Food Bags", "Galle", "3:00 PM", "2 hours", "Low"),


            VolunteerTask("Bath and Brush - Senior Dogs", "Colombo", "10:00 AM", "2 hours", "Medium"),
            VolunteerTask("Clean Ears & Trim Nails", "Kurunegala", "1:00 PM", "1.5 hours", "Low"),


            VolunteerTask("Assist in Vaccination Drive", "Matara", "9:30 AM", "4 hours", "High"),
            VolunteerTask("Distribute Medical Supplies", "Anuradhapura", "2:00 PM", "2 hours", "Medium"),


            VolunteerTask("Help at Adoption Fair", "Colombo", "8:00 AM", "6 hours", "High"),
            VolunteerTask("Fundraiser Setup Support", "Kandy", "5:00 PM", "3 hours", "Medium")
        )

        adapter = VolunteerTaskAdapter(taskList)
        binding.tasksRecyclerView.adapter = adapter


        binding.searchBtn.setOnClickListener {
            val query = binding.searchBar.text.toString()
            adapter.filter.filter(query)
        }


        binding.searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                val query = binding.searchBar.text.toString()
                adapter.filter.filter(query)
                true
            } else {
                false
            }
        }
    }
}

