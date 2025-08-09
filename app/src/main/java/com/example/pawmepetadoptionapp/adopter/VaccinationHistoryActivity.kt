package com.example.pawmepetadoptionapp.adopter

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pawmepetadoptionapp.R
import com.google.firebase.firestore.FirebaseFirestore

class VaccinationHistoryActivity : AppCompatActivity() {

    private lateinit var tabCompleted: TextView
    private lateinit var tabUpcoming: TextView
    private var dogId: String = ""
    private var dogName: String = ""
    private var selectedStatus: String = "completed"

    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccination_history)

        dogId = intent?.getStringExtra("dogId").orEmpty()
        dogName = intent?.getStringExtra("dogName").orEmpty()

        tabCompleted = findViewById(R.id.tabCompleted)
        tabUpcoming = findViewById(R.id.tabUpcoming)
        val closeButton = findViewById<View>(R.id.closeButton)

        // Initial tab UI
        setTabState(completedSelected = true)

        // If we don't have dogId, try to resolve it from dogName
        if (dogId.isBlank()) {
            if (dogName.isNotBlank()) {
                resolveDogIdByName(dogName)
            } else {
                Toast.makeText(this, "Missing dog identifier", Toast.LENGTH_SHORT).show()
            }
        } else {
            // We already have the id; load initial fragment
            showFragment(VaccinationListFragment.newInstance(status = selectedStatus, dogId = dogId))
        }

        tabCompleted.setOnClickListener {
            selectedStatus = "completed"
            setTabState(completedSelected = true)
            if (dogId.isNotBlank()) {
                showFragment(VaccinationListFragment.newInstance(status = selectedStatus, dogId = dogId))
            }
        }

        tabUpcoming.setOnClickListener {
            selectedStatus = "due" // Adjust if your Firestore uses a different value
            setTabState(completedSelected = false)
            if (dogId.isNotBlank()) {
                showFragment(VaccinationListFragment.newInstance(status = selectedStatus, dogId = dogId))
            }
        }

        closeButton.setOnClickListener { finish() }
    }

    private fun resolveDogIdByName(name: String) {
        firestore.collection("dogs")
            .whereEqualTo("name", name)
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    dogId = snapshot.documents.first().id
                    showFragment(VaccinationListFragment.newInstance(status = selectedStatus, dogId = dogId))
                } else {
                    Toast.makeText(this, "Dog not found: $name", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load dog: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setTabState(completedSelected: Boolean) {
        if (completedSelected) {
            tabCompleted.setTextColor(getColor(android.R.color.holo_orange_dark))
            tabCompleted.setBackgroundResource(R.drawable.tab_selected_bg)
            tabUpcoming.setTextColor(getColor(android.R.color.darker_gray))
            tabUpcoming.setBackgroundResource(R.drawable.tab_unselected_bg)
        } else {
            tabUpcoming.setTextColor(getColor(android.R.color.holo_orange_dark))
            tabUpcoming.setBackgroundResource(R.drawable.tab_selected_bg)
            tabCompleted.setTextColor(getColor(android.R.color.darker_gray))
            tabCompleted.setBackgroundResource(R.drawable.tab_unselected_bg)
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}