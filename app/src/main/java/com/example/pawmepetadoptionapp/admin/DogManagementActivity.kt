package com.example.pawmepetadoptionapp.admin

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawmepetadoptionapp.admin.adapter.DogAdapter
import com.example.pawmepetadoptionapp.admin.model.Dog
import com.example.pawmepetadoptionapp.databinding.ActivityDogManagementBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DogManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDogManagementBinding
    private lateinit var dogAdapter: DogAdapter
    private val db = FirebaseFirestore.getInstance()
    private val dogsCollection = db.collection("dogs")
    private var dogList = mutableListOf<Dog>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDogManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.rvDogs.layoutManager = LinearLayoutManager(this)
        dogAdapter = DogAdapter(dogList,
            onEdit = { showEditDialog(it) },
            onDelete = { deleteDog(it) }
        )
        binding.rvDogs.adapter = dogAdapter

        binding.fabAddDog.setOnClickListener {
            showAddDialog()
        }

        fetchDogs()
    }

    private fun fetchDogs() {
        dogsCollection.get().addOnSuccessListener { querySnapshot ->
            dogList.clear()
            for (doc in querySnapshot) {
                val dog = doc.toObject(Dog::class.java)
                dogList.add(dog)
            }
            dogAdapter.notifyDataSetChanged()
        }
    }

    private fun showAddDialog() {
        val dialog = DogDialogFragment(onSave = {
            dogsCollection.add(it).addOnSuccessListener {
                fetchDogs()
                Toast.makeText(this, "Dog added", Toast.LENGTH_SHORT).show()
            }
        })
        dialog.show(supportFragmentManager, "AddDogDialog")
    }

    private fun showEditDialog(dog: Dog) {
        val dialog = DogDialogFragment(dog, onSave = { updatedDog ->
            dogsCollection.whereEqualTo("name", dog.name).get().addOnSuccessListener { snapshot ->
                for (doc in snapshot) {
                    dogsCollection.document(doc.id).set(updatedDog).addOnSuccessListener {
                        fetchDogs()
                        Toast.makeText(this, "Dog updated", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        dialog.show(supportFragmentManager, "EditDogDialog")
    }

    private fun deleteDog(dog: Dog) {
        AlertDialog.Builder(this)
            .setTitle("Delete Dog")
            .setMessage("Are you sure you want to delete ${dog.name}?")
            .setPositiveButton("Yes") { _, _ ->
                dogsCollection.whereEqualTo("name", dog.name).get().addOnSuccessListener { snapshot ->
                    for (doc in snapshot) {
                        dogsCollection.document(doc.id).delete().addOnSuccessListener {
                            fetchDogs()
                            Toast.makeText(this, "Dog deleted", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("No", null)
            .show()
    }
}
