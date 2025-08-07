package com.example.pawmepetadoptionapp.adopter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pawmepetadoptionapp.R
import com.example.pawmepetadoptionapp.SignInActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okio.IOException
import okio.source
import java.io.File



class AdopterProfileActivity : AppCompatActivity() {

    private lateinit var tvUsername: TextView
    private lateinit var tvEmail: TextView
    private lateinit var etName: EditText
    private lateinit var etAge: EditText
    private lateinit var etAddress: EditText
    private lateinit var etMobile: EditText
    private lateinit var etNic: EditText
    private lateinit var ivProfilePic: ImageView
    private lateinit var btnUploadPic: Button
    private lateinit var btnSave: Button
    private lateinit var progressBar: ProgressBar
    private var selectedProfilePicUri: Uri? = null

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Replace with your Cloudinary credentials
    private val CLOUD_NAME = "dsgdfsh7o"
    private val API_KEY = "518841528965393"
    private val API_SECRET = "PakHabw-EJTGs1fQOFjlXgNex5U"

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            selectedProfilePicUri = result.data!!.data
            ivProfilePic.setImageURI(selectedProfilePicUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adopter_profile)

        tvUsername = findViewById(R.id.tvUsername)
        tvEmail = findViewById(R.id.tvEmail)
        etName = findViewById(R.id.etName)
        etAge = findViewById(R.id.etAge)
        etAddress = findViewById(R.id.etAddress)
        etMobile = findViewById(R.id.etMobile)
        etNic = findViewById(R.id.etNic)
        ivProfilePic = findViewById(R.id.ivProfilePic)
        btnUploadPic = findViewById(R.id.btnUploadPic)
        btnSave = findViewById(R.id.btnSave)
        progressBar = findViewById(R.id.profileProgressBar)

        loadProfile()

        btnUploadPic.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageLauncher.launch(intent)
        }

        btnSave.setOnClickListener {
            saveProfile()
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, AdoptionActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    val intent = Intent(this, AdopterProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_logout ->{
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun loadProfile() {
        progressBar.visibility = View.VISIBLE
        val currentUid = auth.currentUser?.uid
        if (currentUid != null) {
            db.collection("users")
                .document(currentUid)
                .get()
                .addOnSuccessListener { document ->
                    val user = document.toObject(User::class.java)
                    progressBar.visibility = View.GONE
                    if (user != null && user.role == "Adopter") {
                        tvUsername.text = "Username: ${user.username}"
                        tvEmail.text = "Email: ${user.email}"
                        tvUsername.visibility = View.VISIBLE
                        tvEmail.visibility = View.VISIBLE

                        etName.setText(user.name ?: "")
                        etAge.setText(user.age ?: "")
                        etAddress.setText(user.address ?: "")
                        etMobile.setText(user.mobile ?: "")
                        etNic.setText(user.nic ?: "")

                        if (!user.profilePicUrl.isNullOrEmpty()) {
                            Glide.with(this)
                                .load(user.profilePicUrl)
                                .placeholder(R.drawable.ic_profile_placeholder)
                                .circleCrop()
                                .into(ivProfilePic)
                        } else {
                            ivProfilePic.setImageResource(R.drawable.ic_profile_placeholder)
                        }
                    } else {
                        tvUsername.visibility = View.GONE
                        tvEmail.visibility = View.GONE
                        Toast.makeText(this, "You are not an adopter", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
                }
        } else {
            progressBar.visibility = View.GONE
            Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveProfile() {
        val currentUid = auth.currentUser?.uid ?: return
        val updatedData = hashMapOf<String, Any?>(
            "name" to etName.text.toString(),
            "age" to etAge.text.toString(),
            "address" to etAddress.text.toString(),
            "mobile" to etMobile.text.toString(),
            "nic" to etNic.text.toString()
        )

        progressBar.visibility = View.VISIBLE

        if (selectedProfilePicUri != null) {
            uploadImageToCloudinaryWithAuth(selectedProfilePicUri!!) { url ->
                if (url != null) {
                    updatedData["profilePicUrl"] = url
                    updateUserData(currentUid, updatedData)
                } else {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            updateUserData(currentUid, updatedData)
        }
    }

    private fun updateUserData(uid: String, data: Map<String, Any?>) {
        db.collection("users").document(uid)
            .update(data)
            .addOnSuccessListener {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                loadProfile()
            }
            .addOnFailureListener {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToCloudinaryWithAuth(uri: Uri, callback: (String?) -> Unit) {
        val CLOUDINARY_UPLOAD_URL = "https://api.cloudinary.com/v1_1/$CLOUD_NAME/image/upload"

        // 1. Copy the Uri content to a temp file
        val tempFile = File.createTempFile("upload", ".jpg", cacheDir)
        try {
            contentResolver.openInputStream(uri)?.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: Exception) {
            callback(null)
            return
        }

        // 2. Prepare Cloudinary params
        val timestamp = (System.currentTimeMillis() / 1000).toString()
        val paramsToSign = "timestamp=$timestamp"
        val signature = sha1(paramsToSign + API_SECRET)

        // 3. Build the request body
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", tempFile.name, tempFile.asRequestBody("image/*".toMediaType()))
            .addFormDataPart("api_key", API_KEY)
            .addFormDataPart("timestamp", timestamp)
            .addFormDataPart("signature", signature)
            .build()

        // 4. Create and send the request
        val request = Request.Builder()
            .url(CLOUDINARY_UPLOAD_URL)
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                tempFile.delete()
                runOnUiThread { callback(null) }
            }

            override fun onResponse(call: Call, response: Response) {
                tempFile.delete()
                val resp = response.body?.string()
                val url = resp?.let { extractCloudinaryUrl(it) }
                runOnUiThread { callback(url) }
            }
        })
    }
    private fun sha1(input: String): String {
        val bytes = input.toByteArray()
        val md = java.security.MessageDigest.getInstance("SHA-1")
        val result = md.digest(bytes)
        return result.joinToString("") { "%02x".format(it) }
    }

    private fun extractCloudinaryUrl(response: String): String? {
        // Simple extraction, for production use a real JSON parser!
        val regex = "\"secure_url\":\"([^\"]+)\"".toRegex()
        val match = regex.find(response)
        return match?.groups?.get(1)?.value?.replace("\\/", "/")
    }
}

data class User(
    var username: String? = null,
    var email: String? = null,
    var role: String? = null,
    var name: String? = null,
    var age: String? = null,
    var address: String? = null,
    var mobile: String? = null,
    var nic: String? = null,
    var profilePicUrl: String? = null
)