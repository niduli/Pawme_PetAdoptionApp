package com.example.pawmepetadoptionapp

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawmepetadoptionapp.databinding.ActivityAvailableTasksBinding
import com.google.firebase.firestore.FirebaseFirestore

class AvailableTasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAvailableTasksBinding
    private lateinit var adapter: VolunteerTaskAdapter
    private val allTasks = mutableListOf<VolunteerTask>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAvailableTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { finish() }

        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = VolunteerTaskAdapter(allTasks)
        binding.tasksRecyclerView.adapter = adapter

        fetchTasksFromFirestore()

        binding.searchBtn.setOnClickListener {
            val query = binding.searchBar.text.toString()
            adapter.filter.filter(query)
        }

        binding.searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                val query = binding.searchBar.text.toString()
                adapter.filter.filter(query)
                true
            } else false
        }
    }

    private fun fetchTasksFromFirestore() {
        db.collection("volunteer_tasks")
            .get()
            .addOnSuccessListener { result ->
                allTasks.clear()
                for (doc in result) {
                    val task = doc.toObject(VolunteerTask::class.java)
                    allTasks.add(task)
                }
                adapter = VolunteerTaskAdapter(allTasks)
                binding.tasksRecyclerView.adapter = adapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load tasks", Toast.LENGTH_SHORT).show()
                Log.e("AvailableTasksActivity", "Error fetching tasks", e)
            }
    }
}
