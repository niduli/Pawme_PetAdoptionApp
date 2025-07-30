package com.example.pawmepetadoptionapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class VolunteerDashboardActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_dashboard)


        val sloganText = findViewById<TextView>(R.id.sloganText)
        val coloredText = """
            <font color='#FF6F4E'>Be Their Hero</font> 
            <font color='#000000'>– Volunteer for Paws With Purpose</font>
        """.trimIndent()
        sloganText.text = Html.fromHtml(coloredText, Html.FROM_HTML_MODE_LEGACY)


        findViewById<CardView>(R.id.cardAvailableTasks).setOnClickListener {
            startActivity(Intent(this, AvailableTasksActivity::class.java))
        }

        findViewById<CardView>(R.id.cardMySchedule).setOnClickListener {
            startActivity(Intent(this, MyScheduleActivity::class.java))
        }

        findViewById<CardView>(R.id.cardHistory).setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }
}
