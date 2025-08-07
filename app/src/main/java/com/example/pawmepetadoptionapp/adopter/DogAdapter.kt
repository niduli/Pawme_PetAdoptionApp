package com.example.pawmepetadoptionapp.adopter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pawmepetadoptionapp.R

class DogAdapter(
    private val dogs: List<Dog>,
    private val onItemClick: (Dog) -> Unit
) : RecyclerView.Adapter<DogAdapter.DogViewHolder>() {

    inner class DogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dogImage: ImageView = view.findViewById(R.id.dog_image)
        val dogName: TextView = view.findViewById(R.id.dog_name)
        val dogDetails: TextView = view.findViewById(R.id.dog_details)

        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(dogs[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dog_card, parent, false)
        return DogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val dog = dogs[position]
        holder.dogName.text = dog.name
        holder.dogDetails.text = "${dog.age} years old • ${dog.Breed}"

        // Load the image from the URL using Glide
        if (!dog.imageUrl.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(dog.imageUrl)
                .placeholder(R.drawable.ic_profile_placeholder) // Optional placeholder
                .centerCrop()
                .into(holder.dogImage)
        } else {
            holder.dogImage.setImageResource(R.drawable.ic_profile_placeholder)
        }
        holder.dogImage.contentDescription = "Photo of ${dog.name}"
    }

    override fun getItemCount(): Int = dogs.size
}