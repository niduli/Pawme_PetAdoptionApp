package com.example.pawmepetadoptionapp.admin.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pawmepetadoptionapp.databinding.ItemDogBinding
import com.example.pawmepetadoptionapp.admin.model.Dog

class DogAdapter(
    private var dogList: List<Dog>,
    private val onEdit: (Dog) -> Unit,
    private val onDelete: (Dog) -> Unit
) : RecyclerView.Adapter<DogAdapter.DogViewHolder>() {

    inner class DogViewHolder(val binding: ItemDogBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val binding = ItemDogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val dog = dogList[position]
        with(holder.binding) {
            tvDogName.text = dog.name.ifEmpty { "Unknown" }
            tvBreed.text = dog.breed.ifEmpty { "Unknown breed" }
            tvType.text = dog.type.ifEmpty { "Unknown type" }
            tvAvailability.text = if (dog.isAvailable) "Available" else "Not Available"

            Log.d("DogAdapter", "Loading image URL: ${dog.imageName}")

            if (!dog.imageName.isNullOrEmpty()) {
                Glide.with(root.context)
                    .load(dog.imageName)
                    //.placeholder(R.drawable.placeholder_dog) // Uncomment if you have a placeholder drawable
                    //.error(R.drawable.placeholder_error)     // Uncomment if you have an error drawable
                    .into(imgDog)
            } else {
                // imgDog.setImageResource(R.drawable.placeholder_dog) // Uncomment for fallback placeholder
            }

            btnEdit.setOnClickListener { onEdit(dog) }
            btnDelete.setOnClickListener { onDelete(dog) }
        }
    }

    override fun getItemCount(): Int = dogList.size
}
