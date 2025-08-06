package com.example.pawmepetadoptionapp

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawmepetadoptionapp.databinding.ActivityHistoryBinding
import com.google.firebase.firestore.FirebaseFirestore

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val taskList = mutableListOf<VolunteerTask>()
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
    }

    private fun fetchCompletedTasksFromFirestore() {
        firestore.collection("volunteerTasks")
            .whereEqualTo("status", "Completed")
            .get()
            .addOnSuccessListener { result ->
                taskList.clear()
                for (doc in result) {
                    val task = doc.toObject(VolunteerTask::class.java)
                    taskList.add(task)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to fetch history tasks", Toast.LENGTH_SHORT).show()
            }
    }
}
