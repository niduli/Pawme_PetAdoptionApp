package com.example.pawmepetadoptionapp

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.HashMap

class StrayDogReportFormActivity : AppCompatActivity() {

    private var imageUri: Uri? = null
    private lateinit var currentPhotoPath: String
    private var pickedLatLng: Pair<Double, Double>? = null

    private val photoUris = mutableListOf<Uri>()
    private lateinit var photoAdapter: PhotoAdapter

    private val LOCATION_PERMISSION_REQUEST_CODE = 1002
    private val CAMERA_PERMISSION_REQUEST_CODE = 2001

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private var progressDialog: AlertDialog? = null

    private val pickPhotoLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            if (!uris.isNullOrEmpty()) {
                photoUris.addAll(uris)
                photoAdapter.notifyDataSetChanged()
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && imageUri != null) {
                photoUris.add(imageUri!!)
                photoAdapter.notifyDataSetChanged()
            }
        }

    private val mapLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val address = result.data?.getStringExtra("address")
                val lat = result.data?.getDoubleExtra("lat", 0.0) ?: 0.0
                val lng = result.data?.getDoubleExtra("lng", 0.0) ?: 0.0
                val editTextLocation = findViewById<EditText>(R.id.editTextLocation)
                val textViewCoordinates = findViewById<TextView>(R.id.textViewCoordinates)
                if (!address.isNullOrBlank()) {
                    editTextLocation.setText(address)
                } else {
                    editTextLocation.setText("$lat, $lng")
                }
                textViewCoordinates.text = getString(R.string.coordinates_format, lat, lng)
                pickedLatLng = lat to lng
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stray_dog_report_form)

        // Defensive init (will only run if Application not correctly registered)
        ensureCloudinaryInitialized()

        requestNeededPermissions()

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
        val buttonAddPhoto = findViewById<Button>(R.id.buttonAddPhoto)

        val photosRecyclerView = findViewById<RecyclerView>(R.id.photosRecyclerView)
        photoAdapter = PhotoAdapter(photoUris) { pos ->
            photoUris.removeAt(pos)
            photoAdapter.notifyDataSetChanged()
        }
        photosRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        photosRecyclerView.adapter = photoAdapter

        buttonAddPhoto.setOnClickListener {
            val options = arrayOf("Camera", "Gallery")
            AlertDialog.Builder(this)
                .setTitle("Add Photo")
                .setItems(options) { _, which ->
                    if (which == 0) {
                        val photoFile = createImageFile()
                        val uri = FileProvider.getUriForFile(
                            this,
                            "${applicationContext.packageName}.fileprovider",
                            photoFile
                        )
                        imageUri = uri
                        cameraLauncher.launch(uri)
                    } else {
                        pickPhotoLauncher.launch("image/*")
                    }
                }.show()
        }

        val calendar = Calendar.getInstance()
        buttonPickDate.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    textViewDate.text = dateFormat.format(calendar.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        buttonPickTime.setOnClickListener {
            TimePickerDialog(
                this,
                { _, hour, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    textViewTime.text = timeFormat.format(calendar.time)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        buttonDropPin.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            mapLauncher.launch(intent)
        }

        editTextLocation.isFocusableInTouchMode = true
        editTextLocation.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                pickedLatLng = null
                textViewCoordinates.text = ""
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        buttonSubmit.setOnClickListener {
            val location = editTextLocation.text.toString().trim()
            val description = editTextDescription.text.toString().trim()
            val date = textViewDate.text.toString().trim()
            val time = textViewTime.text.toString().trim()
            val contact = editTextContact.text.toString().trim()
            val latLng = pickedLatLng
            val userId = auth.currentUser?.uid ?: "anonymous"

            if (location.isBlank() ||
                description.isBlank() ||
                date.isBlank() ||
                time.isBlank() ||
                photoUris.isEmpty()
            ) {
                Toast.makeText(
                    this,
                    getString(R.string.please_fill_fields_and_photos),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            uploadAllToCloudinaryAndSave(
                location,
                description,
                date,
                time,
                contact,
                latLng,
                userId
            )
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.volunteerBottomNav)
        bottomNav.selectedItemId = R.id.nav_paw_alert
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, VolunteerDashboardActivity::class.java)); true
                }
                R.id.nav_available_tasks -> {
                    startActivity(Intent(this, AvailableTasksActivity::class.java)); true
                }
                R.id.nav_my_schedule -> {
                    startActivity(Intent(this, MyScheduleActivity::class.java)); true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this, HistoryActivity::class.java)); true
                }
                R.id.nav_paw_alert -> true
                else -> false
            }
        }
    }


    private fun isCloudinaryInitialized(): Boolean =
        try {
            MediaManager.get()
            true
        } catch (e: IllegalStateException) {
            false
        }

    private fun ensureCloudinaryInitialized() {
        if (isCloudinaryInitialized()) {
            Log.d("CloudinaryInit", "Cloudinary already initialized.")
            return
        }
        try {
            Log.w("CloudinaryInit", "Cloudinary not initialized. Performing fallback init in Activity.")
            val config: HashMap<String, String> = hashMapOf(
                "cloud_name" to "dsgdfsh7o",
                "api_key" to "518841528965393",
                "api_secret" to "PakHabw-EJTGs1fQOFjlXgNex5U",
                "secure" to "true"
            )
            MediaManager.init(applicationContext, config)
            Log.d("CloudinaryInit", "Fallback init success.")
        } catch (e: Exception) {
            Log.e("CloudinaryInit", "Fallback init failed: ${e.message}", e)
        }
    }

    private fun uploadAllToCloudinaryAndSave(
        location: String,
        description: String,
        date: String,
        time: String,
        contact: String,
        latLng: Pair<Double, Double>?,
        userId: String
    ) {
        if (photoUris.isEmpty()) return

        ensureCloudinaryInitialized()

        if (!isCloudinaryInitialized()) {
            Toast.makeText(this, "Cloudinary not initialized. Cannot upload.", Toast.LENGTH_LONG).show()
            return
        }

        showProgress("Uploading 0/${photoUris.size} photos...")

        val uploadedUrls = Collections.synchronizedList(mutableListOf<String>())
        val completed = AtomicInteger(0)
        var anyFailed = false
        val total = photoUris.size

        fun updateProgress() {
            val done = completed.get()
            showProgress("Uploading $done/$total photos...")
            if (done == total) {
                if (uploadedUrls.isEmpty()) {
                    hideProgress()
                    Toast.makeText(
                        this,
                        "Upload failed. No images saved.",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
                if (anyFailed) {
                    Toast.makeText(
                        this,
                        "Some images failed. Saving successful uploads.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                showProgress("Saving report...")
                saveReportToFirestore(
                    location,
                    description,
                    date,
                    time,
                    contact,
                    latLng,
                    uploadedUrls.toList(),
                    userId
                )
            }
        }

        photoUris.forEachIndexed { index, uri ->
            Log.d("CloudinaryUpload", "Starting upload index=$index uri=$uri")
            MediaManager.get().upload(uri)
                .option("resource_type", "image")
                .option("folder", "pawme/stray_reports")
                .option("public_id", "report_${System.currentTimeMillis()}_${index}")
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String) {
                        Log.d("CloudinaryUpload", "onStart requestId=$requestId")
                    }

                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) { }

                    override fun onSuccess(requestId: String, resultData: MutableMap<Any?, Any?>) {
                        val secureUrl = (resultData["secure_url"] ?: resultData["url"])?.toString()
                        if (!secureUrl.isNullOrBlank()) {
                            uploadedUrls.add(secureUrl)
                            Log.d("CloudinaryUpload", "Success $requestId -> $secureUrl")
                        } else {
                            anyFailed = true
                            Log.e("CloudinaryUpload", "Missing secure_url for $requestId")
                        }
                        completed.incrementAndGet()
                        updateProgress()
                    }

                    override fun onError(requestId: String, error: ErrorInfo) {
                        anyFailed = true
                        Log.e("CloudinaryUpload", "Error requestId=$requestId desc=${error.description}")
                        completed.incrementAndGet()
                        updateProgress()
                    }

                    override fun onReschedule(requestId: String, error: ErrorInfo) {
                        anyFailed = true
                        Log.w("CloudinaryUpload", "Reschedule requestId=$requestId desc=${error.description}")
                        completed.incrementAndGet()
                        updateProgress()
                    }
                })
                .dispatch()
        }
    }

    private fun saveReportToFirestore(
        location: String,
        description: String,
        date: String,
        time: String,
        contact: String,
        latLng: Pair<Double, Double>?,
        photoUrls: List<String>,
        userId: String
    ) {
        val report = hashMapOf(
            "location" to location,
            "description" to description,
            "date" to date,
            "time" to time,
            "contact" to contact,
            "photoUrls" to photoUrls,
            "userId" to userId,
            "createdAt" to System.currentTimeMillis()
        )
        latLng?.let {
            report["latitude"] = it.first
            report["longitude"] = it.second
        }

        db.collection("stray_dog_reports")
            .add(report)
            .addOnSuccessListener {
                hideProgress()
                Toast.makeText(
                    this,
                    getString(R.string.report_submitted_successfully),
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
            .addOnFailureListener { e ->
                hideProgress()
                Toast.makeText(
                    this,
                    "Failed to submit report: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun createImageFile(): File {
        val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply { currentPhotoPath = absolutePath }
    }

    private fun requestNeededPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun showProgress(message: String) {
        if (progressDialog?.isShowing == true) {
            progressDialog?.setMessage(message)
            return
        }
        progressDialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setMessage(message)
            .create()
        progressDialog?.show()
    }

    private fun hideProgress() {
        progressDialog?.dismiss()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (!(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(
                        this,
                        getString(R.string.camera_permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (!(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
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