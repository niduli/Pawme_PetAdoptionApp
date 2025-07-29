package com.example.pawmepetadoptionapp.adopter

import android.content.Intent
import android.os.Bundle
import android.widget.*
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

        val name = intent.getStringExtra("name") ?: "Doggo"
        val ageBreed = intent.getStringExtra("ageBreed") ?: "Unknown"
        val imageResId = intent.getIntExtra("imageResId", -1)

        dogName.text = "Name: $name"
        dogBreed.text = "Breed: $ageBreed"
        description.text = "$name is a loving and loyal companion looking for a forever home. $ageBreed and full of joy!"

        if (imageResId != -1) {
            dogImage.setImageResource(imageResId)
        }

        backButton.setOnClickListener {
            finish()
        }

        vaccinationBtn.setOnClickListener {
            val intent = Intent(this, VaccinationHistoryActivity::class.java)
            startActivity(intent)
        }

        adoptBtn.setOnClickListener {
            val intent = Intent(this, AdoptFormActivity::class.java)
            intent.putExtra("dogName", name)
            startActivity(intent)
        }
    }
}
