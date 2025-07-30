package com.example.pawmepetadoptionapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class BoneToyDonationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bone_toy)

        // Initialize views
        val backIcon = findViewById<ImageView>(R.id.backIcon)
        val homeIcon = findViewById<ImageView>(R.id.homeIcon)
        val continueButton = findViewById<Button>(R.id.continueButton)
        val amountInput = findViewById<TextInputEditText>(R.id.amountInput)

        // Back icon -> InKindDonationsActivity
        backIcon.setOnClickListener {
            val intent = Intent(this, InKindDonationsActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Home icon -> DonationActivity
        homeIcon.setOnClickListener {
            val intent = Intent(this, DonationActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
            finish()
        }

        // Continue -> PaymentActivity
        continueButton.setOnClickListener {
            val amount = amountInput.text.toString()
            if (amount.isNotEmpty()) {
                try {
                    val paymentIntent = Intent(this, PaymentActivity::class.java).apply {
                        putExtra("PRODUCT_NAME", "Bone Toy")
                        putExtra("PRODUCT_PRICE", 20.99)
                        putExtra("AMOUNT", amount.toDouble())
                    }
                    startActivity(paymentIntent)
                } catch (e: NumberFormatException) {
                    amountInput.error = "Please enter a valid amount"
                }
            } else {
                amountInput.error = "Please enter an amount"
            }
        }
    }
}
