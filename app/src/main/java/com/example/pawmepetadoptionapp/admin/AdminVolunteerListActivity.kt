package com.example.pawmepetadoptionapp.admin


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawmepetadoptionapp.admin.adapter.VolunteerAdapter
import com.example.pawmepetadoptionapp.admin.model.User
import com.example.pawmepetadoptionapp.databinding.ActivityAdminVolunteerListBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminVolunteerListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminVolunteerListBinding
    private lateinit var adapter: VolunteerAdapter
    private val volunteerList = mutableListOf<User>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminVolunteerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = VolunteerAdapter(volunteerList) { volunteer ->
            Toast.makeText(this, "Selected: ${volunteer.username}", Toast.LENGTH_SHORT).show()
            // You can add navigation to task assignment screen here if needed
        }

        binding.recyclerViewVolunteers.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewVolunteers.adapter = adapter

        fetchVolunteers()
    }

    private fun fetchVolunteers() {
        db.collection("users")
            .whereEqualTo("role", "Volunteer")
            .get()
            .addOnSuccessListener { documents ->
                volunteerList.clear()
                for (doc in documents) {
                    val user = doc.toObject(User::class.java)
                    volunteerList.add(user)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load volunteers", Toast.LENGTH_SHORT).show()
            }
    }
}
