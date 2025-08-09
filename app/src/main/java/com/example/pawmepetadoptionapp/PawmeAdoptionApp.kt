package com.example.pawmepetadoptionapp

import android.app.Application
import com.cloudinary.android.MediaManager

class PawmeAdoptionApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val config: HashMap<String, String> = hashMapOf(
            "cloud_name" to "dsgdfsh7o",
            "api_key" to "518841528965393",
            "api_secret" to "PakHabw-EJTGs1fQOFjlXgNex5U",
            "secure" to "true"
        )
        MediaManager.init(this, config)
    }
}