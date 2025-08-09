package com.example.pawmepetadoptionapp.admin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pawmepetadoptionapp.R
import com.example.pawmepetadoptionapp.admin.model.StrayDogReport

class StrayDogReportAdapter(
    private val items: List<StrayDogReport>,
    private val onClick: (reportId: String) -> Unit
) : RecyclerView.Adapter<StrayDogReportAdapter.VH>() {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPhoto: ImageView = itemView.findViewById(R.id.ivPhoto)
        val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        val tvCoords: TextView = itemView.findViewById(R.id.tvCoords)
        val tvDateTime: TextView = itemView.findViewById(R.id.tvDateTime)
        val tvDesc: TextView = itemView.findViewById(R.id.tvDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_stray_report, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        val ctx = holder.itemView.context

        val firstPhoto = item.photoUrls.firstOrNull()
        if (firstPhoto.isNullOrBlank()) {
            holder.ivPhoto.setImageResource(R.drawable.ic_image_placeholder)
        } else {
            Glide.with(ctx)
                .load(firstPhoto)
                .centerCrop()
                .placeholder(R.drawable.ic_image_placeholder)
                .into(holder.ivPhoto)
        }

        holder.tvAddress.text = item.location.ifBlank { "No address" }
        val lat = item.latitude?.let { String.format("%.6f", it) } ?: "N/A"
        val lng = item.longitude?.let { String.format("%.6f", it) } ?: "N/A"
        holder.tvCoords.text = "Lat: $lat, Lng: $lng"
        holder.tvDateTime.text = listOf(item.date, item.time).filter { it.isNotBlank() }.joinToString(" • ")
        holder.tvDesc.text = item.description.ifBlank { "No description" }

        holder.itemView.setOnClickListener { onClick(item.id) }
    }

    override fun getItemCount(): Int = items.size
}