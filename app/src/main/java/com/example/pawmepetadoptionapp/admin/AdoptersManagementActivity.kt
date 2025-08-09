package com.example.pawmepetadoptionapp.admin


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmepetadoptionapp.R
import com.example.pawmepetadoptionapp.admin.adapter.AdoptionRequestAdapter
import com.example.pawmepetadoptionapp.admin.model.AdoptionRequest
import com.google.firebase.firestore.FirebaseFirestore

class AdoptersManagementActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdoptionRequestAdapter
    private val requests = mutableListOf<AdoptionRequest>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adopters_management)

        recyclerView = findViewById(R.id.recyclerViewAdoptionRequests)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AdoptionRequestAdapter(requests,
            onAccept = { request -> acceptRequest(request) },
            onReject = { request -> rejectRequest(request) }
        )
        recyclerView.adapter = adapter

        fetchRequests()
    }

    private fun fetchRequests() {
        db.collection("adoption_requests")
            .whereEqualTo("status", "pending")
            .get()
            .addOnSuccessListener { snapshot ->
                requests.clear()
                for (doc in snapshot.documents) {
                    val request = doc.toObject(AdoptionRequest::class.java)
                    if (request != null) {
                        request.id = doc.id
                        requests.add(request)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load requests.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun acceptRequest(request: AdoptionRequest) {
        val requestRef = db.collection("adoption_requests").document(request.id)
        val dogRef = db.collection("dogs").document(request.dogId)

        db.runBatch { batch ->
            batch.update(requestRef, "status", "accepted")
            batch.update(dogRef, "available", false)

            val notifRef = db.collection("notifications").document()
            val notifData = mapOf(
                "userId" to request.userId,
                "message" to "Your adoption request for ${request.dogName} has been accepted.",
                "timestamp" to com.google.firebase.Timestamp.now(),
                "read" to false
            )
            batch.set(notifRef, notifData)
        }.addOnSuccessListener {
            Toast.makeText(this, "Request accepted.", Toast.LENGTH_SHORT).show()
            fetchRequests()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to accept request.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun rejectRequest(request: AdoptionRequest) {
        val requestRef = db.collection("adoption_requests").document(request.id)

        requestRef.update("status", "rejected")
            .addOnSuccessListener {
                val notifRef = db.collection("notifications").document()
                val notifData = mapOf(
                    "userId" to request.userId,
                    "message" to "Your adoption request for ${request.dogName} has been rejected.",
                    "timestamp" to com.google.firebase.Timestamp.now(),
                    "read" to false
                )
                notifRef.set(notifData)

                Toast.makeText(this, "Request rejected.", Toast.LENGTH_SHORT).show()
                fetchRequests()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to reject request.", Toast.LENGTH_SHORT).show()
            }
    }
}
