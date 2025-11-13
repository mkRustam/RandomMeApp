package com.mkr.randomuser.presentation.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mkr.randomuser.domain.model.User
import com.mkr.randomuser.databinding.ListItemUserBinding
import com.mkr.randomuser.presentation.util.CountryUtils

class UserListAdapter(private val onItemClick: (User) -> Unit) :
    ListAdapter<User, UserListAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
    UserViewHolder {
        val binding = ListItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
        holder.itemView.setOnClickListener { onItemClick(user) }
    }

    class UserViewHolder(private val binding: ListItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.userNameTextView.text = "${user.name.first} ${user.name.last}"
            binding.userPhoneTextView.text = user.phone
            binding.userNatTextView.text = "${CountryUtils.getCountryFlag(user.nat)} ${user.nat}"
            Glide.with(binding.userImageView.context)
                .load(user.picture.thumbnail)
                .circleCrop()
                .into(binding.userImageView)
        }
    }

    private class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}