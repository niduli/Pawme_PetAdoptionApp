package com.example.pawmepetadoptionapp.Profile

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AlertDialog
import com.example.pawmepetadoptionapp.R


class FosterProfileFragment : Fragment() {
    private lateinit var badgeAdapter: BadgeAdapter
    private val badges = listOf("Top Foster 2025", "Reliable Rescuer", "5-Star Carer")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_foster_fragment, container, false)

        val txtDuration = view.findViewById<TextView>(R.id.txtPrefDuration)
        val txtSize = view.findViewById<TextView>(R.id.txtPrefSize)
        val btnEdit = view.findViewById<Button>(R.id.btnEditPreferences)
        val logout = view.findViewById<Button>(R.id.btnLogout)

        val badgeRecycler = view.findViewById<RecyclerView>(R.id.recyclerBadges)
        badgeRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        badgeAdapter = BadgeAdapter(badges)
        badgeRecycler.adapter = badgeAdapter

        btnEdit.setOnClickListener {
            showEditDialog(txtDuration, txtSize)
        }

        logout.setOnClickListener {
            Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
            // Navigation or actual logout logic here
        }

        return view
    }

    private fun showEditDialog(txtDuration: TextView, txtSize: TextView) {
        val dialog = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.profile_dialog_edit_preferences, null)

        val inputDuration = view.findViewById<EditText>(R.id.inputDuration)
        val inputSize = view.findViewById<EditText>(R.id.inputSize)

        dialog.setTitle("Edit Preferences")
        dialog.setView(view)
        dialog.setPositiveButton("Save") { _, _ ->
            txtDuration.text = "Duration: ${inputDuration.text}"
            txtSize.text = "Preferred Dog Size: ${inputSize.text}"
        }
        dialog.setNegativeButton("Cancel", null)
        dialog.show()
    }

    class BadgeAdapter(private val badges: List<String>) :
        RecyclerView.Adapter<BadgeAdapter.BadgeViewHolder>() {

        class BadgeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val txtBadge: TextView = view.findViewById(R.id.txtBadge)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.profile_badge_item, parent, false)
            return BadgeViewHolder(view)
        }

        override fun onBindViewHolder(holder: BadgeViewHolder, position: Int) {
            holder.txtBadge.text = badges[position]
        }

        override fun getItemCount(): Int = badges.size
    }

}