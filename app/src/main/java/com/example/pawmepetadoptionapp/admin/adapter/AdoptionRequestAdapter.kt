package com.example.pawmepetadoptionapp.admin.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmepetadoptionapp.R
import com.example.pawmepetadoptionapp.admin.model.AdoptionRequest

class AdoptionRequestAdapter(
    private val requests: List<AdoptionRequest>,
    private val onAccept: (AdoptionRequest) -> Unit,
    private val onReject: (AdoptionRequest) -> Unit
) : RecyclerView.Adapter<AdoptionRequestAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUserName: TextView = view.findViewById(R.id.tvUserName)
        val tvDogName: TextView = view.findViewById(R.id.tvDogName)
        val tvPhone: TextView = view.findViewById(R.id.tvPhone)
        val tvAddress: TextView = view.findViewById(R.id.tvAddress)
        val btnAccept: Button = view.findViewById(R.id.btnAccept)
        val btnReject: Button = view.findViewById(R.id.btnReject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_adoption_request, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val request = requests[position]
        holder.tvUserName.text = request.userName
        holder.tvDogName.text = "Dog: ${request.dogName}"
        holder.tvPhone.text = "Phone: ${request.phone}"
        holder.tvAddress.text = "Address: ${request.address}"
        holder.btnAccept.setOnClickListener { onAccept(request) }
        holder.btnReject.setOnClickListener { onReject(request) }
    }

    override fun getItemCount(): Int = requests.size
}
