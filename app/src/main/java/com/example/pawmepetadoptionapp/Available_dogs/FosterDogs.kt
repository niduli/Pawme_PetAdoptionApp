package com.example.pawmepetadoptionapp.Available_dogs

data class FosterDogs(
    var id: String = "",
    val name: String = "",
    val age: Long = 0,
    val breed: String = "",
    val duration: String = "",
    val needs: String = "",
    val imageName: String = "",
    val available: Boolean = true
)
