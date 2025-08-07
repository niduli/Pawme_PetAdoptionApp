package com.example.pawmepetadoptionapp

import android.content.Context

fun getDrawableResIdByName(name: String, context: Context): Int {
    return context.resources.getIdentifier(name, "drawable", context.packageName)
}
