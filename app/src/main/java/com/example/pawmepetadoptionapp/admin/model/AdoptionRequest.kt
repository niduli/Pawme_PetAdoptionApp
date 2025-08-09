package com.example.pawmepetadoptionapp.admin.model

import com.google.firebase.Timestamp

data class AdoptionRequest(
    var id: String = "",
    val dogId: String = "",
    val dogName: String = "",
    val userId: String = "",
    val userName: String = "",
    val phone: String = "",
    val address: String = "",
    val hasOtherPets: Boolean = false,
    val status: String = "",
    val timestamp: Timestamp? = null
)
