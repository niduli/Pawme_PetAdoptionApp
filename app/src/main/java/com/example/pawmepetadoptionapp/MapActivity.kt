package com.example.pawmepetadoptionapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private var droppedLatLng: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val buttonConfirmLocation = findViewById<Button>(R.id.buttonConfirmLocation)
        buttonConfirmLocation.setOnClickListener {
            droppedLatLng?.let {
                val intent = Intent()
                intent.putExtra("lat", it.latitude)
                intent.putExtra("lng", it.longitude)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val defaultLocation = LatLng(7.8731, 80.7718) // Sri Lanka center
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 7f))

        map.setOnMapClickListener { latLng ->
            map.clear()
            map.addMarker(MarkerOptions().position(latLng).title("Stray Dog Location"))
            droppedLatLng = latLng
        }
    }
}