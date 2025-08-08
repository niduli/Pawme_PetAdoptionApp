package com.example.pawmepetadoptionapp

data class AssignedTask(
    val name: String = "",
    val time: String = "",
    val location: String = "",
    val status: String = "",
    val userId: String = "" // Add userId for proper backend storage
)