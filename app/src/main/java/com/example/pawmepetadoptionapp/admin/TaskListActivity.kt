package com.example.pawmepetadoptionapp.admin

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmepetadoptionapp.R
import com.example.pawmepetadoptionapp.admin.model.Task
import com.example.pawmepetadoptionapp.admin.adapter.TaskAdapter
import com.google.firebase.firestore.FirebaseFirestore

class TaskListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private val taskList = mutableListOf<Task>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        recyclerView = findViewById(R.id.recyclerViewTasks)
        val btnAdd: ImageButton = findViewById(R.id.btnAddTask)

        adapter = TaskAdapter(taskList) { /* handle click if needed */ }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnAdd.setOnClickListener {
            AddTaskDialog().show(supportFragmentManager, "AddTaskDialog")
        }

        loadTasks()
    }

    private fun loadTasks() {
        db.collection("tasks")
            .whereEqualTo("status", "unassigned")
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener

                taskList.clear()
                snapshot?.forEach { doc ->
                    val task = doc.toObject(Task::class.java)
                    taskList.add(task)
                }
                adapter.notifyDataSetChanged()
            }
    }
}
