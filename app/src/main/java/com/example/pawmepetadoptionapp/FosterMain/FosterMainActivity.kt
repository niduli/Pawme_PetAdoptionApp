package com.example.pawme.Main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import com.example.pawme.Available_dogs.AvailableDogsFragment
import com.example.pawme.My_foster.MyFostersFragment
import com.example.pawmepetadoptionapp.DummyFragment
import com.example.pawmepetadoptionapp.R

class FosterMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foster_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_available -> loadFragment(AvailableDogsFragment())
                R.id.nav_myfosters -> loadFragment(MyFostersFragment())
                R.id.nav_vaccination -> loadFragment(DummyFragment("Vaccination Tracker"))
                R.id.nav_profile -> loadFragment(DummyFragment("Profile"))
            }
            true
        }

        // Load default fragment
        loadFragment(AvailableDogsFragment())
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }
}
