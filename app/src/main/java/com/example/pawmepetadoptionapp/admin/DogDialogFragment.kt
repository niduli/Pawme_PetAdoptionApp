// src/main/java/com/example/pawmepetadoptionapp/admin/DogDialogFragment.kt
package com.example.pawmepetadoptionapp.admin

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pawmepetadoptionapp.admin.model.Dog
import com.example.pawmepetadoptionapp.databinding.DialogAddEditDogBinding
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback

class DogDialogFragment(
    private val dog: Dog? = null,
    private val onSave: (Dog) -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogAddEditDogBinding
    private var selectedImageUri: Uri? = null
    private var imageUrl: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddEditDogBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireContext())
            .setTitle(if (dog == null) "Add Dog" else "Edit Dog")
            .setView(binding.root)
            .setPositiveButton("Save") { _, _ ->
                val name = binding.etName.text.toString().trim()
                val breed = binding.etBreed.text.toString().trim()
                val age = binding.etAge.text.toString().toIntOrNull() ?: 0
                val duration = binding.etDuration.text.toString().trim()
                val needs = binding.etNeeds.text.toString().trim()
                val type = binding.spinnerType.selectedItem.toString()
                val isAvailable = binding.switchAvailability.isChecked

                if (name.isEmpty() || breed.isEmpty() || type.isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if (selectedImageUri != null) {
                    uploadImageAndSaveDog(
                        name, breed, age, duration, needs, isAvailable, type
                    )
                } else {
                    val newDog = Dog(
                        name = name,
                        breed = breed,
                        age = age,
                        duration = duration,
                        needs = needs,
                        isAvailable = isAvailable,
                        imageName = imageUrl,
                        type = type
                    )
                    onSave(newDog)
                }
            }
            .setNegativeButton("Cancel", null)

        val typeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("adoption", "foster")
        )
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerType.adapter = typeAdapter

        dog?.let {
            binding.etName.setText(it.name)
            binding.etBreed.setText(it.breed)
            binding.etAge.setText(it.age.toString())
            binding.etDuration.setText(it.duration)
            binding.etNeeds.setText(it.needs)
            binding.switchAvailability.isChecked = it.isAvailable
            val spinnerPosition = typeAdapter.getPosition(it.type)
            binding.spinnerType.setSelection(spinnerPosition)
            imageUrl = it.imageName
            // Optionally load imageUrl into ivDogImage using Glide/Picasso
        }

        binding.btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        return builder.create()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            binding.ivDogImage.setImageURI(selectedImageUri)
        }
    }

    private fun uploadImageAndSaveDog(
        name: String, breed: String, age: Int, duration: String, needs: String,
        isAvailable: Boolean, type: String
    ) {
        selectedImageUri?.let { uri ->
            MediaManager.get().upload(uri)
                .option("resource_type", "image")
                .option("folder", "dog_images")
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String) {}
                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}
                    override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                        val imageUrl = resultData["secure_url"] as? String ?: ""
                        val newDog = Dog(
                            name = name,
                            breed = breed,
                            age = age,
                            duration = duration,
                            needs = needs,
                            isAvailable = isAvailable,
                            imageName = imageUrl,
                            type = type
                        )
                        onSave(newDog)
                    }
                    override fun onError(requestId: String, error: ErrorInfo) {
                        Toast.makeText(requireContext(), "Image upload failed", Toast.LENGTH_SHORT).show()
                    }
                    override fun onReschedule(requestId: String, error: ErrorInfo) {}
                })
                .dispatch()
        }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1001
    }
}