package com.example.pawmepetadoptionapp.adopter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pawmepetadoptionapp.R
import com.example.pawmepetadoptionapp.SignInActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdopterProfileActivity : AppCompatActivity() {

    private lateinit var tvUsername: TextView
    private lateinit var tvEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adopter_profile)

        tvUsername = findViewById(R.id.tvUsername)
        tvEmail = findViewById(R.id.tvEmail)

        val currentUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUid != null) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(currentUid)
                .get()
                .addOnSuccessListener { document ->
                    val user = document.toObject(User::class.java)
                    if (user != null && user.role == "Adopter") {
                        tvUsername.text = "Username: ${user.username}"
                        tvEmail.text = "Email: ${user.email}"
                        tvUsername.visibility = View.VISIBLE
                        tvEmail.visibility = View.VISIBLE
                    } else {
                        tvUsername.visibility = View.GONE
                        tvEmail.visibility = View.GONE
                        Toast.makeText(this, "You are not an adopter", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AdoptionActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AdopterProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_logout ->{
                    Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}

class User() {
    var username: String? = null
    var email: String? = null
    var role: String? = null

    constructor(username: String, email: String, role: String) : this() {
        this.username = username
        this.email = email
        this.role = role
    }
}