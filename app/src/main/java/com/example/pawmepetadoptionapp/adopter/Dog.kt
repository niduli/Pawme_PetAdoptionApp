package com.example.pawmepetadoptionapp.adopter
data class Dog(
    val name: String,
    val age: Int,
    val Breed: String,
    val needs: String? = null,
    val imageUrl: String? = null // Add this line
)