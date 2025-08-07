package com.example.pawmepetadoptionapp.admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
            tvDogName.text = dog.name
            tvBreed.text = dog.breed
            tvType.text = dog.type
            tvAvailability.text = if (dog.isAvailable) "Available" else "Not Available"

            btnEdit.setOnClickListener { onEdit(dog) }
            btnDelete.setOnClickListener { onDelete(dog) }
        }
    }

    override fun getItemCount(): Int = dogList.size
}
