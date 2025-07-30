package com.example.pawmepetadoptionapp

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private lateinit var usernameField: EditText
    private lateinit var emailField: EditText
    private lateinit var roleField: AutoCompleteTextView
    private lateinit var passwordField: EditText
    private lateinit var confirmPasswordField: EditText
    private lateinit var signUpBtn: Button
    private lateinit var signInText: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore


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

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val roles = listOf("Adopter", "Foster", "Volunteer", "Donor")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles)
        roleField.setAdapter(adapter)

        // Toggle visibility for password
        setupPasswordToggle(passwordField, R.drawable.padlocksquare)
        setupPasswordToggle(confirmPasswordField, R.drawable.padlocksquare)

        signUpBtn.setOnClickListener {
            signUpUser()
        }

        signInText.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }

    private fun signUpUser() {
        val username = usernameField.text.toString().trim()
        val email = emailField.text.toString().trim()
        val role = roleField.text.toString().trim()
        val password = passwordField.text.toString()
        val confirmPassword = confirmPasswordField.text.toString()

        if (username.isEmpty() || email.isEmpty() || role.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val roles = listOf("Adopter", "Foster", "Volunteer", "Donor")
        if (!roles.contains(role)) {
            Toast.makeText(this, "Please select a valid role", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    val user = hashMapOf(
                        "uid" to uid,
                        "username" to username,
                        "email" to email,
                        "role" to role
                    )

                    if (uid != null) {
                        firestore.collection("users").document(uid)
                            .set(user)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Sign-up successful!", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, SignInActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to save user: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "Sign-up failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun setupPasswordToggle(editText: EditText, iconStart: Int) {
        var isVisible = false

        editText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2
                val drawable = editText.compoundDrawables[drawableEnd]
                if (drawable != null) {
                    val bounds = drawable.bounds
                    if (event.rawX >= (editText.right - bounds.width())) {
                        isVisible = !isVisible

                        // Set input type and icon
                        editText.inputType = if (isVisible) {
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        } else {
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        }

                        editText.setCompoundDrawablesWithIntrinsicBounds(
                            ContextCompat.getDrawable(this, iconStart),
                            null,
                            ContextCompat.getDrawable(
                                this,
                                if (isVisible) R.drawable.visible else R.drawable.visibility_off
                            ),
                            null
                        )

                        editText.setSelection(editText.text.length)
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }
    }

}
