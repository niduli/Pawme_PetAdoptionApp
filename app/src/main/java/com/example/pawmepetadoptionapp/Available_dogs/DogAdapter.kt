package com.example.pawmepetadoptionapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



class DogAdapter(private val dogs: List<Dog>) : RecyclerView.Adapter<DogAdapter.DogViewHolder>() {

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
        holder.dogImage.setImageResource(dog.imageResId)// static for now

        holder.learnMore.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DogProfileActivity::class.java)
            intent.putExtra("DOG_NAME", dog.name)
            intent.putExtra("DOG_BREED", dog.breed)
            intent.putExtra("DOG_AGE", dog.age)
            intent.putExtra("DOG_DURATION", dog.duration)
            intent.putExtra("DOG_NEEDS", dog.needs)
            intent.putExtra("DOG_IMAGE", dog.imageResId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = dogs.size
}
