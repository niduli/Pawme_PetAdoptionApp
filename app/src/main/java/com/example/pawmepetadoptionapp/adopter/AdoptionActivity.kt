package com.example.pawmepetadoptionapp.adopter

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmepetadoptionapp.R
import com.example.pawmepetadoptionapp.model.Dog
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdoptionActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adoption) // updated layout file

        drawerLayout = findViewById(R.id.drawer_layout)

        // Open drawer on menu icon click
        val menuIcon: ImageView = findViewById(R.id.menu_icon)
        menuIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Close drawer on close icon click
        val closeDrawerIcon: ImageView = findViewById(R.id.close_drawer)
        closeDrawerIcon.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        // Sample dog list
        val dogs = listOf(
            Dog("Shaggy", "2 yrs • Golden Retriever", R.drawable.shaggy),
            Dog("Milo", "1 yr • Beagle", R.drawable.milo),
            Dog("Daisy", "4 yrs • Poodle", R.drawable.daisy),
            Dog("Razor", "3 yrs • Husky", R.drawable.razor),
            Dog("Luna", "5 months • Mix", R.drawable.luna)
        )

        recyclerView = findViewById(R.id.recyclerViewDogs)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = DogAdapter(dogs)
        recyclerView.adapter = adapter

        // Bottom navigation listener
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_profile -> {
                    Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        // Drawer menu actions
        findViewById<LinearLayout>(R.id.btnAdopter).setOnClickListener {
            Toast.makeText(this, "Adopter selected", Toast.LENGTH_SHORT).show()
            drawerLayout.closeDrawers()
        }

        findViewById<LinearLayout>(R.id.btnVolunteer).setOnClickListener {
            Toast.makeText(this, "Volunteer selected", Toast.LENGTH_SHORT).show()
            drawerLayout.closeDrawers()
        }

        findViewById<LinearLayout>(R.id.btnDonator).setOnClickListener {
            Toast.makeText(this, "Donator selected", Toast.LENGTH_SHORT).show()
            drawerLayout.closeDrawers()
        }

        findViewById<LinearLayout>(R.id.btnFoster).setOnClickListener {
            Toast.makeText(this, "Foster selected", Toast.LENGTH_SHORT).show()
            drawerLayout.closeDrawers()
        }
    }
}
