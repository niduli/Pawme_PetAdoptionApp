package com.example.pawmepetadoptionapp.FosterDogs

import android.content.Context
import android.os.Bundle
import android.text.InputType
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
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide


class CurrentFostersFragment : Fragment() {

    // Firebase instances
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // UI components
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CurrentFosterAdapter

    // List to hold fostered dogs
    private val currentFosters = mutableListOf<FosterDog>()

    // Data class to model foster dog info
    data class FosterDog(
        val id: String = "",
        val name: String = "",
        val imageResName: String = "",
        var fosterEndDate: String = ""
    )

    // Inflate the fragment layout and set up RecyclerView
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

    // Refresh data when fragment resumes
    override fun onResume() {
        super.onResume()
        loadCurrentFosters()
    }

    // Load fostered dogs from Firestore
    private fun loadCurrentFosters() {
        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }
        val userId = user.uid

        // Clear old data before loading new
        currentFosters.clear()
        adapter.notifyDataSetChanged()

        // Fetch current fosters from Firestore
        db.collection("users").document(userId)
            .collection("currentFosters")
            .get()
            .addOnSuccessListener { result ->
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val today = Date()

                val remainingDogs = mutableListOf<FosterDog>()

                val batch = db.batch() // For efficient batch operations

                val processTasks = result.documents.map { doc ->
                    val id = doc.id
                    val name = doc.getString("name") ?: ""
                    val imageResName = doc.getString("imageResName") ?: ""
                    val fosterEndDateStr = doc.getString("fosterEndDate") ?: ""
                    val fosterStartDate = doc.getString("fosterStartDate") ?: ""

                    val fosterEndDate = try {
                        sdf.parse(fosterEndDateStr)
                    } catch (e: Exception) {
                        null
                    }

                    if (fosterEndDate != null && fosterEndDate.before(today)) {
                        // Past due → Move to pastFosters
                        val pastData = doc.data?.toMutableMap() ?: mutableMapOf()
                        pastData["endedEarly"] = false
                        pastData["endReason"] = "Foster period completed"
                        pastData["fosterEndDate"] = sdf.format(today)
                        pastData["fosterStartDate"] = fosterStartDate

                        val pastRef = db.collection("users").document(userId)
                            .collection("pastFosters").document(id)

                        batch.set(pastRef, pastData)

                        // Remove from currentFosters
                        val currentRef = db.collection("users").document(userId)
                            .collection("currentFosters").document(id)

                        // Mark dog as available again
                        val dogMainRef = db.collection("dogs").document(id)
                        batch.update(dogMainRef, "available", true)

                        batch.delete(currentRef)

                    } else {
                        // Still active → keep
                        remainingDogs.add(FosterDog(id, name, imageResName, fosterEndDateStr))
                    }
                }

                // Commit the batch if we moved any dogs
                batch.commit().addOnSuccessListener {
                    currentFosters.addAll(remainingDogs)
                    adapter.notifyDataSetChanged()

                    if (currentFosters.isEmpty()) {
                        Toast.makeText(context, "No current fosters found", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(context, "Error updating fosters: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to load current fosters: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    // RecyclerView adapter for current fostered dogs
    inner class CurrentFosterAdapter(private val dogs: List<FosterDog>) :
        RecyclerView.Adapter<CurrentFosterAdapter.ViewHolder>() {

        // ViewHolder to bind views
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imgDog: ImageView = itemView.findViewById(R.id.imageDog)
            val txtName: TextView = itemView.findViewById(R.id.txtName)
            val txtDaysRemaining: TextView = itemView.findViewById(R.id.txtDaysRemaining)
            val btnExtend: Button = itemView.findViewById(R.id.btnExtend)
            val btnEndFoster: Button = itemView.findViewById(R.id.btnEndFoster)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_foster_item_current_foster, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val dog = dogs[position]
            holder.txtName.text = dog.name

            // Calculate remaining foster days
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

            val imageUrl = dog.imageResName
            if (imageUrl.startsWith("http")) {
                // Load from URL with Glide
                Glide.with(holder.itemView.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.sample_dog) // fallback image while loading
                    .error(R.drawable.sample_dog)       // fallback if load fails
                    .into(holder.imgDog)
            } else {
                // Load from drawable resource (legacy or fallback)
                val resId = holder.itemView.context.resources.getIdentifier(
                    imageUrl, "drawable", holder.itemView.context.packageName
                )
                if (resId != 0) {
                    holder.imgDog.setImageResource(resId)
                } else {
                    holder.imgDog.setImageResource(R.drawable.sample_dog)
                }
            }

            // Handle EXTEND button click
            holder.btnExtend.setOnClickListener {

                val context = holder.itemView.context
                val dialogView = LayoutInflater.from(context).inflate(R.layout.my_foster_extend_days, null)
                val spinner = dialogView.findViewById<Spinner>(R.id.spinnerExtendOptions)

                // Spinner dropdown options
                val options = listOf("10 days", "1 month", "5 months")
                val adapterSpinner = ArrayAdapter(context, android.R.layout.simple_spinner_item, options)
                adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapterSpinner

                val dialog = AlertDialog.Builder(context)
                    .setTitle("Extend Foster Period")
                    .setView(dialogView)
                    .setPositiveButton("Extend") { _, _ ->
                        val selected = spinner.selectedItem as String
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val endDate = try {
                            sdf.parse(dog.fosterEndDate)
                        } catch (e: Exception) {
                            Date()
                        }
                        val cal = Calendar.getInstance()
                        cal.time = endDate

                        // Add extra days or months
                        when {
                            selected.endsWith("days") -> {
                                val days = selected.split(" ")[0].toIntOrNull() ?: 0
                                cal.add(Calendar.DAY_OF_YEAR, days)
                            }
                            selected.endsWith("month") || selected.endsWith("months") -> {
                                val months = selected.split(" ")[0].toIntOrNull() ?: 0
                                cal.add(Calendar.MONTH, months)
                            }
                        }

                        val newEndDate = sdf.format(cal.time)

                        // Update Firestore with new date
                        val userId = FirebaseAuth.getInstance().currentUser?.uid
                        if (userId != null) {
                            FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(userId)
                                .collection("currentFosters")
                                .document(dog.id)
                                .update("fosterEndDate", newEndDate)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Extended until $newEndDate", Toast.LENGTH_SHORT).show()
                                    dog.fosterEndDate = newEndDate
                                    notifyItemChanged(position)
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Failed to update foster end date", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()

                // Set button colors
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))

            }
            // Handle END FOSTER button click
            holder.btnEndFoster.setOnClickListener {
                val context = holder.itemView.context
                val dialog = AlertDialog.Builder(context)
                    .setTitle("End Foster")
                    .setMessage("Are you sure you want to end the foster for ${dog.name}?")
                    .setPositiveButton("Yes") { _, _ ->
                        showReasonDialog(context, dog, position)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
                // Set button colors
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
            }
        }

        override fun getItemCount(): Int = dogs.size
    }

    // Show second dialog to enter reason after confirming foster end
    private fun showReasonDialog(context: Context, dog: FosterDog, position: Int) {
        val input = EditText(context).apply {
            hint = "Enter reason for ending foster"
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            minLines = 3
            setPadding(40, 30, 40, 30)
        }

        val dialog = AlertDialog.Builder(context)
            .setTitle("Reason for Ending Foster")
            .setView(input)
            .setPositiveButton("Submit") { _, _ ->
                val reason = input.text.toString().trim()
                if (reason.isNotEmpty()) {
                    endFosterForDog(dog, reason, position)
                } else {
                    Toast.makeText(context, "Please provide a reason.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
        // Set button colors
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
    }

    // Move dog from currentFosters to pastFosters and update UI
    private fun endFosterForDog(dog: FosterDog, reason: String, position: Int) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()
        val currentDocRef = db.collection("users").document(userId)
            .collection("currentFosters").document(dog.id)

        currentDocRef.get().addOnSuccessListener { snapshot ->
            val data = snapshot.data
            if (data != null) {
                val pastData = data.toMutableMap()

                // Add extra info
                pastData["endedEarly"] = true
                pastData["endReason"] = reason
                pastData["fosterEndDate"] = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                // Save to pastFosters
                val pastRef = db.collection("users").document(userId)
                    .collection("pastFosters").document(dog.id)

                pastRef.set(pastData).addOnSuccessListener {
                    // Remove from currentFosters
                    currentDocRef.delete().addOnSuccessListener {
                        // Mark dog available again
                        db.collection("dogs").document(dog.id)
                            .update("available", true)

                        // Update UI
                        currentFosters.removeAt(position)
                        adapter.notifyItemRemoved(position)
                        Toast.makeText(requireContext(), "Foster ended for ${dog.name}", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed to remove current foster", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to save to past fosters", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Dog not found in database", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
