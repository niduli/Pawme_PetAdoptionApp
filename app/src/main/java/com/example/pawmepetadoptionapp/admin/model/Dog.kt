package com.example.pawmepetadoptionapp.admin.model


data class Dog(
    val id: String = "",
    val name: String = "",
    val breed: String = "",
    val age: Int = 0,
    val duration: String = "",
    val needs: String = "",
    val isAvailable: Boolean = true,
    val imageName: String = "",
    var imageUrl: String? = null,
    val type: String = "" // "adoption" or "foster"
)

