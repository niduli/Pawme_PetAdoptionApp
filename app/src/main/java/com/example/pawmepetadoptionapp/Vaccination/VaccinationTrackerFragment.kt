package com.example.pawmepetadoptionapp.Vaccination

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmepetadoptionapp.R
import java.util.Calendar

class VaccinationTrackerFragment : Fragment() {

    data class Vaccine(
        val name: String,
        val date: Long, // store as timestamp
        var isReminderSet: Boolean = false,
        val isCompleted: Boolean = false
    )

    private lateinit var calendarView: CalendarView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VaccinationAdapter

    private val vaccines = mutableListOf(
        Vaccine("Bordetella", getDateMillis(2025, 6, 10), isCompleted = true),
        Vaccine("Rabies", getDateMillis(2025, 6, 15)),
        Vaccine("Distemper", getDateMillis(2025, 6, 22))
    )

    private val reminderDates = mutableSetOf<Long>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.vaccination_tracker_fragment, container, false)

        calendarView = view.findViewById(R.id.calendarView)
        recyclerView = view.findViewById(R.id.recyclerVaccines)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = VaccinationAdapter(vaccines) { vaccine ->
            vaccine.isReminderSet = true
            reminderDates.add(vaccine.date)
            adapter.notifyDataSetChanged()

            Toast.makeText(requireContext(), "Reminder set for ${vaccine.name}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        // Optionally, update CalendarView on date change
        calendarView.setOnDateChangeListener { _, year, month, day ->
            val clicked = getDateMillis(year, month, day)
            if (reminderDates.contains(clicked)) {
                Toast.makeText(requireContext(), "Reminder on this day!", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    companion object {
        fun getDateMillis(year: Int, month: Int, day: Int): Long {
            val cal = Calendar.getInstance()
            cal.set(year, month, day, 0, 0, 0)
            cal.set(Calendar.MILLISECOND, 0)
            return cal.timeInMillis
        }
    }

}
