package com.example.pawmepetadoptionapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawmepetadoptionapp.databinding.ActivityAvailableTasksBinding

class AvailableTasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAvailableTasksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAvailableTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        val taskList = listOf(
            VolunteerTask("Morning Feeding - Shelter Dogs", "Colombo", "9:00 AM", "2 hours", "High Priority"),
            VolunteerTask("Vet Transport", "Kandy", "11:00 AM", "3 hours", "Medium"),
            // Add more dummy tasks...
        )

        val adapter = VolunteerTaskAdapter(taskList)
        binding.tasksRecyclerView.adapter = adapter
    }
}
