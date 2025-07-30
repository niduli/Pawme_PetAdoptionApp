package com.example.pawmepetadoptionapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DonationAdapter(private val donationList: List<DonationModel>) :
    RecyclerView.Adapter<DonationAdapter.DonationViewHolder>() {

    // Describes an item view and metadata about its place within the RecyclerView.
    class DonationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeTextView: TextView = itemView.findViewById(R.id.donationTypeTextView) // Assuming you have these IDs in your item layout
        val dateTextView: TextView = itemView.findViewById(R.id.donationDateTextView)
        val amountTextView: TextView = itemView.findViewById(R.id.donationAmountTextView)
    }

    // Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationViewHolder {
        // Create a new view, which defines the UI of the list item
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_donation_history, parent, false) // Ensure you have this layout file
        return DonationViewHolder(itemView)
    }

    // Called by RecyclerView to display the data at the specified position.
    override fun onBindViewHolder(holder: DonationViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val currentItem = donationList[position]
        holder.typeTextView.text = currentItem.type
        holder.dateTextView.text = currentItem.date
        holder.amountTextView.text = currentItem.amount
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = donationList.size
}
