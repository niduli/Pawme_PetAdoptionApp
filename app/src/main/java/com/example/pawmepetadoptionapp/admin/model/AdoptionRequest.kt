package com.example.pawmepetadoptionapp.admin.model

import com.google.firebase.Timestamp

data class AdoptionRequest(
    var id: String = "",
    var dogId: String = "",
    var dogName: String = "",
    var userId: String = "",
    var userName: String = "",
    var phone: String = "",
    var address: String = "",
    var hasOtherPets: Boolean = false,
    var experience: String = "",
    var additionalInformation: String = "",
    var status: String = "",
    var timestamp: Timestamp? = null
)
