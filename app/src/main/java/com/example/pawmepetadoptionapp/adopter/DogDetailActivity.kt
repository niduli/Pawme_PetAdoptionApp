package com.example.pawmepetadoptionapp.adopter

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pawmepetadoptionapp.R

class DogDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog_detail)

        val backButton: ImageButton = findViewById(R.id.backButton)
        val dogImage: ImageView = findViewById(R.id.dogImage)
        val dogName: TextView = findViewById(R.id.dogName)
        val dogBreed: TextView = findViewById(R.id.dogBreed)
        val dogNeeds: TextView = findViewById(R.id.dogNeeds)
        val dogAge: TextView = findViewById(R.id.dogAge)
        val description: TextView = findViewById(R.id.dogDescription)
        val vaccinationBtn: Button = findViewById(R.id.vaccinationBtn)
        val adoptBtn: Button = findViewById(R.id.adoptBtn)

        // Expect both dogId (document id) and name from the previous screen
        val dogId = intent.getStringExtra("dogId") ?: ""   // IMPORTANT for subcollection path
        val name = intent.getStringExtra("name") ?: "Doggo"
        val age = intent.getStringExtra("age") ?: "Unknown"
        val breed = intent.getStringExtra("Breed") ?: "Unknown"
        val needs = intent.getStringExtra("needs") ?: "None"
        val imageResId = intent.getIntExtra("imageResId", -1)
        val imageUrl = intent.getStringExtra("imageUrl")

        dogName.text = name
        dogBreed.text = breed
        dogAge.text = age
        dogNeeds.text = needs
        description.text = "$name is a loving and loyal companion looking for a forever home."

        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_profile_placeholder)
                .centerCrop()
                .into(dogImage)
        } else if (imageResId != -1) {
            dogImage.setImageResource(imageResId)
        }

        backButton.setOnClickListener { finish() }

        vaccinationBtn.setOnClickListener {
            val intent = Intent(this, VaccinationHistoryActivity::class.java)
            intent.putExtra("dogId", dogId)
            intent.putExtra("dogName", name) // optional, for display
            startActivity(intent)
        }

        adoptBtn.setOnClickListener {
            val intent = Intent(this, AdoptFormActivity::class.java)
            intent.putExtra("dogName", name)
            startActivity(intent)
        }
    }
}