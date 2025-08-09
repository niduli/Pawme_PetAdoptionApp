package com.example.pawmepetadoptionapp.admin


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawmepetadoptionapp.admin.adapter.TaskAdapter
import com.example.pawmepetadoptionapp.admin.model.VolunteerTask
import com.example.pawmepetadoptionapp.databinding.ActivityAdminTaskManagementBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminTaskManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminTaskManagementBinding
    private lateinit var adapter: TaskAdapter
    private val taskList = mutableListOf<VolunteerTask>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminTaskManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TaskAdapter(taskList) { task ->
            Toast.makeText(this, "Selected task: ${task.name}", Toast.LENGTH_SHORT).show()
            // Add edit or assign task logic here
        }

        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewTasks.adapter = adapter

        binding.btnAddTask.setOnClickListener {
            addTask()
        }

        fetchTasks()
    }

    private fun fetchTasks() {
        db.collection("tasks")
            .get()
            .addOnSuccessListener { documents ->
                taskList.clear()
                for (doc in documents) {
                    val task = doc.toObject(VolunteerTask::class.java).copy(id = doc.id)
                    taskList.add(task)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load tasks", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addTask() {
        val name = binding.etTaskName.text.toString().trim()
        val location = binding.etTaskLocation.text.toString().trim()
        val time = binding.etTaskTime.text.toString().trim()
        val duration = binding.etTaskDuration.text.toString().trim()
        val urgency = binding.etTaskUrgency.text.toString().trim()

        if (name.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Name and Location are required", Toast.LENGTH_SHORT).show()
            return
        }

        val task = VolunteerTask(
            name = name,
            location = location,
            time = time,
            duration = duration,
            urgency = urgency,
            status = "open",
            assignedTo = null
        )

        db.collection("tasks")
            .add(task)
            .addOnSuccessListener {
                Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show()
                clearInputFields()
                fetchTasks()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add task", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearInputFields() {
        binding.etTaskName.text?.clear()
        binding.etTaskLocation.text?.clear()
        binding.etTaskTime.text?.clear()
        binding.etTaskDuration.text?.clear()
        binding.etTaskUrgency.text?.clear()
    }
}
