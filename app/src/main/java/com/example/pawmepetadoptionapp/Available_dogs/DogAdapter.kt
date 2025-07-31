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
import com.example.pawmepetadoptionapp.Available_dogs.FosterDogs


class DogAdapter(private val dogs: MutableList<FosterDogs>) : RecyclerView.Adapter<DogAdapter.DogViewHolder>() {

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
        val imageResId = context.resources.getIdentifier(
            dog.imageName, // e.g., "dog1"
            "drawable",
            context.packageName
        )

        // If image is found, set it. Else use a default image
        if (imageResId != 0) {
            holder.dogImage.setImageResource(imageResId)
        } else {
            holder.dogImage.setImageResource(R.drawable.sample_dog)
        }


        holder.learnMore.setOnClickListener {
            val activity = holder.itemView.context as? AppCompatActivity
            if (activity != null) {
                val dialog = DogProfileDialog(dog) { selectedDog ->
                    // Handle "Choose Fostering" click here, e.g.:
                    // Toast.makeText(activity, "${selectedDog.name} chosen for fostering!", Toast.LENGTH_SHORT).show()
                }
                dialog.show(activity.supportFragmentManager, "DogProfileDialog")
            } else {
                // Optionally handle the error if context is not an AppCompatActivity
            }
        }
    }

    override fun getItemCount() = dogs.size
}
