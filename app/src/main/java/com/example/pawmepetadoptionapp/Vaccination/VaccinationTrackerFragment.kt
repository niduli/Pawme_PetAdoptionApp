package com.example.pawmepetadoptionapp.Vaccination

import android.icu.text.SimpleDateFormat
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class VaccinationTrackerFragment : Fragment() {

    data class Vaccine(
        val dogName: String = "",
        val name: String = "",
        val date: Long = 0,
        val isCompleted: Boolean = false
    )

    private lateinit var calendarView: CalendarView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VaccinationAdapter

    private val vaccines = mutableListOf<Vaccine>()
    private val reminderDates = mutableSetOf<Long>()

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.vaccination_tracker_fragment, container, false)

        calendarView = view.findViewById(R.id.calendarView)
        recyclerView = view.findViewById(R.id.recyclerVaccines)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = VaccinationAdapter()
        recyclerView.adapter = adapter

        calendarView.setOnDateChangeListener { _, year, month, day ->
            val clicked = getDateMillis(year, month, day)
            if (reminderDates.contains(clicked)) {
                Toast.makeText(requireContext(), "Reminder on this day!", Toast.LENGTH_SHORT).show()
            }
        }

        fetchVaccinations()

        return view
    }

    private fun fetchVaccinations() {
        val userId = auth.currentUser?.uid
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        if (userId == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // Get current foster dog IDs for the logged-in user
        db.collection("users").document(userId)
            .collection("currentFosters")
            .get()
            .addOnSuccessListener { fosterDocs ->
                if (fosterDocs.isEmpty) {
                    Toast.makeText(requireContext(), "No current fosters found", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                vaccines.clear()
                reminderDates.clear()

                for (fosterDoc in fosterDocs) {
                    val dogId = fosterDoc.id
                    val dogName = fosterDoc.getString("name") ?: "Unknown Dog"

                    // For each dog, fetch its vaccination records
                    db.collection("dogs").document(dogId)
                        .collection("vaccination")
                        .get()
                        .addOnSuccessListener { vaccDocs ->
                            for (doc in vaccDocs) {
                                val vaccineName = doc.getString("vaccineName") ?: "Unknown Vaccine"
                                val status = doc.getString("status") ?: "completed"
                                val isCompleted = status.equals("completed", ignoreCase = true)

                                val dateStr = doc.getString("date") ?: "2025-01-01"   // fallback date string

                                val parsedDate = try {
                                    sdf.parse(dateStr)
                                } catch (e: Exception) {
                                    null
                                }

                                val dateMillis = parsedDate?.time ?: 0L

                                val vaccine = Vaccine(
                                    dogName = dogName,
                                    name = vaccineName,
                                    date = dateMillis,
                                    isCompleted = isCompleted
                                )


                                vaccines.add(vaccine)

                                if (!isCompleted && dateMillis != 0L) {
                                    reminderDates.add(dateMillis)
                                }
                            }
                            adapter.updateData(vaccines)
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Error loading vaccinations: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error loading fosters: ${it.message}", Toast.LENGTH_SHORT).show()
            }
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
