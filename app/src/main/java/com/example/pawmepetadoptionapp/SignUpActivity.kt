package com.example.pawmepetadoptionapp

import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class SignUpActivity : AppCompatActivity() {

    private lateinit var usernameField: EditText
    private lateinit var emailField: EditText
    private lateinit var roleField: AutoCompleteTextView
    private lateinit var passwordField: EditText
    private lateinit var confirmPasswordField: EditText
    private lateinit var signUpBtn: Button
    private lateinit var signInText: TextView

    private var passwordVisible = false
    private var confirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)  // Make sure your layout file is named activity_sign_up.xml

        usernameField = findViewById(R.id.usernameField)
        emailField = findViewById(R.id.emailField)
        roleField = findViewById(R.id.roleField)
        passwordField = findViewById(R.id.passwordField)
        confirmPasswordField = findViewById(R.id.confirmPasswordField)
        signUpBtn = findViewById(R.id.signUpBtn)
        signInText = findViewById(R.id.signInText)

        val roles = listOf("Admin", "Adopter", "Foster", "Volunteer", "Donor")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles)
        roleField.setAdapter(adapter)

        // Setup password visibility toggle for passwordField
        passwordField.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2
                if (passwordField.compoundDrawables[drawableEnd] != null) {
                    val bounds = passwordField.compoundDrawables[drawableEnd].bounds
                    if (event.rawX >= (passwordField.right - bounds.width())) {
                        passwordVisible = !passwordVisible
                        togglePasswordVisibility(passwordField, passwordVisible, R.drawable.lock1)
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        // Setup password visibility toggle for confirmPasswordField
        confirmPasswordField.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2
                if (confirmPasswordField.compoundDrawables[drawableEnd] != null) {
                    val bounds = confirmPasswordField.compoundDrawables[drawableEnd].bounds
                    if (event.rawX >= (confirmPasswordField.right - bounds.width())) {
                        confirmPasswordVisible = !confirmPasswordVisible
                        togglePasswordVisibility(confirmPasswordField, confirmPasswordVisible, R.drawable.lock2)
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        signUpBtn.setOnClickListener {
            val username = usernameField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val role = roleField.text.toString().trim()
            val password = passwordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()

            if (username.isEmpty() || email.isEmpty() || role.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!roles.contains(role)) {
                Toast.makeText(this, "Please select a valid role", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // TODO: Implement Firebase sign-up logic here

            Toast.makeText(this, "Sign-up successful!", Toast.LENGTH_SHORT).show()
        }

        signInText.setOnClickListener {
            // TODO: Implement navigation to SignInActivity
            Toast.makeText(this, "Redirecting to Sign In...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun togglePasswordVisibility(editText: EditText, isVisible: Boolean, lockDrawableId: Int) {
        if (isVisible) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            editText.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(this, lockDrawableId),  // left icon (lock)
                null,
                ContextCompat.getDrawable(this, R.drawable.visible),  // visible eye icon
                null
            )
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            editText.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(this, lockDrawableId),
                null,
                ContextCompat.getDrawable(this, R.drawable.visibility_off),  // hidden eye icon
                null
            )
        }
        // Keep cursor at the end of text
        editText.setSelection(editText.text.length)
    }
}
