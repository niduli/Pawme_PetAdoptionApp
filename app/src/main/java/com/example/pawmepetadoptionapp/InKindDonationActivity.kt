package com.example.pawmepetadoptionapp

import DonationActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class InKindDonationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_kind_donation)

        val backIcon = findViewById<ImageView>(R.id.backIcon)
        backIcon.setOnClickListener {
            val intent = Intent(this, DonationActivity::class.java)
            startActivity(intent)
            finish() // optional, prevents returning here on back press
        }


        // Footer icons - Example: you can set navigation here
        val homeIcon = findViewById<ImageView>(R.id.homeIcon)
        val profileIcon = findViewById<ImageView>(R.id.profileIcon)

        homeIcon.setOnClickListener {
            // Navigate to Home Activity
            Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show()
            // startActivity(Intent(this, HomeActivity::class.java))
        }

        profileIcon.setOnClickListener {
            // Navigate to Profile Activity
            Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
            // startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Donation buttons with click listeners
        val donateDogFood = findViewById<Button>(R.id.donateDogFood)
        val donateBoneToy = findViewById<Button>(R.id.donateBoneToy)
        val donatePawCage = findViewById<Button>(R.id.donatePawCage)
        val donateDogMat = findViewById<Button>(R.id.donateDogMat)

        donateDogFood.setOnClickListener {
            // Handle Dog Food donation button click
            Toast.makeText(this, "Donate Dog Food clicked", Toast.LENGTH_SHORT).show()
            // Example: Navigate to Dog Food donation details screen
            // val intent = Intent(this, DogFoodDonationActivity::class.java)
            // startActivity(intent)
        }

        donateBoneToy.setOnClickListener {
            // Handle Bone Toy donation button click
            Toast.makeText(this, "Donate Bone Toy clicked", Toast.LENGTH_SHORT).show()
            // val intent = Intent(this, BoneToyDonationActivity::class.java)
            // startActivity(intent)
        }

        donatePawCage.setOnClickListener {
            // Handle Paw Cage donation button click
            Toast.makeText(this, "Donate Paw Cage clicked", Toast.LENGTH_SHORT).show()
            // val intent = Intent(this, PawCageDonationActivity::class.java)
            // startActivity(intent)
        }

        donateDogMat.setOnClickListener {
            // Handle Dog Mat donation button click
            Toast.makeText(this, "Donate Dog Mat clicked", Toast.LENGTH_SHORT).show()
            // val intent = Intent(this, DogMatDonationActivity::class.java)
            // startActivity(intent)
        }
    }
}
