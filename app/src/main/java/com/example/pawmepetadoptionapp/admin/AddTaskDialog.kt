package com.example.pawmepetadoptionapp.admin

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.pawmepetadoptionapp.R
import com.example.pawmepetadoptionapp.admin.model.Task
import com.google.firebase.firestore.FirebaseFirestore

class AddTaskDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_add_task, null)

        val etName = view.findViewById<EditText>(R.id.etTaskName)
        val etTime = view.findViewById<EditText>(R.id.etTaskTime)
        val etLocation = view.findViewById<EditText>(R.id.etTaskLocation)
        val btnAdd = view.findViewById<Button>(R.id.btnAdd)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .setTitle("Add New Task")
            .setNegativeButton("Cancel", null)
            .create()

        btnAdd.setOnClickListener {
            val task = Task(
                name = etName.text.toString(),
                time = etTime.text.toString(),
                location = etLocation.text.toString(),
                status = "unassigned"
            )

            FirebaseFirestore.getInstance()
                .collection("tasks")
                .add(task) // Auto-generated document ID

            dialog.dismiss()
        }

        return dialog
    }
}
