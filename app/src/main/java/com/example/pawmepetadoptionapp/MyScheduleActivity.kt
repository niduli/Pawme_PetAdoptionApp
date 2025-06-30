package com.example.pawmepetadoptionapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawmepetadoptionapp.databinding.ActivityMyScheduleBinding

class MyScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyScheduleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val taskList = listOf(
            AssignedTask("Morning Feeding - Dogs", "8:00 AM", "Colombo Shelter", "Confirmed"),
            AssignedTask("Vet Visit - Cats", "10:00 AM", "Kandy Shelter", "Pending")
        )

        val adapter = MyScheduleAdapter(taskList) { action, task ->
            Toast.makeText(this, "$action clicked for ${task.name}", Toast.LENGTH_SHORT).show()
        }

        binding.scheduleRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.scheduleRecyclerView.adapter = adapter
    }
}
