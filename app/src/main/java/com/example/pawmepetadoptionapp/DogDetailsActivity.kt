package com.example.pawmepetadoptionapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DogDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog_details)

        // Get the dog name from previous screen
        val dogName = intent.getStringExtra("dogName")

        // Find TextView and Button
        val nameText = findViewById<TextView>(R.id.dogNameText)
        val fosterButton = findViewById<Button>(R.id.btnFoster)

        // Set the dog name
        nameText.text = "Dog Name: $dogName"

        // When button is clicked
        fosterButton.setOnClickListener {
            Toast.makeText(this, "You are fostering $dogName!", Toast.LENGTH_SHORT).show()
        }
    }
}
