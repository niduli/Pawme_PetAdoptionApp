package com.example.pawmepetadoptionapp.admin

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pawmepetadoptionapp.R
import com.google.android.material.card.MaterialCardView

class ActivityAdminDashboard : AppCompatActivity() {

    private lateinit var tvDonationsCount: TextView
    private lateinit var tvAdoptionsCount: TextView
    private lateinit var tvFostersCount: TextView
    private lateinit var tvVolunteersCount: TextView
    private lateinit var tvVaccinationsCount: TextView
    private lateinit var tvUsersCount: TextView

    private lateinit var cardDonations: MaterialCardView
    private lateinit var cardAdoptions: MaterialCardView
    private lateinit var cardFosters: MaterialCardView
    private lateinit var cardVolunteers: MaterialCardView
    private lateinit var cardVaccinations: MaterialCardView
    private lateinit var cardUsers: MaterialCardView

    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_dashboard)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize TextViews
        tvDonationsCount = findViewById(R.id.tvDonationsCount)
        tvAdoptionsCount = findViewById(R.id.tvAdoptionsCount)
        tvFostersCount = findViewById(R.id.tvFostersCount)
        tvVolunteersCount = findViewById(R.id.tvVolunteersCount)
        tvVaccinationsCount = findViewById(R.id.tvVaccinationsCount)
        tvUsersCount = findViewById(R.id.tvUsersCount)

        // Set example static data (replace with dynamic values later)
        tvDonationsCount.text = "10"
        tvAdoptionsCount.text = "5"
        tvFostersCount.text = "3"
        tvVolunteersCount.text = "8"
        tvVaccinationsCount.text = "6"
        tvUsersCount.text = "20"

        // Initialize cards
        cardDonations = findViewById(R.id.cardDonations)
        cardAdoptions = findViewById(R.id.cardAdoptions)
        cardFosters = findViewById(R.id.cardFosters)
        cardVolunteers = findViewById(R.id.cardVolunteers)
        cardVaccinations = findViewById(R.id.cardVaccinations)
        cardUsers = findViewById(R.id.cardUsers)

        // Optional: Set click listeners for cards
        cardDonations.setOnClickListener {
            // startActivity(Intent(this, DonationManagementActivity::class.java))
        }

        cardAdoptions.setOnClickListener {
            // startActivity(Intent(this, AdoptionManagementActivity::class.java))
        }

        cardFosters.setOnClickListener {
            // startActivity(Intent(this, FosterManagementActivity::class.java))
        }

        cardVolunteers.setOnClickListener {
            // startActivity(Intent(this, VolunteerManagementActivity::class.java))
        }

        cardVaccinations.setOnClickListener {
            // startActivity(Intent(this, VaccinationManagementActivity::class.java))
        }

        cardUsers.setOnClickListener {
            // startActivity(Intent(this, UserManagementActivity::class.java))
        }

        // Logout button
        btnLogout = findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener {
            // FirebaseAuth.getInstance().signOut()
            // startActivity(Intent(this, LoginActivity::class.java))
            // finish()
        }
    }
}
