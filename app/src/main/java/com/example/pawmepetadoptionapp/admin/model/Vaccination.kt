package com.example.pawmepetadoptionapp.admin.model


data class Vaccination(
    var dogName: String = "",
    var vaccine: String = "",
    var date: String = "",
    var dueDate: String = "",
    var id: String? = null
)
