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
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.UploadCallback
import com.example.pawmepetadoptionapp.R
import com.example.pawmepetadoptionapp.SignInActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.cloudinary.android.callback.ErrorInfo

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

        // Optional: ensure actual white color is applied
        bottomNav.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))

        // Mark the current tab as selected so it stays highlighted
        bottomNav.selectedItemId = R.id.nav_profile

        // Avoid re-launching the same screen and keep selection state correct
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    if (bottomNav.selectedItemId != R.id.nav_home) {
                        startActivity(Intent(this, AdoptionActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                    }
                    true
                }
                R.id.nav_profile -> {
                    // Already here; do nothing, keep it selected
                    true
                }
                R.id.action_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(
                        Intent(this, SignInActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                    )
                    true
                }
                else -> false
            }
        }

        // Optional: ignore reselection to prevent reloading current screen
        bottomNav.setOnItemReselectedListener { /* no-op */ }
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
            uploadImageToCloudinary(selectedProfilePicUri!!) { url ->
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

    private fun uploadImageToCloudinary(uri: Uri, callback: (String?) -> Unit) {
        MediaManager.get().upload(uri)
            .option("resource_type", "image")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {}
                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}
                override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                    val url = resultData?.get("secure_url") as? String
                    runOnUiThread { callback(url) }
                }
                override fun onError(requestId: String?, error: ErrorInfo?) {
                    runOnUiThread { callback(null) }
                }
                override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                    runOnUiThread { callback(null) }
                }
            }).dispatch()
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