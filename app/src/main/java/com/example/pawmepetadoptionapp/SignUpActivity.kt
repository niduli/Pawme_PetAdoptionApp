package com.example.pawmepetadoptionapp

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.*
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
        setContentView(R.layout.activity_sign_up)

        usernameField = findViewById(R.id.usernameField)
        emailField = findViewById(R.id.emailField)
        roleField = findViewById(R.id.roleField)
        passwordField = findViewById(R.id.passwordField)
        confirmPasswordField = findViewById(R.id.confirmPasswordField)
        signUpBtn = findViewById(R.id.signUpBtn)
        signInText = findViewById(R.id.signInText)

        val roles = listOf("Adopter", "Foster", "Volunteer", "Donor")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles)
        roleField.setAdapter(adapter)

        passwordField.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2
                passwordField.compoundDrawables[drawableEnd]?.let { drawable ->
                    val bounds = drawable.bounds
                    if (event.rawX >= (passwordField.right - bounds.width())) {
                        passwordVisible = !passwordVisible
                        togglePasswordVisibility(passwordField, passwordVisible, R.drawable.lock1)
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        confirmPasswordField.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2
                confirmPasswordField.compoundDrawables[drawableEnd]?.let { drawable ->
                    val bounds = drawable.bounds
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

            // TODO: Firebase Sign-Up Logic here

            Toast.makeText(this, "Sign-up successful!", Toast.LENGTH_SHORT).show()

            // Redirect to SignInActivity after successful signup
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        signInText.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun togglePasswordVisibility(editText: EditText, isVisible: Boolean, lockDrawableId: Int) {
        if (isVisible) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            editText.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(this, lockDrawableId),
                null,
                ContextCompat.getDrawable(this, R.drawable.visible),
                null
            )
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            editText.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(this, lockDrawableId),
                null,
                ContextCompat.getDrawable(this, R.drawable.visibility_off),
                null
            )
        }
        editText.setSelection(editText.text.length)
    }
}
