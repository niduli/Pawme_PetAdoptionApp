package com.example.pawmepetadoptionapp.adopter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmepetadoptionapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class AdoptionActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DogAdapter
    private val dogList = mutableListOf<Dog>() // Data source for the adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adoption)

        val user = FirebaseAuth.getInstance().currentUser?.uid

        Log.e("AdoptionActivity", "Current User ID: $user")

        drawerLayout = findViewById(R.id.drawer_layout)

        val menuIcon: ImageView = findViewById(R.id.menu_icon)
        menuIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val closeDrawerIcon: ImageView = findViewById(R.id.close_drawer)
        closeDrawerIcon.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        recyclerView = findViewById(R.id.recyclerViewDogs)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = DogAdapter(dogList) { dog ->
            val intent = Intent(this, DogDetailActivity::class.java)
            intent.putExtra("name", dog.name)
            intent.putExtra("age", dog.age.toString()) // Always pass as String for consistency
            intent.putExtra("Breed", dog.Breed)        // Capital B for Breed, matches data class
            intent.putExtra("needs", dog.needs ?: "None") // Pass needs or "None" if null
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        loadDogsFromFirestore()

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

    private fun loadDogsFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        db.collection("dogs")
            .whereEqualTo("available", true)
            .get()
            .addOnSuccessListener { result ->
                dogList.clear()
                for (document in result) {
                    val dog = parseDog(document)
                    dog?.let { dogList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("AdoptionActivity", "Error getting documents: ", exception)
            }
    }

    private fun parseDog(document: QueryDocumentSnapshot): Dog? {
        val name = document.getString("name") ?: return null
        val age = document.getLong("age")?.toInt() ?: 0
        val breed = document.getString("breed") ?: ""
        val needs = document.getString("needs") // may be null
        return Dog(name, age, breed, needs)
    }

}