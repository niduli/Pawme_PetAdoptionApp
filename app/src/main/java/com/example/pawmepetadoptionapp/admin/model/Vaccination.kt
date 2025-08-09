package com.example.pawmepetadoptionapp.admin.model

data class Vaccination(
    var date: String = "",
    var status: String = "",
    var vaccineName: String = "",
    var id: String? = null // Firestore doc id
)