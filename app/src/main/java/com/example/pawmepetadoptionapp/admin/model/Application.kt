package com.example.pawmepetadoptionapp.admin.model


import com.google.firebase.Timestamp

data class Application(
    val applicationId: String = "",
    val volunteerId: String = "",
    val taskId: String = "",
    val status: String = "pending",
    val appliedAt: Timestamp? = null
)
