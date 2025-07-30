package com.example.pawmepetadoptionapp

data class DonationModel(
    val type: String,
    val date: String,
    val amount: String // Or you might want to use a more specific type like BigDecimal or Int
)
