/*package com.example.pawmepetadoptionapp

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.pawmepetadoptionapp.databinding.ActivityVolunteerDashboardBinding
import com.google.firebase.auth.FirebaseAuth

class VolunteerDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVolunteerDashboardBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVolunteerDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // Set colored slogan text with HTML formatting
        val coloredText = """
            <font color='#FF6F4E'>Be Their Hero</font>
            <font color='#000000'>– Volunteer for Paws With Purpose</font>
        """.trimIndent()
        binding.sloganText.text = Html.fromHtml(coloredText, Html.FROM_HTML_MODE_LEGACY)

        binding.cardAvailableTasks.setOnClickListener {
            startActivity(Intent(this, AvailableTasksActivity::class.java))
        }
        binding.cardMySchedule.setOnClickListener {
            startActivity(Intent(this, MyScheduleActivity::class.java))
        }
        binding.cardHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java)) // You can implement HistoryActivity similarly
        }
    }
}
*/

package com.example.pawmepetadoptionapp

import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import com.example.pawmepetadoptionapp.databinding.ActivityVolunteerDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class VolunteerDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVolunteerDashboardBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVolunteerDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set colored slogan text with HTML formatting
        val coloredText = """
            <font color='#FF6F4E'>Be Their Hero</font> 
            <font color='#000000'>– Volunteer for Paws With Purpose</font>
        """.trimIndent()
        binding.sloganText.text = Html.fromHtml(coloredText, Html.FROM_HTML_MODE_LEGACY)

        binding.cardAvailableTasks.setOnClickListener {
            startActivity(Intent(this, AvailableTasksActivity::class.java))
        }
        binding.cardMySchedule.setOnClickListener {
            startActivity(Intent(this, MyScheduleActivity::class.java))
        }
        binding.cardHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        // Bottom Navigation setup
        val bottomNav = findViewById<BottomNavigationView>(R.id.volunteerBottomNav)
        bottomNav.selectedItemId = R.id.nav_home
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Already in VolunteerDashboardActivity
                    true
                }
                R.id.nav_available_tasks -> {
                    startActivity(Intent(this, AvailableTasksActivity::class.java))
                    true
                }
                R.id.nav_my_schedule -> {
                    startActivity(Intent(this, MyScheduleActivity::class.java))
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    true
                }
                R.id.nav_paw_alert -> {
                    startActivity(Intent(this, StrayDogReportFormActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}