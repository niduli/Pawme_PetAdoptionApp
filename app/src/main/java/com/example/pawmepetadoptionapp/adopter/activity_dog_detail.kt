package com.example.dogadoptionapp

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.example.pawmepetadoptionapp.R

class DogDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog_detail)

        // Set views
        val backButton: ImageButton = findViewById(R.id.backButton)
        val dogImage: ImageView = findViewById(R.id.dogImage)
        val dogName: TextView = findViewById(R.id.dogName)
        val dogBreed: TextView = findViewById(R.id.dogBreed)
        val description: TextView = findViewById(R.id.dogDescription)
        val vaccinationBtn: Button = findViewById(R.id.vaccinationBtn)
        val adoptBtn: Button = findViewById(R.id.adoptBtn)

        // Back button click
        backButton.setOnClickListener {
            finish()
        }

        // Example: set data manually (later connect to real data or intent extras)
        dogName.text = "Shaggy"
        dogBreed.text = "Cocker Spaniel"
        description.text = "Shaggy is a gentle Cocker Spaniel who loves walks, cuddles, and being your loyal shadow. She's looking for her forever home!"

        // TODO: Set image with dogImage.setImageResource(R.drawable.shaggy)


        vaccinationBtn.setOnClickListener {
            // Open vaccination history
        }

        adoptBtn.setOnClickListener {
            // Handle adopt action
        }
    }
}
