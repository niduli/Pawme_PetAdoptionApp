package com.example.pawmepetadoptionapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawmepetadoptionapp.databinding.ActivityHistoryBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val taskList = mutableListOf<AssignedTask>()
    private lateinit var adapter: HistoryTaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = HistoryTaskAdapter(taskList)
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = adapter

        binding.backButton.setOnClickListener { finish() }

        fetchCompletedTasksFromFirestore()

        val sloganText = getString(R.string.history_slogan)
        val spannable = SpannableString(sloganText)
        val start = sloganText.indexOf("Pawprint")
        if (start >= 0) {
            val end = start + "Pawprint".length
            spannable.setSpan(
                ForegroundColorSpan(Color.parseColor("#FF6F4E")),
                start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        binding.historySubtitle.text = spannable

        // Bottom Navigation setup
        val bottomNav = findViewById<BottomNavigationView>(R.id.volunteerBottomNav)
        bottomNav.selectedItemId = R.id.nav_history
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
                    startActivity(Intent(this, MyScheduleActivity::class.java))
                    true
                }
                R.id.nav_history -> true
                R.id.nav_paw_alert -> {
                    startActivity(Intent(this, StrayDogReportFormActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun fetchCompletedTasksFromFirestore() {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("assigned_tasks")
            .whereEqualTo("userId", userId)
            .whereEqualTo("status", "Completed")
            .get()
            .addOnSuccessListener { result ->
                taskList.clear()
                for (doc in result) {
                    val task = doc.toObject(AssignedTask::class.java)
                    taskList.add(task)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to fetch history tasks", Toast.LENGTH_SHORT).show()
            }
    }
}
