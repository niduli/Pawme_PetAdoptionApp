package com.example.pawmepetadoptionapp.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.pawmepetadoptionapp.R
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ActivityAdminDashboard : AppCompatActivity() {

    private lateinit var tvUsersCount: TextView
    private lateinit var tvDogsCount: TextView
    private lateinit var tvDonationsCount: TextView
    private lateinit var tvVolunteersCount: TextView
    private lateinit var tvFostersCount: TextView
    private lateinit var tvAdoptersCount: TextView
    private lateinit var tvVaccinationsCount: TextView

    private lateinit var cardUsers: MaterialCardView
    private lateinit var cardDogs: MaterialCardView
    private lateinit var cardDonations: MaterialCardView
    private lateinit var cardVolunteers: MaterialCardView
    private lateinit var cardFosters: MaterialCardView
    private lateinit var cardAdopters: MaterialCardView
    private lateinit var cardVaccinations: MaterialCardView

    private lateinit var btnLogout: Button
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        // Firebase Authentication - Check logged-in user
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            Log.e("User ID", "Logged in User ID: $uid")
        } else {
            Log.e("User ID", "No user is logged in")
        }

        // Firestore instance
        db = FirebaseFirestore.getInstance()

        // Initialize count TextViews
        tvUsersCount = findViewById(R.id.tvUsersCount)
        tvDogsCount = findViewById(R.id.tvDogsCount)
        tvDonationsCount = findViewById(R.id.tvDonationsCount)
        tvVolunteersCount = findViewById(R.id.tvVolunteersCount)
        tvFostersCount = findViewById(R.id.tvFostersCount)
        tvAdoptersCount = findViewById(R.id.tvAdoptersCount)
        tvVaccinationsCount = findViewById(R.id.tvVaccinationsCount)

        // Initialize cards
        cardUsers = findViewById(R.id.cardUsers)
        cardDogs = findViewById(R.id.cardDogs)
        cardDonations = findViewById(R.id.cardDonations)
        cardVolunteers = findViewById(R.id.cardVolunteers)
        cardFosters = findViewById(R.id.cardFosters)
        cardAdopters = findViewById(R.id.cardAdopters)
        cardVaccinations = findViewById(R.id.cardVaccinations)

        // Fetch counts from Firestore
        fetchCollectionCount("users", tvUsersCount)
        fetchCollectionCount("dogs", tvDogsCount)
        fetchCollectionCount("donations", tvDonationsCount)
        fetchCollectionCount("volunteers", tvVolunteersCount)
        fetchCollectionCount("fosters", tvFostersCount)
        fetchCollectionCount("adopters", tvAdoptersCount)
        fetchCollectionCount("vaccinations", tvVaccinationsCount)

        // Set click listeners for navigation
        cardUsers.setOnClickListener {
            startActivity(Intent(this, UserManagementActivity::class.java))
        }

        cardDogs.setOnClickListener {
            startActivity(Intent(this, DogManagementActivity::class.java))
        }

        cardDonations.setOnClickListener {
            // startActivity(Intent(this, DonationManagementActivity::class.java))
        }

        cardVolunteers.setOnClickListener {
             startActivity(Intent(this, AdminVolunteerListActivity::class.java))
        }

        cardFosters.setOnClickListener {
            // startActivity(Intent(this, FosterManagementActivity::class.java))
        }

        cardAdopters.setOnClickListener {
             startActivity(Intent(this, AdoptersManagementActivity::class.java))
        }

        cardVaccinations.setOnClickListener {
            startActivity(Intent(this, ActivityVaccination::class.java))
        }

        // Optional: Logout functionality
        // btnLogout = findViewById(R.id.btnLogout)
        // btnLogout.setOnClickListener {
        //     FirebaseAuth.getInstance().signOut()
        //     startActivity(Intent(this, LoginActivity::class.java))
        //     finish()
        // }
    }


    private fun fetchCollectionCount(collectionName: String, textView: TextView) {
        db.collection(collectionName)
            .get()
            .addOnSuccessListener { documents ->
                textView.text = documents.size().toString()
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Error fetching count for $collectionName", e)
                textView.text = "0"
            }
    }
}
