package com.example.pawmepetadoptionapp.admin

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.pawmepetadoptionapp.R
import com.example.pawmepetadoptionapp.admin.adapter.PhotoPagerAdapter
import com.example.pawmepetadoptionapp.admin.model.StrayDogReport
import com.google.firebase.firestore.FirebaseFirestore
import java.net.URLEncoder

class ActivityAdminStrayReportDetail : AppCompatActivity() {

    companion object {
        const val EXTRA_REPORT_ID = "reportId"
    }

    private val db by lazy { FirebaseFirestore.getInstance() }

    private lateinit var pager: ViewPager2
    private lateinit var pagerIndicator: TextView
    private lateinit var ivFallback: ImageView
    private lateinit var tvAddress: TextView
    private lateinit var tvCoords: TextView
    private lateinit var tvDateTime: TextView
    private lateinit var tvDesc: TextView
    private lateinit var tvContact: TextView
    private lateinit var btnOpenMaps: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_stray_report_detail)

        pager = findViewById(R.id.viewPagerPhotos)
        pagerIndicator = findViewById(R.id.tvPagerIndicator)
        ivFallback = findViewById(R.id.ivFallback)
        tvAddress = findViewById(R.id.tvAddress)
        tvCoords = findViewById(R.id.tvCoords)
        tvDateTime = findViewById(R.id.tvDateTime)
        tvDesc = findViewById(R.id.tvDesc)
        tvContact = findViewById(R.id.tvContact)
        btnOpenMaps = findViewById(R.id.btnOpenMaps)

        val id = intent.getStringExtra(EXTRA_REPORT_ID) ?: return finish()

        loadReport(id)
    }

    private fun loadReport(id: String) {
        db.collection("stray_dog_reports").document(id)
            .get()
            .addOnSuccessListener { doc ->
                if (!doc.exists()) {
                    finish()
                    return@addOnSuccessListener
                }
                val report = StrayDogReport(
                    id = doc.id,
                    description = doc.getString("description") ?: "",
                    location = doc.getString("location") ?: "",
                    latitude = doc.getDouble("latitude"),
                    longitude = doc.getDouble("longitude"),
                    contact = doc.getString("contact") ?: "",
                    photoUrls = (doc.get("photoUrls") as? List<*>)?.mapNotNull { it?.toString() } ?: emptyList(),
                    date = doc.getString("date") ?: "",
                    time = doc.getString("time") ?: ""
                )

                bind(report)
            }
            .addOnFailureListener { finish() }
    }

    private fun bind(report: StrayDogReport) {
        val photos = report.photoUrls
        if (photos.isEmpty()) {
            pager.visibility = View.GONE
            pagerIndicator.visibility = View.GONE
            ivFallback.visibility = View.VISIBLE
        } else {
            pager.adapter = PhotoPagerAdapter(photos)
            pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    pagerIndicator.text = "${position + 1}/${photos.size}"
                }
            })
            pagerIndicator.text = "1/${photos.size}"
            pager.visibility = View.VISIBLE
            pagerIndicator.visibility = View.VISIBLE
            ivFallback.visibility = View.GONE
        }

        tvAddress.text = report.location.ifBlank { "No address" }
        val latStr = report.latitude?.let { String.format("%.6f", it) } ?: "N/A"
        val lngStr = report.longitude?.let { String.format("%.6f", it) } ?: "N/A"
        tvCoords.text = "Latitude: $latStr\nLongitude: $lngStr"

        tvDateTime.text = listOf(report.date, report.time).filter { it.isNotBlank() }.joinToString(" • ")
        tvDesc.text = report.description.ifBlank { "No description" }
        tvContact.text = report.contact.ifBlank { "N/A" }

        btnOpenMaps.setOnClickListener {
            openInMaps(report)
        }
    }

    private fun openInMaps(report: StrayDogReport) {
        val lat = report.latitude
        val lng = report.longitude
        val query = if (!report.location.isBlank()) report.location else "${lat ?: 0.0},${lng ?: 0.0}"

        if (lat != null && lng != null) {
            val uri = Uri.parse("geo:$lat,$lng?q=${URLEncoder.encode(query, "UTF-8")}")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                setPackage("com.google.android.apps.maps")
            }
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // Fallback to browser
                val web = Uri.parse("https://maps.google.com/?q=$lat,$lng")
                startActivity(Intent(Intent.ACTION_VIEW, web))
            }
        } else {
            // Only address available
            val web = Uri.parse("https://maps.google.com/?q=${URLEncoder.encode(query, "UTF-8")}")
            startActivity(Intent(Intent.ACTION_VIEW, web))
        }
    }
}