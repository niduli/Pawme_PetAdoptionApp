package com.example.pawmepetadoptionapp.admin


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pawmepetadoptionapp.admin.model.Dog
import com.example.pawmepetadoptionapp.databinding.DialogAddEditDogBinding

class DogDialogFragment(
    private val dog: Dog? = null,
    private val onSave: (Dog) -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogAddEditDogBinding

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
                val imageName = binding.etImageName.text.toString().trim()
                val type = binding.spinnerType.selectedItem.toString()
                val isAvailable = binding.switchAvailability.isChecked

                if (name.isEmpty() || breed.isEmpty() || type.isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val newDog = Dog(
                    name = name,
                    breed = breed,
                    age = age,
                    duration = duration,
                    needs = needs,
                    isAvailable = isAvailable,
                    imageName = imageName,
                    type = type
                )

                onSave(newDog)
            }
            .setNegativeButton("Cancel", null)

        // Populate dropdown
        val typeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("adoption", "foster")
        )
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerType.adapter = typeAdapter

        // Pre-fill if editing
        dog?.let {
            binding.etName.setText(it.name)
            binding.etBreed.setText(it.breed)
            binding.etAge.setText(it.age.toString())
            binding.etDuration.setText(it.duration)
            binding.etNeeds.setText(it.needs)
            binding.etImageName.setText(it.imageName)
            binding.switchAvailability.isChecked = it.isAvailable
            val spinnerPosition = typeAdapter.getPosition(it.type)
            binding.spinnerType.setSelection(spinnerPosition)
        }

        return builder.create()
    }
}
