package com.example.pawmepetadoptionapp

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class StrayDogReportFormActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var imageUri: Uri? = null
    private var pickedLatLng: Pair<Double, Double>? = null
    private lateinit var currentPhotoPath: String

    private val photoUris = mutableListOf<Uri>()
    private lateinit var photoAdapter: PhotoAdapter

    private val LOCATION_PERMISSION_REQUEST_CODE = 1002
    private val CAMERA_PERMISSION_REQUEST_CODE = 2001

    // For picking multiple photos from gallery
    private val pickPhotoLauncher = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri>? ->
        if (uris != null) {
            photoUris.addAll(uris)
            photoAdapter.notifyDataSetChanged()
        }
    }

    // For taking a photo with the camera
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success && imageUri != null) {
            photoUris.add(imageUri!!)
            photoAdapter.notifyDataSetChanged()
        }
    }

    // For picking location from the map (returns address and lat/lng)
    private val mapLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val address = result.data?.getStringExtra("address")
            val lat = result.data?.getDoubleExtra("lat", 0.0) ?: 0.0
            val lng = result.data?.getDoubleExtra("lng", 0.0) ?: 0.0
            val editTextLocation = findViewById<EditText>(R.id.editTextLocation)
            val textViewCoordinates = findViewById<TextView>(R.id.textViewCoordinates)
            if (address != null && address.isNotBlank()) {
                editTextLocation.setText(address)
            } else {
                editTextLocation.setText("$lat, $lng")
            }
            textViewCoordinates.text = getString(R.string.coordinates_format, lat, lng)
            pickedLatLng = Pair(lat, lng)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stray_dog_report_form)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Request permissions at runtime
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }

        val editTextLocation = findViewById<EditText>(R.id.editTextLocation)
        val buttonDropPin = findViewById<Button>(R.id.buttonDropPin)
        val textViewCoordinates = findViewById<TextView>(R.id.textViewCoordinates)
        val editTextDescription = findViewById<EditText>(R.id.editTextDescription)
        val buttonPickDate = findViewById<Button>(R.id.buttonPickDate)
        val textViewDate = findViewById<TextView>(R.id.textViewDate)
        val buttonPickTime = findViewById<Button>(R.id.buttonPickTime)
        val textViewTime = findViewById<TextView>(R.id.textViewTime)
        val editTextContact = findViewById<EditText>(R.id.editTextContact)
        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)

        // RecyclerView for photos
        val photosRecyclerView = findViewById<RecyclerView>(R.id.photosRecyclerView)
        photoAdapter = PhotoAdapter(photoUris) { pos ->
            photoUris.removeAt(pos)
            photoAdapter.notifyDataSetChanged()
        }
        photosRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        photosRecyclerView.adapter = photoAdapter

        // Add photo button
        val buttonAddPhoto = findViewById<Button>(R.id.buttonAddPhoto)
        buttonAddPhoto.setOnClickListener {
            val options = arrayOf("Camera", "Gallery")
            AlertDialog.Builder(this)
                .setTitle("Add Photo")
                .setItems(options) { _, which ->
                    if (which == 0) {
                        // Camera
                        val photoFile = createImageFile()
                        val uri = FileProvider.getUriForFile(
                            this,
                            "${applicationContext.packageName}.fileprovider",
                            photoFile
                        )
                        imageUri = uri
                        cameraLauncher.launch(uri)
                    } else {
                        // Gallery (allow multiple)
                        pickPhotoLauncher.launch("image/*")
                    }
                }
                .show()
        }

        // Date Picker
        val calendar = Calendar.getInstance()
        buttonPickDate.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    textViewDate.text = dateFormat.format(calendar.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Time Picker
        buttonPickTime.setOnClickListener {
            TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    textViewTime.text = timeFormat.format(calendar.time)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        // Drop Pin on Map (address handled via MapActivity)
        buttonDropPin.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            mapLauncher.launch(intent)
        }

        // Location field is now set only by map (no long press for current location)
        editTextLocation.isFocusable = false
        editTextLocation.isClickable = false

        // Submit Button
        buttonSubmit.setOnClickListener {
            val location = editTextLocation.text.toString()
            val description = editTextDescription.text.toString()
            val date = textViewDate.text.toString()
            val time = textViewTime.text.toString()
            val contact = editTextContact.text.toString()
            val photosList = photoUris.map { it.toString() }

            if (location.isBlank() || description.isBlank() || date.isBlank() || time.isBlank() || photoUris.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.please_fill_fields_and_photos),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            Toast.makeText(
                this,
                getString(R.string.report_submitted_ui_only),
                Toast.LENGTH_LONG
            ).show()
            finish()
        }

        // Bottom Navigation setup
        val bottomNav = findViewById<BottomNavigationView>(R.id.volunteerBottomNav)
        bottomNav.selectedItemId = R.id.nav_paw_alert
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, VolunteerDashboardActivity::class.java))
                    true
                }
                R.id.nav_available_tasks -> {
                    startActivity(Intent(this, AvailableTasksActivity::class.java))
                    true
                }
                R.id.nav_my_schedule -> {
                    startActivity(Intent(this, MyScheduleActivity::class.java))
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    true
                }
                R.id.nav_paw_alert -> {
                    // Already in StrayDogReportFormActivity
                    true
                }
                else -> false
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", ".jpg", storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        getString(R.string.camera_permission_granted),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.camera_permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        getString(R.string.location_permission_granted),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.location_permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}