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
import java.text.SimpleDateFormat
import java.util.*


class PastFostersFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    data class PastDog(val name: String, val fosterStart: String, val fosterEnd: String)

    private val pastList = mutableListOf<PastDog>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.my_fosters_fragment, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerPastFosters)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val itemView = layoutInflater.inflate(R.layout.my_foster_item_past_foster, parent, false)
                return object : RecyclerView.ViewHolder(itemView) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val dog = pastList[position]
                val itemView = holder.itemView

                itemView.findViewById<TextView>(R.id.txtDogName).text = dog.name
                itemView.findViewById<TextView>(R.id.txtFosterDates).text = "Fostered: ${dog.fosterStart} - ${dog.fosterEnd}"

                itemView.findViewById<Button>(R.id.btnReview).setOnClickListener {
                    Toast.makeText(context, "Review submitted for ${dog.name}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun getItemCount(): Int = pastList.size
        }

        recyclerView.adapter = adapter

        // Fetch past fosters
        val user = auth.currentUser
        if (user != null) {
            db.collection("users").document(user.uid).collection("fosteredDogs")
                .get()
                .addOnSuccessListener { documents ->
                    pastList.clear()
                    for (doc in documents) {
                        val fosterEndStr = doc.getString("fosterEnd") ?: continue
                        val fosterStartStr = doc.getDate("fosterStart")?.let {
                            dateFormat.format(it)
                        } ?: continue

                        val fosterEndDate = dateFormat.parse(fosterEndStr) ?: continue
                        if (fosterEndDate.before(Date())) {
                            pastList.add(
                                PastDog(
                                    name = doc.getString("name") ?: "",
                                    fosterStart = fosterStartStr,
                                    fosterEnd = fosterEndStr
                                )
                            )
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to fetch past fosters", Toast.LENGTH_SHORT).show()
                }
        }

        return view
    }
}
