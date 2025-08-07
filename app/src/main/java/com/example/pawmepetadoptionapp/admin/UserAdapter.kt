package com.example.pawmepetadoptionapp.admin


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmepetadoptionapp.databinding.ItemUserBinding
import com.example.pawmepetadoptionapp.admin.model.User

class UserAdapter(
    private val users: List<User>,
    private val onDeleteClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.binding.tvName.text = user.username // <-- here username
        holder.binding.tvEmail.text = user.email
        holder.binding.tvRole.text = user.role
        holder.binding.btnDelete.setOnClickListener {
            onDeleteClick(user)
        }
    }

}
