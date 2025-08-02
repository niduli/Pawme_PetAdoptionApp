package com.example.pawmepetadoptionapp.FosterDogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmepetadoptionapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PastFostersFragment : Fragment() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    data class PastDog(val name: String, val startDate: String, val endDate: String)

    private val pastList = mutableListOf<PastDog>()
    private lateinit var adapter: RecyclerView.Adapter<*>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val context = requireContext()
        val recyclerView = RecyclerView(context)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                val view =
                    layoutInflater.inflate(R.layout.my_foster_item_past_foster, parent, false)
                return object : RecyclerView.ViewHolder(view) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val dog = pastList[position]
                val view = holder.itemView
                view.findViewById<TextView>(R.id.txtDogName).text = dog.name
                view.findViewById<TextView>(R.id.txtFosterDates).text =
                    "Fostered: ${dog.startDate} - ${dog.endDate}"
                view.findViewById<Button>(R.id.btnReview).setOnClickListener {
                    Toast.makeText(context, "Review submitted for ${dog.name}", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun getItemCount(): Int = pastList.size
        }

        recyclerView.adapter = adapter

        fetchPastFosters()

        return recyclerView
    }

    private fun fetchPastFosters() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users").document(userId).collection("pastFosters")
            .get()
            .addOnSuccessListener { documents ->
                pastList.clear()
                for (doc in documents) {
                    val name = doc.getString("name") ?: continue
                    val startDate = doc.getString("startDate") ?: "-"
                    val endDate = doc.getString("endDate") ?: "-"
                    pastList.add(PastDog(name, startDate, endDate))
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "Failed to load past fosters: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

}


