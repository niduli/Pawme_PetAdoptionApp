package com.example.pawmepetadoptionapp

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import androidx.core.content.ContextCompat

class SignInActivity : AppCompatActivity() {

    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var rememberCheck: CheckBox
    private lateinit var signInBtn: Button
    private lateinit var signUpText: TextView
    private lateinit var forgotPasswordText: TextView

    private var passwordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        emailField = findViewById(R.id.emailField)
        passwordField = findViewById(R.id.passwordField)
        rememberCheck = findViewById(R.id.rememberCheck)
        signInBtn = findViewById(R.id.signInBtn)
        signUpText = findViewById(R.id.signUpText)
        forgotPasswordText = findViewById(R.id.forgotPasswordText)

        // Password visibility toggle on drawableEnd (eye icon)
        passwordField.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEndIndex = 2 // drawableEnd is the 3rd drawable
                val drawableEnd = passwordField.compoundDrawables[drawableEndIndex]
                if (drawableEnd != null) {
                    val bounds = drawableEnd.bounds
                    // Check if touch is inside drawableEnd bounds
                    if (event.rawX >= (passwordField.right - bounds.width())) {
                        passwordVisible = !passwordVisible
                        togglePasswordVisibility()
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        signInBtn.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Snackbar.make(it, "Please enter both email and password", Snackbar.LENGTH_SHORT).show()
            } else {
                // TODO: Firebase sign-in logic here
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        signUpText.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        forgotPasswordText.setOnClickListener {
            Toast.makeText(this, "Forgot Password feature coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun togglePasswordVisibility() {
        if (passwordVisible) {
            passwordField.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            passwordField.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(this, R.drawable.mail), // your start icon (mail)
                null,
                ContextCompat.getDrawable(this, R.drawable.visible), // eye-open icon
                null
            )
        } else {
            passwordField.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            passwordField.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(this, R.drawable.mail),
                null,
                ContextCompat.getDrawable(this, R.drawable.visibility_off), // eye-closed icon
                null
            )
        }
        // Keep cursor at the end
        passwordField.setSelection(passwordField.text.length)
    }
}
