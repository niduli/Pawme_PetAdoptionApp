package com.example.pawmepetadoptionapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DogProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.available_activity_dog_profile)

        val name = intent.getStringExtra("DOG_NAME")
        val breed = intent.getStringExtra("DOG_BREED")
        val age = intent.getStringExtra("DOG_AGE")
        val duration = intent.getStringExtra("DOG_DURATION")
        val needs = intent.getStringExtra("DOG_NEEDS")

        findViewById<TextView>(R.id.txtDogName).text = name
        findViewById<TextView>(R.id.txtDogDetails).text = "Breed: $breed\nAge: $age\nDuration: $duration\nSpecial Needs: $needs"
    }
}
