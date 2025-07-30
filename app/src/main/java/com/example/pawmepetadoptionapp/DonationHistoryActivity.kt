package com.example.pawmepetadoptionapp

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DonationHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DonationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_donation_history)

        // Back icon click
        findViewById<ImageView>(R.id.backIcon).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        recyclerView = findViewById(R.id.donationRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Sample donation history
        val donationList = listOf(
            DonationModel("Money Donation", "15/01/2025", "$100"),
            DonationModel("In-Kind Donation", "15/01/2025", "$100"),
            DonationModel("Money Donation", "15/01/2025", "$100"),
            DonationModel("Money Donation", "15/01/2025", "$100")
        )

        adapter = DonationAdapter(donationList)
        recyclerView.adapter = adapter
    }
}
