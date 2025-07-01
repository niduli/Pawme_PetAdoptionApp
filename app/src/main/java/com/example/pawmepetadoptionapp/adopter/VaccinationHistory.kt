package com.example.pawmepetadoptionapp.vaccine

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.pawmepetadoptionapp.R

class VaccinationHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccination_history)

        val tabCompleted = findViewById<TextView>(R.id.tabCompleted)
        val tabUpcoming = findViewById<TextView>(R.id.tabUpcoming)
        val completedList = findViewById<LinearLayout>(R.id.completedList)
        val upcomingList = findViewById<LinearLayout>(R.id.upcomingList)
        val closeButton = findViewById<View>(R.id.closeButton)

        tabCompleted.setTextColor(resources.getColor(android.R.color.holo_orange_dark))
        tabCompleted.setBackgroundResource(R.drawable.tab_selected_bg)
        completedList.visibility = View.VISIBLE
        upcomingList.visibility = View.GONE

        tabCompleted.setOnClickListener {
            tabCompleted.setTextColor(resources.getColor(android.R.color.holo_orange_dark))
            tabCompleted.setBackgroundResource(R.drawable.tab_selected_bg)
            tabUpcoming.setTextColor(resources.getColor(android.R.color.darker_gray))
            tabUpcoming.setBackgroundResource(R.drawable.tab_unselected_bg)
            completedList.visibility = View.VISIBLE
            upcomingList.visibility = View.GONE
        }

        tabUpcoming.setOnClickListener {
            tabUpcoming.setTextColor(resources.getColor(android.R.color.holo_orange_dark))
            tabUpcoming.setBackgroundResource(R.drawable.tab_selected_bg)
            tabCompleted.setTextColor(resources.getColor(android.R.color.darker_gray))
            tabCompleted.setBackgroundResource(R.drawable.tab_unselected_bg)
            completedList.visibility = View.GONE
            upcomingList.visibility = View.VISIBLE
        }

        closeButton.setOnClickListener {
            finish()
        }
    }
}
