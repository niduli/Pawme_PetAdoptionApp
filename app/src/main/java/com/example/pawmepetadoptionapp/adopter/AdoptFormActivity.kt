package com.example.pawmepetadoptionapp.adopter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pawmepetadoptionapp.R
import com.google.firebase.firestore.FirebaseFirestore

class AdoptFormActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adopt_form)

        val dogName = intent.getStringExtra("dogName")

        val backButton = findViewById<ImageButton>(R.id.backButton)
        val nameField = findViewById<EditText>(R.id.etName)
        val phoneField = findViewById<EditText>(R.id.etPhone)
        val addressField = findViewById<EditText>(R.id.etAddress)
        val submitButton = findViewById<Button>(R.id.btnSubmit)

        val checkboxYes = findViewById<CheckBox>(R.id.checkboxYes)
        val checkboxNo = findViewById<CheckBox>(R.id.checkboxNo)

        // Optional: Make them exclusive like radio buttons
        checkboxYes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) checkboxNo.isChecked = false
        }
        checkboxNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) checkboxYes.isChecked = false
        }
        backButton.setOnClickListener { finish() }

        submitButton.setOnClickListener {
            val userName = nameField.text.toString()
            val phone = phoneField.text.toString()
            val address = addressField.text.toString()
            val hasOtherPets = checkboxYes.isChecked

            if (userName.isBlank() || phone.isBlank() || address.isBlank()) {
                Toast.makeText(this, "Please fill out all required fields", Toast.LENGTH_SHORT).show()
            } else if (dogName.isNullOrBlank()) {
                Toast.makeText(this, "Dog information is missing.", Toast.LENGTH_SHORT).show()
            } else {
                // Firestore logic
                val db = FirebaseFirestore.getInstance()
                // Find the dog by name (assumes 'name' is unique, adjust if you have dogId)
                db.collection("dogs")
                    .whereEqualTo("name", dogName)
                    .limit(1)
                    .get()
                    .addOnSuccessListener { result ->
                        if (!result.isEmpty) {
                            val dogDoc = result.documents[0].reference
                            // 1. Set available to false
                            dogDoc.update("available", false)
                                .addOnSuccessListener {
                                    // 2. Create adoption_requests subcollection
                                    val request = hashMapOf(
                                        "userName" to userName,
                                        "phone" to phone,
                                        "address" to address,
                                        "hasOtherPets" to hasOtherPets,
                                        "timestamp" to com.google.firebase.Timestamp.now()
                                    )
                                    dogDoc.collection("adoption_requests")
                                        .add(request)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                this,
                                                "Thank you $userName! Your adoption request for $dogName is submitted.",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            // Redirect to AdoptionActivity
                                            val intent = Intent(this, AdoptionActivity::class.java)
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                            startActivity(intent)
                                            finish()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(
                                                this,
                                                "Failed to submit adoption request: ${e.message}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        this,
                                        "Failed to update dog availability: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        } else {
                            Toast.makeText(
                                this,
                                "Dog not found in database.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            this,
                            "Error searching for dog: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }
        }
    }
}