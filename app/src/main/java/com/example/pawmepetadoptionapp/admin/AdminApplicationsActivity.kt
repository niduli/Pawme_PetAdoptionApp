package com.example.pawmepetadoptionapp.admin


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawmepetadoptionapp.admin.adapter.ApplicationAdapter
import com.example.pawmepetadoptionapp.admin.model.Application
import com.example.pawmepetadoptionapp.databinding.ActivityAdminApplicationsBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminApplicationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminApplicationsBinding
    private lateinit var adapter: ApplicationAdapter
    private val applicationList = mutableListOf<Application>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminApplicationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ApplicationAdapter(applicationList,
            onApprove = { application -> updateApplicationStatus(application, true) },
            onReject = { application -> updateApplicationStatus(application, false) }
        )

        binding.recyclerViewApplications.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewApplications.adapter = adapter

        fetchPendingApplications()
    }

    private fun fetchPendingApplications() {
        db.collection("applications")
            .whereEqualTo("status", "pending")
            .get()
            .addOnSuccessListener { documents ->
                applicationList.clear()
                for (doc in documents) {
                    val app = doc.toObject(Application::class.java).copy(applicationId = doc.id)
                    applicationList.add(app)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load applications", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateApplicationStatus(application: Application, approved: Boolean) {
        val appRef = db.collection("applications").document(application.applicationId)
        val taskRef = db.collection("tasks").document(application.taskId)
        val schedulesRef = db.collection("schedules")

        val batch = db.batch()

        if (approved) {
            batch.update(appRef, "status", "accepted")
            batch.update(taskRef, mapOf("status" to "assigned", "assignedTo" to application.volunteerId))
            val scheduleData = hashMapOf(
                "userId" to application.volunteerId,
                "name" to "", // Optional: Fetch task name for better UX
                "time" to "", // Admin can add schedule time here or via separate UI
                "location" to "",
                "status" to "Upcoming"
            )
            val newScheduleRef = schedulesRef.document()
            batch.set(newScheduleRef, scheduleData)
        } else {
            batch.update(appRef, "status", "rejected")
        }

        batch.commit()
            .addOnSuccessListener {
                Toast.makeText(this, "Application ${if (approved) "approved" else "rejected"}", Toast.LENGTH_SHORT).show()
                fetchPendingApplications()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update application status", Toast.LENGTH_SHORT).show()
            }
    }
}
