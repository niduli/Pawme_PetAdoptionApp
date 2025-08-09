package com.example.pawmepetadoptionapp.admin.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pawmepetadoptionapp.R
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
            tvDogName.text = dog.name ?: "Unknown"
            tvBreed.text = dog.breed ?: "Unknown breed"
            tvType.text = dog.type ?: "Unknown type"
            tvAvailability.text = if (dog.isAvailable) "Available" else "Not Available"

            Log.d("DogAdapter", "Loading image URL: ${dog.imageUrl}")

            if (!dog.imageUrl.isNullOrEmpty()) {
                Glide.with(root.context)
                    .load(dog.imageUrl)
                    //.placeholder(R.drawable.placeholder_dog) // make sure you have this drawable
                    //.error(R.drawable.placeholder_error)     // make sure you have this drawable
                    .into(imgDog)
            } else {
                //imgDog.setImageResource(R.drawable.placeholder_dog) // fallback placeholder
            }

            btnEdit.setOnClickListener { onEdit(dog) }
            btnDelete.setOnClickListener { onDelete(dog) }
        }
    }

    override fun getItemCount(): Int = dogList.size
}
