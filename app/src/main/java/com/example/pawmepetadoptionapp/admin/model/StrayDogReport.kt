package com.example.pawmepetadoptionapp.admin.model

data class StrayDogReport(
    val id: String = "",
    val description: String = "",
    val location: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val contact: String = "",
    val photoUrls: List<String> = emptyList(),
    val date: String = "",
    val time: String = "",
    val createdAt: Long? = null,
    val userId: String = ""
)