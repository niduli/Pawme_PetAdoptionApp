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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            Log.e("User ID", "Logged in User ID: $uid")
        } else {
            Log.e("User ID", "No user is logged in")
        }



        // Initialize count text views
        tvUsersCount = findViewById(R.id.tvUsersCount)
        tvDogsCount = findViewById(R.id.tvDogsCount)
        tvDonationsCount = findViewById(R.id.tvDonationsCount)
        tvVolunteersCount = findViewById(R.id.tvVolunteersCount)
        tvFostersCount = findViewById(R.id.tvFostersCount)
        tvAdoptersCount = findViewById(R.id.tvAdoptersCount)
        tvVaccinationsCount = findViewById(R.id.tvVaccinationsCount)

        // Set static example data (you can fetch actual Firestore data here)
        tvUsersCount.text = "20"
        tvDogsCount.text = "12"
        tvDonationsCount.text = "10"
        tvVolunteersCount.text = "8"
        tvFostersCount.text = "3"
        tvAdoptersCount.text = "5"
        tvVaccinationsCount.text = "7"

        // Initialize cards
        cardUsers = findViewById(R.id.cardUsers)
        cardDogs = findViewById(R.id.cardDogs)
        cardDonations = findViewById(R.id.cardDonations)
        cardVolunteers = findViewById(R.id.cardVolunteers)
        cardFosters = findViewById(R.id.cardFosters)
        cardAdopters = findViewById(R.id.cardAdopters)
        cardVaccinations = findViewById(R.id.cardVaccinations)

        // Set click listeners
        cardUsers.setOnClickListener {
            // Navigate to user management screen
            startActivity(Intent(this, UserManagementActivity::class.java))
        }

        cardDogs.setOnClickListener {
             startActivity(Intent(this, DogManagementActivity::class.java))
        }

        cardDonations.setOnClickListener {
            // startActivity(Intent(this, DonationManagementActivity::class.java))
        }

        cardVolunteers.setOnClickListener {
            // startActivity(Intent(this, VolunteerManagementActivity::class.java))
        }

        cardFosters.setOnClickListener {
            // startActivity(Intent(this, FosterManagementActivity::class.java))
        }

        cardAdopters.setOnClickListener {
            // startActivity(Intent(this, AdopterManagementActivity::class.java))
        }
        cardVaccinations.setOnClickListener {
            // Navigate to vaccination management screen
            // startActivity(Intent(this, VaccinationManagementActivity::class.java))
        }


        // Optional: Logout button setup (if added in XML)
        // btnLogout = findViewById(R.id.btnLogout)
        // btnLogout.setOnClickListener {
        //     FirebaseAuth.getInstance().signOut()
        //     startActivity(Intent(this, LoginActivity::class.java))
        //     finish()
        // }
    }
}
