package com.example.pawme.Available_dogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmepetadoptionapp.R

class DogAdapter(private val dogs: List<Dog>) : RecyclerView.Adapter<DogAdapter.DogViewHolder>() {

    class DogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.dogName)
        val info = itemView.findViewById<TextView>(R.id.dogInfo)
        val duration = itemView.findViewById<TextView>(R.id.dogDuration)
        val needs = itemView.findViewById<TextView>(R.id.dogNeeds)
        val button = itemView.findViewById<Button>(R.id.btnLearnMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.available_dog_card, parent, false)
        return DogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val dog = dogs[position]
        holder.name.text = dog.name
        holder.info.text = "Age: ${dog.age} | Breed: ${dog.breed}"
        holder.duration.text = "Foster Duration: ${dog.duration}"
        holder.needs.text = "Special Needs: ${dog.needs}"
        holder.button.setOnClickListener {
            Toast.makeText(holder.itemView.context, "More info about ${dog.name}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = dogs.size
}
