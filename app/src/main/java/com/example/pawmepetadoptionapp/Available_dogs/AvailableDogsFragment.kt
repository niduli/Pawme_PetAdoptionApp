package com.example.pawmepetadoptionapp


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class AvailableDogsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ✅ Make sure this matches your actual layout file name
        val view = inflater.inflate(R.layout.available_fragment_available_dogs, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewDogs)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // ✅ Dummy dog list
        val dogs = listOf(
            Dog("Max", "2 yrs", "Labrador", "2 weeks", "Needs medication", R.drawable.bella),
            Dog("Bella", "1 yr", "Beagle", "1 week", "None",R.drawable.max),
            Dog("Ragnar", "4 yr", "German-Shepherd", "1 Month", "None", R.drawable.ragnar),
            Dog("Rowie", "3 yr", "Rottweiler", "3 week", "Needs medication", R.drawable.rowie),
            Dog("Norman", "5 yr", "Bullmustif", "2 week", "None", R.drawable.norman),


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
    val needs: String,
    val imageResId: Int,
)


