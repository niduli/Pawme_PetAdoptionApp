package com.example.pawmepetadoptionapp.Vaccination

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

    data class Vaccine(val name: String, val dueDate: String, val isCompleted: Boolean)

    private val vaccineList = listOf(
        Vaccine("Bordetella", "Completed", true),
        Vaccine("Rabies", "July 15", false),
        Vaccine("Distemper", "July 22", false)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.vaccination_tracker_fragment, container, false)

        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerVaccines)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = VaccinationAdapter(vaccineList)

        return view
    }

}
