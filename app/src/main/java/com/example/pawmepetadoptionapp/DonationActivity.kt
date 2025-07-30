package com.example.pawmepetadoptionapp
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.widget.Button
import com.example.pawmepetadoptionapp.InKindDonationsActivity
import com.example.pawmepetadoptionapp.MoneyDonationActivity
import com.example.pawmepetadoptionapp.R

class DonationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation) // Assuming your XML is named 'activity_donation.xml'


        val moneyDonationCard = findViewById<CardView>(R.id.moneyDonationCard)
        val inKindDonationCard = findViewById<CardView>(R.id.inKindDonationCard)
        val viewHistoryButton = findViewById<Button>(R.id.viewHistoryButton)


        moneyDonationCard.setOnClickListener {

            val intent = Intent(this, MoneyDonationActivity::class.java)
            startActivity(intent)
        }


        inKindDonationCard.setOnClickListener {

            val intent = Intent(this, InKindDonationsActivity::class.java)
            startActivity(intent)
        }


        viewHistoryButton.setOnClickListener {

            val intent = Intent(this, DonationHistoryActivity::class.java)
            startActivity(intent)
        }
    }
}