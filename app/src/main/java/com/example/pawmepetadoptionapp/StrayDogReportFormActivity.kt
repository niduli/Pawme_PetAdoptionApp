package com.example.pawmepetadoptionapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
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

    @SuppressLint("MissingInflatedId")
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

        // Drop Pin on Map
        buttonDropPin.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivityForResult(intent, 1001)
        }

        // Get Current Location
        editTextLocation.setOnLongClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED ||
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
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            } else {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    location?.let {
                        val coords = "${it.latitude}, ${it.longitude}"
                        editTextLocation.setText(coords)
                        textViewCoordinates.text = coords
                        pickedLatLng = Pair(it.latitude, it.longitude)
                    }
                }
            }
            true
        }

        // Submit Button
        buttonSubmit.setOnClickListener {
            val location = editTextLocation.text.toString()
            val description = editTextDescription.text.toString()
            val date = textViewDate.text.toString()
            val time = textViewTime.text.toString()
            val contact = editTextContact.text.toString()
            val photosList = photoUris.map { it.toString() } // list of photo URIs

            if (location.isBlank() || description.isBlank() || date.isBlank() || time.isBlank() || photoUris.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please fill all required fields and add at least one photo.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            // You can now send all this data (location, description, date, time, contact, photosList) to Firebase or your backend
            Toast.makeText(
                this,
                "Report submitted! (UI only, backend next step)",
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
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            val lat = data.getDoubleExtra("lat", 0.0)
            val lng = data.getDoubleExtra("lng", 0.0)
            val editTextLocation = findViewById<EditText>(R.id.editTextLocation)
            val textViewCoordinates = findViewById<TextView>(R.id.textViewCoordinates)
            editTextLocation.setText("$lat, $lng")
            textViewCoordinates.text = "$lat, $lng"
            pickedLatLng = Pair(lat, lng)
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
                        "Camera permission granted. You can now take photos.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Camera permission denied.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "Location permission granted.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Location permission denied.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}