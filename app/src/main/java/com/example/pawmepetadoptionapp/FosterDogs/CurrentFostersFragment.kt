package com.example.pawmepetadoptionapp.FosterDogs

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmepetadoptionapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class CurrentFostersFragment : Fragment() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CurrentFosterAdapter

    private val currentFosters = mutableListOf<FosterDog>()

    data class FosterDog(
        val id: String = "",
        val name: String = "",
        val imageResName: String = "",
        val fosterEndDate: String = ""
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_current_fosters, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewCurrentFosters)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = CurrentFosterAdapter(currentFosters)
        recyclerView.adapter = adapter

        return view
    }

    override fun onResume() {
        super.onResume()
        loadCurrentFosters()
    }

    private fun loadCurrentFosters() {
        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }
        val userId = user.uid

        // Clear existing data
        currentFosters.clear()
        adapter.notifyDataSetChanged()

        db.collection("users").document(userId)
            .collection("currentFosters")
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    val dog = FosterDog(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        imageResName = doc.getString("imageResName") ?: "",
                        fosterEndDate = doc.getString("fosterEndDate") ?: ""
                    )
                    currentFosters.add(dog)
                }
                adapter.notifyDataSetChanged()

                if (currentFosters.isEmpty()) {
                    Toast.makeText(context, "No current fosters found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to load current fosters: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    inner class CurrentFosterAdapter(private val dogs: List<FosterDog>) :
        RecyclerView.Adapter<CurrentFosterAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imgDog: ImageView = itemView.findViewById(R.id.imageDog)
            val txtName: TextView = itemView.findViewById(R.id.txtName)
            val txtDaysRemaining: TextView = itemView.findViewById(R.id.txtDaysRemaining)
            val btnExtend: Button = itemView.findViewById(R.id.btnExtend)
            val btnReport: Button = itemView.findViewById(R.id.btnReport)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_foster_item_current_foster, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val dog = dogs[position]
            holder.txtName.text = dog.name

            // Calculate days remaining
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val endDate = try {
                sdf.parse(dog.fosterEndDate)
            } catch (e: Exception) {
                null
            }
            val today = Date()
            val diffDays = if (endDate != null) {
                ((endDate.time - today.time) / (1000 * 60 * 60 * 24)).toInt()
            } else {
                -1
            }

            holder.txtDaysRemaining.text =
                if (diffDays >= 0) "Foster days remaining: $diffDays"
                else "Foster end date invalid"

            // Set dog image resource
            val resId = holder.itemView.context.resources.getIdentifier(
                dog.imageResName, "drawable", holder.itemView.context.packageName
            )
            if (resId != 0) {
                holder.imgDog.setImageResource(resId)
            } else {
                holder.imgDog.setImageResource(R.drawable.sample_dog) // fallback image
            }

            holder.btnExtend.setOnClickListener {
                Toast.makeText(holder.itemView.context, "Extend requested for ${dog.name}", Toast.LENGTH_SHORT).show()
            }
            holder.btnReport.setOnClickListener {
                Toast.makeText(holder.itemView.context, "Issue reported for ${dog.name}", Toast.LENGTH_SHORT).show()
            }
        }

        override fun getItemCount(): Int = dogs.size
    }
}
