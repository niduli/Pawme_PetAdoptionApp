package com.example.pawmepetadoptionapp.adopter

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pawmepetadoptionapp.R

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

        backButton.setOnClickListener {
            finish()
        }

        submitButton.setOnClickListener {
            val userName = nameField.text.toString()
            val phone = phoneField.text.toString()
            val address = addressField.text.toString()

            if (userName.isBlank() || phone.isBlank() || address.isBlank()) {
                Toast.makeText(this, "Please fill out all required fields", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    this,
                    "Thank you $userName! Your adoption request for $dogName is submitted.",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
    }
}
