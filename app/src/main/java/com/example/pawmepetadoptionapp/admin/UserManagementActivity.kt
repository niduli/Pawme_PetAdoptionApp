package com.example.pawmepetadoptionapp.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawmepetadoptionapp.databinding.ActivityUserManagementBinding
import com.example.pawmepetadoptionapp.databinding.DialogAddUserBinding
import com.example.pawmepetadoptionapp.admin.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query



class UserManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserManagementBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val users = mutableListOf<User>()
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter(users) { user ->
            deleteUser(user)
        }

        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewUsers.adapter = adapter

        binding.fabAddUser.setOnClickListener {
            showAddUserDialog()
        }

        loadUsers()
    }

    private fun loadUsers() {
        firestore.collection("users")
            .orderBy("username", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                users.clear()
                for (doc in result) {
                    val user = doc.toObject(User::class.java).apply {
                        id = doc.id
                    }
                    users.add(user)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load users", Toast.LENGTH_SHORT).show()
            }
    }


    private fun showAddUserDialog() {
        val dialogBinding = DialogAddUserBinding.inflate(LayoutInflater.from(this))
        AlertDialog.Builder(this)
            .setTitle("Add New User")
            .setView(dialogBinding.root)
            .setPositiveButton("Add") { _, _ ->
                val username = dialogBinding.etName.text.toString().trim()
                val email = dialogBinding.etEmail.text.toString().trim()
                val role = dialogBinding.etRole.text.toString().trim()

                if (username.isNotEmpty() && email.isNotEmpty() && role.isNotEmpty()) {
                    val user = User(username = username, email = email, role = role)
                    firestore.collection("users")
                        .add(user)
                        .addOnSuccessListener {
                            Toast.makeText(this, "User added", Toast.LENGTH_SHORT).show()
                            loadUsers()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to add user", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun deleteUser(user: User) {
        firestore.collection("users").document(user.id!!)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show()
                loadUsers()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to delete user", Toast.LENGTH_SHORT).show()
            }
    }
}
