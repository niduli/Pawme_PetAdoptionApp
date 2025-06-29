package com.example.pawmepetadoptionapp.Main

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pawmepetadoptionapp.FosterDogs.MyFostersFragment
import com.example.pawmepetadoptionapp.AvailableDogsFragment
import com.example.pawmepetadoptionapp.DummyFragment
import com.example.pawmepetadoptionapp.Profile.FosterProfileFragment
import com.example.pawmepetadoptionapp.R
import com.example.pawmepetadoptionapp.Vaccination.VaccinationTrackerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class FosterMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foster_main)

        // Setup Bottom Navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_available -> loadFragment(AvailableDogsFragment())
                R.id.nav_myfosters -> loadFragment(MyFostersFragment())
                R.id.nav_vaccination -> loadFragment(VaccinationTrackerFragment())
                R.id.nav_profile -> loadFragment(FosterProfileFragment())
            }
            true
        }

        // Load default fragment
        loadFragment(AvailableDogsFragment())

        // Setup Toolbar icons
        val menuIcon = findViewById<ImageView>(R.id.menuIcon)
        val logoIcon = findViewById<ImageView>(R.id.logoIcon)

        menuIcon.setOnClickListener {
            Toast.makeText(this, "Menu icon clicked", Toast.LENGTH_SHORT).show()
            // Optional: Open a navigation drawer or dialog
        }

        logoIcon.setOnClickListener {
            Toast.makeText(this, "App logo clicked", Toast.LENGTH_SHORT).show()
            // Optional: Redirect to home or show info
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }
}
