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

class CurrentFostersFragment : Fragment() {

    data class FosterDog(val name: String, val daysLeft: Int, val imageResId: Int)

    private val fosterList = listOf(
        FosterDog("Milo", 5, R.drawable.Milo),
        FosterDog("Mochi", 22, R.drawable.mochi)


    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val context = requireContext()
        val recyclerView = RecyclerView(context)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val view = layoutInflater.inflate(R.layout.my_foster_item_current_foster, parent, false)
                return object : RecyclerView.ViewHolder(view) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val dog = fosterList[position]
                val view = holder.itemView
                view.findViewById<TextView>(R.id.txtName).text = dog.name
                view.findViewById<TextView>(R.id.txtDaysRemaining).text = "Foster days remaining: ${dog.daysLeft}"
                view.findViewById<ImageView>(R.id.imageDog).setImageResource(dog.imageResId)
                view.findViewById<Button>(R.id.btnExtend).setOnClickListener {
                    Toast.makeText(context, "Extend requested for ${dog.name}", Toast.LENGTH_SHORT).show()
                }
                view.findViewById<Button>(R.id.btnReport).setOnClickListener {
                    Toast.makeText(context, "Issue reported for ${dog.name}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun getItemCount(): Int = fosterList.size
        }

        return recyclerView
    }
}
