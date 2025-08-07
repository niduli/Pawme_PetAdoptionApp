package com.example.pawmepetadoptionapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private var droppedLatLng: LatLng? = null
    private var droppedAddress: String? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationPermissionRequestCode = 1002

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val buttonConfirmLocation = findViewById<Button>(R.id.buttonConfirmLocation)
        buttonConfirmLocation.setOnClickListener {
            if (droppedLatLng != null) {
                val intent = Intent()
                intent.putExtra("lat", droppedLatLng!!.latitude)
                intent.putExtra("lng", droppedLatLng!!.longitude)
                intent.putExtra("address", droppedAddress)
                setResult(RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this, "Please pick a location on the map!", Toast.LENGTH_SHORT).show()
            }
        }

        val buttonPickCurrentLocation = findViewById<Button>(R.id.buttonPickCurrentLocation)
        buttonPickCurrentLocation.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    locationPermissionRequestCode
                )
            } else {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        val latLng = LatLng(location.latitude, location.longitude)
                        map.clear()
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
                        val markerOptions = MarkerOptions().position(latLng).title("Your Current Location")
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val addressText = try {
                            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                            if (!addresses.isNullOrEmpty()) addresses[0].getAddressLine(0) else null
                        } catch (e: Exception) { null }
                        if (addressText != null) {
                            markerOptions.snippet(addressText)
                            droppedAddress = addressText
                        } else {
                            droppedAddress = "${latLng.latitude}, ${latLng.longitude}"
                        }
                        map.addMarker(markerOptions)
                        droppedLatLng = latLng
                    } else {
                        Toast.makeText(this, "Unable to get current location!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val defaultLocation = LatLng(7.8731, 80.7718)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 7f))

        map.setOnMapClickListener { latLng ->
            map.clear()
            val markerOptions = MarkerOptions().position(latLng).title("Picked Location")
            val geocoder = Geocoder(this, Locale.getDefault())
            val addressText = try {
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (!addresses.isNullOrEmpty()) addresses[0].getAddressLine(0) else null
            } catch (e: Exception) { null }
            if (addressText != null) {
                markerOptions.snippet(addressText)
                droppedAddress = addressText
            } else {
                droppedAddress = "${latLng.latitude}, ${latLng.longitude}"
            }
            map.addMarker(markerOptions)
            droppedLatLng = latLng
        }
    }
}