package com.example.pawme.My_foster
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager

class PastFostersFragment : Fragment() {

    data class PastDog(val name: String, val dates: String)

    private val pastList = listOf(
        PastDog("Rocky", "1 May - 20 May"),
        PastDog("Charlie", "10 Apr - 25 Apr")
    )

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val context = requireContext()
//        val recyclerView = RecyclerView(context)
//        recyclerView.layoutManager = LinearLayoutManager(context)
//        recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//                val view = layoutInflater.inflate(R.layout.item_past_foster, parent, false)
//                return object : RecyclerView.ViewHolder(view) {}
//            }

//            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//                val dog = pastList[position]
//                val view = holder.itemView
//                view.findViewById<TextView>(R.id.txtDogName).text = dog.name
//                view.findViewById<TextView>(R.id.txtFosterDates).text = "Fostered: ${dog.dates}"
//                view.findViewById<Button>(R.id.btnReview).setOnClickListener {
//                    Toast.makeText(context, "Review submitted for ${dog.name}", Toast.LENGTH_SHORT).show()
////                }
////            }
//
//            override fun getItemCount(): Int = pastList.size
//        }
//
//        return recyclerView
//    }
}
