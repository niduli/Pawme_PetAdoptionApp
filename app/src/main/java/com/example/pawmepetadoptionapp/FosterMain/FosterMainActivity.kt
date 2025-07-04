package com.example.pawmepetadoptionapp.Main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pawmepetadoptionapp.AvailableDogsFragment
import com.example.pawmepetadoptionapp.FosterDogs.MyFostersFragment
import com.example.pawmepetadoptionapp.Profile.FosterProfileFragment
import com.example.pawmepetadoptionapp.R
import com.example.pawmepetadoptionapp.Vaccination.VaccinationTrackerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class FosterMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foster_main)

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

        loadFragment(AvailableDogsFragment())

        val menuIcon = findViewById<ImageView>(R.id.menuIcon)
        val logoIcon = findViewById<ImageView>(R.id.logoIcon)

        menuIcon.setOnClickListener { showPopupMenu(it) }

        logoIcon.setOnClickListener {
            Toast.makeText(this, "App logo clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }

    private fun showPopupMenu(view: View) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.menu_toolbar_dropdown, popup.menu)

//        popup.setOnMenuItemClickListener { item ->
//            when (item.itemId) {
//                R.id.action_logout -> {
//                    Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, FosterLoginActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    startActivity(intent)
//                    true
//                }
//
//                else -> false
//            }
//        }

        // Show icons in popup menu using reflection
        try {
            val field = popup.javaClass.getDeclaredField("mPopup")
            field.isAccessible = true
            val menuPopupHelper = field.get(popup)
            val setForceIcons = menuPopupHelper.javaClass.getMethod("setForceShowIcon", Boolean::class.java)
            setForceIcons.invoke(menuPopupHelper, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        popup.show()
    }
}
