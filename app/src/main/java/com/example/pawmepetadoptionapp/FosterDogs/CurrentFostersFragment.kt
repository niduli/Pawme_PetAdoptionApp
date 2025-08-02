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
import java.text.SimpleDateFormat
import java.util.*

class CurrentFostersFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    data class FosterDog(
        val name: String = "",
        val imageName: String = "",
        val fosterEnd: String = ""
    )

    private val fosterList = mutableListOf<FosterDog>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.my_fosters_fragment, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerCurrentFosters)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val itemView = layoutInflater.inflate(R.layout.my_foster_item_current_foster, parent, false)
                return object : RecyclerView.ViewHolder(itemView) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val dog = fosterList[position]
                val itemView = holder.itemView

                val daysLeft = getDaysLeft(dog.fosterEnd)
                itemView.findViewById<TextView>(R.id.txtName).text = dog.name
                itemView.findViewById<TextView>(R.id.txtDaysRemaining).text = "Foster days remaining: $daysLeft"

                val imageRes = resources.getIdentifier(dog.imageName, "drawable", requireContext().packageName)
                itemView.findViewById<ImageView>(R.id.imageDog).setImageResource(
                    if (imageRes != 0) imageRes else R.drawable.sample_dog
                )

                itemView.findViewById<Button>(R.id.btnExtend).setOnClickListener {
                    Toast.makeText(context, "Extend requested for ${dog.name}", Toast.LENGTH_SHORT).show()
                }

                itemView.findViewById<Button>(R.id.btnReport).setOnClickListener {
                    Toast.makeText(context, "Issue reported for ${dog.name}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun getItemCount(): Int = fosterList.size
        }

        recyclerView.adapter = adapter

        // Fetch current fosters
        val user = auth.currentUser
        if (user != null) {
            db.collection("users").document(user.uid).collection("currentFosters")
                .get()
                .addOnSuccessListener { documents ->
                    fosterList.clear()
                    for (doc in documents) {
                        val fosterEndStr = doc.getString("fosterEndDate") ?: continue
                        val fosterEndDate = dateFormat.parse(fosterEndStr) ?: continue
                        if (fosterEndDate.after(Date())) {
                            fosterList.add(
                                FosterDog(
                                    name = doc.getString("name") ?: "",
                                    imageName = doc.getString("imageResName") ?: "sample_dog",
                                    fosterEnd = fosterEndStr
                                )
                            )
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to fetch current fosters", Toast.LENGTH_SHORT).show()
                }
        }

        return view
    }

    private fun getDaysLeft(fosterEnd: String): Int {
        return try {
            val endDate = dateFormat.parse(fosterEnd)
            val diff = endDate.time - Date().time
            (diff / (1000 * 60 * 60 * 24)).toInt()
        } catch (e: Exception) {
            0
        }
    }
}
