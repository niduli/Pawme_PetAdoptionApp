package com.example.pawmepetadoptionapp.adopter

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.pawmepetadoptionapp.R

class DogDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog_detail)

        val backButton: ImageButton = findViewById(R.id.backButton)
        val dogImage: ImageView = findViewById(R.id.dogImage)
        val dogName: TextView = findViewById(R.id.dogName)
        val dogBreed: TextView = findViewById(R.id.dogBreed)
        val description: TextView = findViewById(R.id.dogDescription)
        val vaccinationBtn: Button = findViewById(R.id.vaccinationBtn)
        val adoptBtn: Button = findViewById(R.id.adoptBtn)

        // Get data from Intent
        val name = intent.getStringExtra("name")
        val ageBreed = intent.getStringExtra("ageBreed")
        val imageResId = intent.getIntExtra("imageResId", -1)

        // Set values to views
        dogName.text = name
        dogBreed.text = ageBreed
        if (imageResId != -1) {
            dogImage.setImageResource(imageResId)
        }
        description.text = "$name is a loving and loyal companion looking for a forever home. $ageBreed and full of joy!"

        backButton.setOnClickListener {
            finish()
        }

        vaccinationBtn.setOnClickListener {
            // Future feature: Show vaccination history
        }

        adoptBtn.setOnClickListener {
            // Future feature: Handle adoption
        }
    }
}
