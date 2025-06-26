package com.example.pawme.Available_dogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmepetadoptionapp.R


class AvailableDogsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.available_fragment_available_dogs, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewDogs)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Dummy dog list
        val dogs = listOf(
            Dog("Max", "2 yrs", "Labrador", "2 weeks", "Needs medication"),
            Dog("Bella", "1 yr", "Beagle", "1 week", "None")
        )

        recyclerView.adapter = DogAdapter(dogs)

        return view
    }
}

data class Dog(
    val name: String,
    val age: String,
    val breed: String,
    val duration: String,
    val needs: String
)
