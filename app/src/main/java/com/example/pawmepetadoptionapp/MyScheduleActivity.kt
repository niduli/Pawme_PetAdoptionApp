package com.example.pawmepetadoptionapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawmepetadoptionapp.databinding.ActivityMyScheduleBinding

class MyScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyScheduleBinding
    private lateinit var adapter: MyScheduleAdapter
    private val taskList = mutableListOf<AssignedTask>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            val intent = Intent(this, VolunteerDashboardActivity::class.java)
            startActivity(intent)
            finish()
        }

        taskList.addAll(AssignedTasksStore.getTasks())

        adapter = MyScheduleAdapter(taskList) { action, task ->
            when (action) {
                "Cancel" -> {
                    AssignedTasksStore.removeTask(task)
                    adapter.removeTask(task)
                }
                "Start" -> updateTaskStatus(task, "In Progress")
                "Complete" -> updateTaskStatus(task, "Completed")
            }
        }


        binding.scheduleRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.scheduleRecyclerView.adapter = adapter
    }

    private fun updateTaskStatus(task: AssignedTask, newStatus: String) {
        val index = taskList.indexOf(task)
        if (index != -1) {
            taskList[index] = task.copy(status = newStatus)
            AssignedTasksStore.updateTask(index, taskList[index])
            adapter.notifyItemChanged(index)
            Toast.makeText(this, "Task '${task.name}' marked as $newStatus", Toast.LENGTH_SHORT).show()
        }
    }
}
