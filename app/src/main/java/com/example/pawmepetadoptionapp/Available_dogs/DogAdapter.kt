package com.example.pawmepetadoptionapp

import DogProfileDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pawmepetadoptionapp.Available_dogs.FosterDogs


class DogAdapter(private val dogs: MutableList<FosterDogs>,
                 private val onDogFostered: (FosterDogs) -> Unit
) : RecyclerView.Adapter<DogAdapter.DogViewHolder>() {

    // Add this function to update the list
    fun updateList(newDogs: List<FosterDogs>) {
        dogs.clear()
        dogs.addAll(newDogs)
        notifyDataSetChanged()
    }

    class DogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dogImage: ImageView = itemView.findViewById(R.id.dogImage)
        val dogName: TextView = itemView.findViewById(R.id.dogName)
        val learnMore: Button = itemView.findViewById(R.id.btnLearnMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.available_dog_card, parent, false)
        return DogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val dog = dogs[position]
        holder.dogName.text = dog.name

        // Get the image name from the dog object and convert it to a drawable resource ID
        val context = holder.itemView.context
        Glide.with(context)
            .load(dog.imageName) // Now it's a URL
            .placeholder(R.drawable.sample_dog) // default while loading
            .error(R.drawable.sample_dog) // fallback if fails
            .into(holder.dogImage)


        holder.learnMore.setOnClickListener {
            val activity = holder.itemView.context as? AppCompatActivity
            if (activity != null) {
                val dialog = DogProfileDialog(dog) { selectedDog ->
                    onDogFostered(selectedDog) // Notify the fragment to update list
                }
                dialog.show(activity.supportFragmentManager, "DogProfileDialog")
            } else {
                // Optionally handle the error if context is not an AppCompatActivity
            }
        }
    }

    override fun getItemCount() = dogs.size
}
