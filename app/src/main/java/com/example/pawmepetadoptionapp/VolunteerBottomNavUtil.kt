package com.example.pawmepetadoptionapp

import android.app.Activity
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView

object VolunteerBottomNavUtil {
    fun setupBottomNav(activity: Activity, selectedItemId: Int) {
        val bottomNav = activity.findViewById<BottomNavigationView>(R.id.volunteerBottomNav)
        if (bottomNav.selectedItemId != selectedItemId) {
            bottomNav.selectedItemId = selectedItemId
        }
        bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId == selectedItemId) return@setOnItemSelectedListener true
            when(item.itemId) {
                R.id.nav_home -> {
                    activity.startActivity(Intent(activity, VolunteerDashboardActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    activity.overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_available_tasks -> {
                    activity.startActivity(Intent(activity, AvailableTasksActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    activity.overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_my_schedule -> {
                    activity.startActivity(Intent(activity, MyScheduleActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    activity.overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_history -> {
                    activity.startActivity(Intent(activity, HistoryActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    activity.overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_paw_alert -> {
                    activity.startActivity(Intent(activity, StrayDogReportFormActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    activity.overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
    }
}