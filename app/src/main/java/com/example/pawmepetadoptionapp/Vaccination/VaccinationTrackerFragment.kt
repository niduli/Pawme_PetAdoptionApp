package com.example.pawme.Vaccination

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmepetadoptionapp.R

class VaccinationTrackerFragment : Fragment() {

    data class Vaccine(
        val name: String,
        val dueDate: String,
        val status: String
    )

    private val vaccineList = listOf(
        Vaccine("Rabies Booster", "June 30", "Upcoming"),
        Vaccine("Distemper", "June 20", "Completed ✅"),
        Vaccine("Parvovirus", "July 5", "Upcoming")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vaccination_tracker, container, false)

        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewVaccines)
        val reminderButton = view.findViewById<Button>(R.id.btnSetReminder)

        // RecyclerView setup
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_vaccine, parent, false)
                return object : RecyclerView.ViewHolder(itemView) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val vaccine = vaccineList[position]
                val view = holder.itemView
                view.findViewById<TextView>(R.id.txtVaccineName).text = vaccine.name
                view.findViewById<TextView>(R.id.txtDueDate).text = "Due: ${vaccine.dueDate}"
                view.findViewById<TextView>(R.id.txtStatus).text = "Status: ${vaccine.status}"
            }

            override fun getItemCount(): Int = vaccineList.size
        }

        // Dummy button click
        reminderButton.setOnClickListener {
            Toast.makeText(context, "Reminder synced (not functional)", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
