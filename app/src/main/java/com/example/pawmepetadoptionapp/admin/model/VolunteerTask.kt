package com.example.pawmepetadoptionapp.admin.model


data class VolunteerTask(
    val id: String = "",
    val name: String = "",
    val location: String = "",
    val time: String = "",
    val duration: String = "",
    val urgency: String = "",
    val status: String = "open",
    val assignedTo: String? = null
)
