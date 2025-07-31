package com.example.pawmepetadoptionapp

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.pawmepetadoptionapp.FosterMain.FosterMainActivity
import com.example.pawmepetadoptionapp.admin.ActivityAdminDashboard
import com.example.pawmepetadoptionapp.adopter.AdoptionActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignInActivity : AppCompatActivity() {

    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var rememberCheck: CheckBox
    private lateinit var signInBtn: Button
    private lateinit var signUpText: TextView
    private lateinit var forgotPasswordText: TextView

    private lateinit var auth: FirebaseAuth
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

        auth = FirebaseAuth.getInstance()

        // Toggle password visibility
        passwordField.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEndIndex = 2
                val drawableEnd = passwordField.compoundDrawables[drawableEndIndex]
                if (drawableEnd != null) {
                    val bounds = drawableEnd.bounds
                    if (event.rawX >= (passwordField.right - bounds.width())) {
                        passwordVisible = !passwordVisible
                        togglePasswordVisibility()
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        // Sign In button click
        signInBtn.setOnClickListener { view ->
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Snackbar.make(view, "Please enter both email and password", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val uid = auth.currentUser?.uid
                    if (uid != null) {
                        val db = FirebaseFirestore.getInstance()
                        db.collection("users").document(uid).get()
                            .addOnSuccessListener { document ->
                                if (document != null && document.exists()) {
                                    val role = document.getString("role")

                                    when (role) {
                                        "Admin" -> startActivity(Intent(this, ActivityAdminDashboard::class.java))
                                        "Volunteer" -> startActivity(Intent(this, VolunteerDashboardActivity::class.java))
                                        "Adopter" -> startActivity(Intent(this, AdoptionActivity::class.java))
                                        "Foster" -> startActivity(Intent(this, FosterMainActivity::class.java))
                                        "Donor" -> startActivity(Intent(this, DonationActivity::class.java))
                                        else -> {
                                            Snackbar.make(view, "No dashboard defined for role: $role", Snackbar.LENGTH_LONG).show()
                                            return@addOnSuccessListener
                                        }
                                    }

                                    finish()
                                } else {
                                    Snackbar.make(view, "User role not found", Snackbar.LENGTH_LONG).show()
                                }
                            }
                            .addOnFailureListener { e ->
                                Snackbar.make(view, "Error fetching user data: ${e.message}", Snackbar.LENGTH_LONG).show()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Snackbar.make(view, "Login failed: ${e.message}", Snackbar.LENGTH_LONG).show()
                }
        }

        // Sign Up navigation
        signUpText.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Forgot Password
        forgotPasswordText.setOnClickListener {
            val email = emailField.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Enter email to reset password", Toast.LENGTH_SHORT).show()
            } else {
                auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Reset link sent to your email", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun togglePasswordVisibility() {
        if (passwordVisible) {
            passwordField.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            passwordField.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(this, R.drawable.padlocksquare),
                null,
                ContextCompat.getDrawable(this, R.drawable.visible),
                null
            )
        } else {
            passwordField.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            passwordField.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(this, R.drawable.padlocksquare),
                null,
                ContextCompat.getDrawable(this, R.drawable.visibility_off),
                null
            )
        }
        passwordField.setSelection(passwordField.text.length)
    }
}
