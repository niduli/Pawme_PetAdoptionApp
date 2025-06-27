package com.example.pawmepetadoptionapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class SignInActivity : AppCompatActivity() {

    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var rememberCheck: CheckBox
    private lateinit var signInBtn: Button
    private lateinit var signUpText: TextView
    private lateinit var forgotPasswordText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Bind views
        emailField = findViewById(R.id.emailField)
        passwordField = findViewById(R.id.passwordField)
        rememberCheck = findViewById(R.id.rememberCheck)
        signInBtn = findViewById(R.id.signInBtn)
        signUpText = findViewById(R.id.signUpText)
        forgotPasswordText = findViewById(R.id.forgotPasswordText)

        // Sign In button click
        signInBtn.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Snackbar.make(it, "Please enter both email and password", Snackbar.LENGTH_SHORT).show()
            } else {
                // TODO: Firebase sign-in logic here
                // Example: Go to HomeActivity
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // Sign Up redirect
        signUpText.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Forgot Password placeholder
        forgotPasswordText.setOnClickListener {
            Toast.makeText(this, "Forgot Password feature coming soon!", Toast.LENGTH_SHORT).show()
        }
    }
}
