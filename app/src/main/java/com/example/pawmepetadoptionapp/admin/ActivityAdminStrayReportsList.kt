package com.example.pawmepetadoptionapp.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmepetadoptionapp.R
import com.example.pawmepetadoptionapp.admin.adapter.StrayDogReportAdapter
import com.example.pawmepetadoptionapp.admin.model.StrayDogReport
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ActivityAdminStrayReportsList : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var progress: ProgressBar
    private lateinit var emptyView: TextView

    private val db by lazy { FirebaseFirestore.getInstance() }
    private val items = mutableListOf<StrayDogReport>()
    private lateinit var adapter: StrayDogReportAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_stray_reports_list)

        recycler = findViewById(R.id.recyclerReports)
        progress = findViewById(R.id.progressBar)
        emptyView = findViewById(R.id.tvEmpty)

        adapter = StrayDogReportAdapter(items) { reportId ->
            val i = Intent(this, ActivityAdminStrayReportDetail::class.java)
            i.putExtra(ActivityAdminStrayReportDetail.EXTRA_REPORT_ID, reportId)
            startActivity(i)
        }
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        fetchReports()
    }

    private fun fetchReports() {
        progress.visibility = View.VISIBLE
        emptyView.visibility = View.GONE

        db.collection("stray_dog_reports")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snap ->
                items.clear()
                for (doc in snap.documents) {
                    val report = StrayDogReport(
                        id = doc.id,
                        description = doc.getString("description") ?: "",
                        location = doc.getString("location") ?: "",
                        latitude = doc.getDouble("latitude"),
                        longitude = doc.getDouble("longitude"),
                        contact = doc.getString("contact") ?: "",
                        photoUrls = (doc.get("photoUrls") as? List<*>)?.mapNotNull { it?.toString() } ?: emptyList(),
                        date = doc.getString("date") ?: "",
                        time = doc.getString("time") ?: "",
                        createdAt = (doc.getLong("createdAt") ?: (doc.getDouble("createdAt")?.toLong())),
                        userId = doc.getString("userId") ?: ""
                    )
                    items.add(report)
                }
                adapter.notifyDataSetChanged()
                progress.visibility = View.GONE
                emptyView.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
            }
            .addOnFailureListener {
                progress.visibility = View.GONE
                emptyView.visibility = View.VISIBLE
                emptyView.text = "Failed to load reports"
            }
    }
}