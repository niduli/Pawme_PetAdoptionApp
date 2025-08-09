package com.example.pawmepetadoptionapp.adopter

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.pawmepetadoptionapp.R
import com.google.firebase.Timestamp
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
        val userExperience = findViewById<EditText>(R.id.etExperience)
        val additionalInfo = findViewById<EditText>(R.id.etNotes)

        val checkboxYes = findViewById<CheckBox>(R.id.checkboxYes)
        val checkboxNo = findViewById<CheckBox>(R.id.checkboxNo)

        // Exclusive checkbox logic + enable/disable experience field
        checkboxYes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkboxNo.isChecked = false
                userExperience.isEnabled = true
            } else if (!checkboxNo.isChecked) {
                userExperience.isEnabled = false
                userExperience.text.clear()
            }
        }
        checkboxNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkboxYes.isChecked = false
                userExperience.isEnabled = false
                userExperience.text.clear()
            } else if (!checkboxYes.isChecked) {
                userExperience.isEnabled = false
            }
        }

        backButton.setOnClickListener { finish() }

        submitButton.setOnClickListener {
            val userName = nameField.text.toString().trim()
            val phone = phoneField.text.toString().trim()
            val address = addressField.text.toString().trim()
            val hasOtherPets = checkboxYes.isChecked
            val experience = if (hasOtherPets) userExperience.text.toString().trim() else ""
            val additionalInformation = additionalInfo.text.toString().trim()

            // Validation
            if (userName.isBlank() || phone.isBlank() || address.isBlank()) {
                Toast.makeText(this, "Please fill out all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (dogName.isNullOrBlank()) {
                Toast.makeText(this, "Dog information is missing.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (hasOtherPets && experience.isBlank()) {
                Toast.makeText(this, "Please add the experience years", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firestore logic
            val db = FirebaseFirestore.getInstance()
            db.collection("dogs")
                .whereEqualTo("name", dogName)
                .limit(1)
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty) {
                        val dogDoc = result.documents[0].reference

                        // Set available to false
                        dogDoc.update("available", false)
                            .addOnSuccessListener {
                                val request = hashMapOf(
                                    "userName" to userName,
                                    "phone" to phone,
                                    "address" to address,
                                    "hasOtherPets" to hasOtherPets,
                                    "experience" to experience,
                                    "additionalInformation" to additionalInformation,
                                    "status" to "pending",
                                    "dogName" to dogName,
                                    "timestamp" to Timestamp.now()

                                )

                                dogDoc.collection("adoption_requests")
                                    .add(request)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            this,
                                            "Thank you $userName! Your adoption request for $dogName is submitted.",
                                            Toast.LENGTH_LONG
                                        ).show()

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
                        Toast.makeText(this, "Dog not found in database.", Toast.LENGTH_LONG).show()
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
