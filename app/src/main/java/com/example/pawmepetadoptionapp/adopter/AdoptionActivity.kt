package com.example.pawmepetadoptionapp.adopter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmepetadoptionapp.R
import com.example.pawmepetadoptionapp.SignInActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class AdoptionActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DogAdapter
    private val dogList = mutableListOf<Dog>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adoption)

        val user = FirebaseAuth.getInstance().currentUser?.uid
        Log.e("AdoptionActivity", "Current User ID: $user")

        recyclerView = findViewById(R.id.recyclerViewDogs)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = DogAdapter(dogList) { dog ->
            val intent = Intent(this, DogDetailActivity::class.java)
            intent.putExtra("name", dog.name)
            intent.putExtra("age", dog.age.toString())
            intent.putExtra("Breed", dog.Breed)
            intent.putExtra("needs", dog.needs ?: "None")
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        loadDogsFromFirestore()

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
        val needs = document.getString("needs")
        return Dog(name, age, breed, needs)
    }
}