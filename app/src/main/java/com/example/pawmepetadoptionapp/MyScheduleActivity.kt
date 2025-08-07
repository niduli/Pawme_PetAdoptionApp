package com.example.pawmepetadoptionapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawmepetadoptionapp.databinding.ActivityMyScheduleBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyScheduleBinding
    private lateinit var adapter: MyScheduleAdapter
    private val taskList = mutableListOf<AssignedTask>()

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            val intent = Intent(this, VolunteerDashboardActivity::class.java)
            startActivity(intent)
            finish()
        }

        userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        adapter = MyScheduleAdapter(taskList) { action, task ->
            when (action) {
                "Cancel" -> cancelTask(task)
                "Start" -> updateTaskStatus(task, "In Progress")
                "Complete" -> updateTaskStatus(task, "Completed")
            }
        }

        binding.scheduleRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.scheduleRecyclerView.adapter = adapter

        fetchScheduledTasks()

        // Bottom Navigation setup
        val bottomNav = findViewById<BottomNavigationView>(R.id.volunteerBottomNav)
        bottomNav.selectedItemId = R.id.nav_my_schedule
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, VolunteerDashboardActivity::class.java))
                    true
                }
                R.id.nav_available_tasks -> {
                    startActivity(Intent(this, AvailableTasksActivity::class.java))
                    true
                }
                R.id.nav_my_schedule -> {
                    // Already in MyScheduleActivity
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    true
                }
                R.id.nav_paw_alert -> {
                    startActivity(Intent(this, StrayDogReportFormActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchScheduledTasks() {
        db.collection("schedules")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                taskList.clear()
                for (doc in documents) {
                    val task = AssignedTask(
                        name = doc.getString("name") ?: "N/A",
                        time = doc.getString("time") ?: "N/A",
                        location = doc.getString("location") ?: "N/A",
                        status = doc.getString("status") ?: "Upcoming"
                    )
                    taskList.add(task)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load schedule: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun cancelTask(task: AssignedTask) {
        // Remove from Firestore schedules collection
        db.collection("schedules")
            .whereEqualTo("userId", userId)
            .whereEqualTo("name", task.name)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    db.collection("schedules").document(doc.id).delete()
                }
                taskList.remove(task)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Task cancelled", Toast.LENGTH_SHORT).show()

                // Optionally, update task status in tasks collection to "open"
                resetTaskStatus(task.name)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to cancel task: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun resetTaskStatus(taskName: String) {
        db.collection("tasks")
            .whereEqualTo("name", taskName)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    db.collection("tasks").document(doc.id)
                        .update(
                            mapOf(
                                "status" to "open",
                                "assignedTo" to null
                            )
                        )
                }
            }
    }

    private fun updateTaskStatus(task: AssignedTask, newStatus: String) {
        db.collection("schedules")
            .whereEqualTo("userId", userId)
            .whereEqualTo("name", task.name)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    db.collection("schedules").document(doc.id)
                        .update("status", newStatus)
                }
                val index = taskList.indexOf(task)
                if (index != -1) {
                    taskList[index] = task.copy(status = newStatus)
                    adapter.notifyItemChanged(index)
                }
                Toast.makeText(this, "Task '${task.name}' marked as $newStatus", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update status: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }
}