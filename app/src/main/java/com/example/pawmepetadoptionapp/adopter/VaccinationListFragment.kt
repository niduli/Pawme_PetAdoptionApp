package com.example.pawmepetadoptionapp.adopter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmepetadoptionapp.R
import com.google.firebase.firestore.FirebaseFirestore

class VaccinationListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var adapter: VaccinationAdapter
    private val firestore = FirebaseFirestore.getInstance()

    companion object {
        private const val ARG_STATUS = "status"
        private const val ARG_DOG_ID = "dogId"

        fun newInstance(status: String, dogId: String): VaccinationListFragment {
            val fragment = VaccinationListFragment()
            val args = Bundle()
            args.putString(ARG_STATUS, status)
            args.putString(ARG_DOG_ID, dogId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_vaccination_list, container, false)
        recyclerView = view.findViewById(R.id.vaccinationRecyclerView)
        emptyView = view.findViewById(R.id.emptyView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = VaccinationAdapter(emptyList())
        recyclerView.adapter = adapter

        loadVaccinations()

        return view
    }

    private fun loadVaccinations() {
        val status = arguments?.getString(ARG_STATUS).orEmpty()
        val dogId = arguments?.getString(ARG_DOG_ID).orEmpty()

        if (dogId.isBlank()) {
            adapter.updateData(emptyList())
            updateEmptyState(true)
            return
        }

        firestore.collection("dogs")
            .document(dogId)
            .collection("vaccination")
            .whereEqualTo("status", status) // "completed" or "due"
            .get()
            .addOnSuccessListener { result ->
                val list = result.map { doc ->
                    VaccinationAdoption(
                        vaccineName = doc.getString("vaccineName") ?: "",
                        date = doc.getString("date") ?: "",
                        status = doc.getString("status") ?: ""
                    )
                }
                adapter.updateData(list)
                updateEmptyState(list.isEmpty())
            }
            .addOnFailureListener {
                adapter.updateData(emptyList())
                updateEmptyState(true)
            }
    }

    private fun updateEmptyState(showEmpty: Boolean) {
        emptyView.visibility = if (showEmpty) View.VISIBLE else View.GONE
        recyclerView.visibility = if (showEmpty) View.GONE else View.VISIBLE
    }
}