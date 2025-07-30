package com.example.pawmepetadoptionapp

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawmepetadoptionapp.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.backButton.setOnClickListener {
            finish()
        }


        val completedTasks = listOf(
            VolunteerTask("Vet Transport - Puppy", "Kandy", "10:00 AM", "2 hrs", "Completed"),
            VolunteerTask("Feeding Strays - Evening", "Colombo", "5:00 PM", "1.5 hrs", "Completed"),
        )


        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = HistoryTaskAdapter(completedTasks)


        val sloganText = getString(R.string.history_slogan)
        val spannable = SpannableString(sloganText)
        val start = sloganText.indexOf("Pawprint")

        if (start >= 0) {
            val end = start + "Pawprint".length
            spannable.setSpan(
                ForegroundColorSpan(Color.parseColor("#FF6F4E")),
                start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        binding.historySubtitle.text = spannable
    }
}
