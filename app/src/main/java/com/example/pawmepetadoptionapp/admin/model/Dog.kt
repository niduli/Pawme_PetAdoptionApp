package com.example.pawmepetadoptionapp.admin.model


data class Dog(
    val name: String = "",
    val breed: String = "",
    val age: Int = 0,
    val duration: String = "",
    val needs: String = "",
    val isAvailable: Boolean = true,
    val imageName: String = "",
    val type: String = "" // "adoption" or "foster"
)
