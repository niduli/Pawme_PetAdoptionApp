package com.example.pawmepetadoptionapp.FosterDogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
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

    data class PastDog(
        val name: String,
        val imageResName: String,
        val startDate: String,
        val endDate: String,
        val endReason: String,
        val endedEarly: Boolean
    )

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

                // Set dog image
                val resId = view.context.resources.getIdentifier(
                    dog.imageResName, "drawable", view.context.packageName
                )
                view.findViewById<ImageView>(R.id.imageDog).setImageResource(
                    if (resId != 0) resId else R.drawable.sample_dog
                )

                // Set up click listener for the Details button
                view.findViewById<Button>(R.id.btnDetails).setOnClickListener {
                    val dialogView = layoutInflater.inflate(R.layout.my_foster_details_dialog, null)
                    val textView = dialogView.findViewById<TextView>(R.id.txtDetails)

                    val details = """
                        Name: ${dog.name}
                        Start Date: ${dog.startDate}
                        End Date: ${dog.endDate}
                        Ended Early: ${if (dog.endedEarly) "Yes" else "No"}
                        Reason: ${dog.endReason}
                    """.trimIndent()

                    textView.text = details

                    val dialog = androidx.appcompat.app.AlertDialog.Builder(view.context)
                        .setTitle("Foster Details")
                        .setView(dialogView)
                        .setPositiveButton("OK", null)
                        .create()

                    dialog.setOnShowListener {
                        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                            ?.setTextColor(
                                view.context.getColor(R.color.colorPrimary) // or any color you prefer
                            )
                    }

                    dialog.show()

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
                    val imageResName = doc.getString("imageResName") ?: "sample_dog"
                    val startDate = doc.getString("fosterStartDate") ?: "-"
                    val endDate = doc.getString("fosterStartDate") ?: "-"
                    val reason = doc.getString("endReason") ?: "No reason provided"
                    val endedEarly = doc.getBoolean("endedEarly") ?: false

                    pastList.add(PastDog(name, imageResName, startDate, endDate, reason, endedEarly))
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


