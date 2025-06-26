package com.example.pawmepetadoptionapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class FosterListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foster_list)

        // 1. Dummy dog names
        val dogNames = listOf("Max", "Bella", "Ragnar", "Luna", "Charlie")

        // 2. Find ListView from XML
        val listView = findViewById<ListView>(R.id.listViewDogs)

        // 3. Create adapter to show list items
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, dogNames)
        listView.adapter = adapter

        // 4. Handle click on dog name
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedDog = dogNames[position]
            val intent = Intent(this, DogDetailsActivity::class.java)
            intent.putExtra("dogName", selectedDog)
            startActivity(intent)
        }
    }
}
