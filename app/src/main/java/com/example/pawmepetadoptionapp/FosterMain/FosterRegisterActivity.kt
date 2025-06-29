package com.example.pawmepetadoptionapp.FosterMain

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.pawmepetadoptionapp.R
import com.google.android.material.textfield.TextInputEditText

class FosterRegisterActivity : AppCompatActivity() {
    private lateinit var fullNameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var phoneInput: TextInputEditText

    private lateinit var spinnerHomeType: Spinner
    private lateinit var spinnerPetExperience: Spinner
    private lateinit var spinnerFosterDuration: Spinner
    private lateinit var spinnerPetsAtHome: Spinner

    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foster_register)

        // Initialize views
        fullNameInput = findViewById(R.id.etFullName)
        emailInput = findViewById(R.id.etEmail)
        passwordInput = findViewById(R.id.etPassword)
        phoneInput = findViewById(R.id.etPhoneNumber)

        spinnerHomeType = findViewById(R.id.spinnerHomeType)
        spinnerPetExperience = findViewById(R.id.spinnerPetExperience)
        spinnerFosterDuration = findViewById(R.id.spinnerFosterDuration)
        spinnerPetsAtHome = findViewById(R.id.spinnerPetsAtHome)

        submitButton = findViewById(R.id.btnSubmit)

        // Setup Spinners
        setupSpinners()

        // Handle Submit Button Click
        submitButton.setOnClickListener {
            submitForm()
        }
    }

    private fun setupSpinners() {
        val homeTypeOptions = listOf("Apartment", "House with Yard")
        val petExperienceOptions = listOf("Beginner", "Intermediate", "Expert")
        val fosterDurationOptions = listOf("Short-term", "Long-term", "Emergency")
        val petsAtHomeOptions = listOf("No", "Yes (Type & Number)")

        spinnerHomeType.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, homeTypeOptions)
        spinnerPetExperience.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, petExperienceOptions)
        spinnerFosterDuration.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, fosterDurationOptions)
        spinnerPetsAtHome.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, petsAtHomeOptions)
    }

    private fun submitForm() {
        val name = fullNameInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()
        val phone = phoneInput.text.toString().trim()
        val homeType = spinnerHomeType.selectedItem.toString()
        val petExperience = spinnerPetExperience.selectedItem.toString()
        val fosterDuration = spinnerFosterDuration.selectedItem.toString()
        val petsAtHome = spinnerPetsAtHome.selectedItem.toString()

        // Basic validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Show success message
        Toast.makeText(this, "Registered Successfully!", Toast.LENGTH_LONG).show()

        // Optional: Clear fields after submission
        fullNameInput.text?.clear()
        emailInput.text?.clear()
        passwordInput.text?.clear()
        phoneInput.text?.clear()
        spinnerHomeType.setSelection(0)
        spinnerPetExperience.setSelection(0)
        spinnerFosterDuration.setSelection(0)
        spinnerPetsAtHome.setSelection(0)
    }


}