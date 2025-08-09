package com.example.pawmepetadoptionapp

import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmepetadoptionapp.Available_dogs.FosterDogs



class AvailableDogsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DogAdapter
    private val dogList = mutableListOf<FosterDogs>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.available_fragment_available_dogs, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewDogs)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = DogAdapter(dogList) { selectedDog ->
            dogList.remove(selectedDog)
            adapter.notifyDataSetChanged()
        }
        recyclerView.adapter = adapter

        fetchAvailableDogs()

        return view

    }

    private fun fetchAvailableDogs() {
        db.collection("dogs")
            .whereEqualTo("available", true)
            .whereEqualTo("type", "foster")
            .get()
            .addOnSuccessListener { documents ->
                val fetchedDogs = mutableListOf<FosterDogs>()
                for (doc in documents) {
                    val dog = doc.toObject(FosterDogs::class.java)
                    dog.id = doc.id
                    fetchedDogs.add(dog)
                }
                adapter.updateList(fetchedDogs)  //  Updates the adapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


}




