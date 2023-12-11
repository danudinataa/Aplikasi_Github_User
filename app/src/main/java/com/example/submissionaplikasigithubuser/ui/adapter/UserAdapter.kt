package com.example.submissionaplikasigithubuser.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionaplikasigithubuser.data.response.UserResponse
import com.example.submissionaplikasigithubuser.databinding.ItemGithubUserBinding

class UserAdapter(private val githubUserList: List<UserResponse>, private val isGrid: Boolean)
    : RecyclerView.Adapter<UserAdapter.CustomViewHolder>() {

    lateinit var listener: OnItemClickListener

    inner class CustomViewHolder(private val binding: ItemGithubUserBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bindList(user: UserResponse){
            if (isGrid) {
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .skipMemoryCache(true)
                    .into(binding.imgItemAvatar)
                binding.tvItemName.text = user.login
                binding.imgGithubLogo.visibility = View.GONE
            } else {
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .skipMemoryCache(true)
                    .into(binding.imgItemAvatar)
                binding.tvItemName.text = user.login
                binding.tvItemGithubUrl.text = user.htmlUrl
                binding.imgGithubLogo.visibility = View.VISIBLE
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemGithubUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return githubUserList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindList(githubUserList[position])
        holder.itemView.setOnClickListener {
            listener.onItemClicked(githubUserList[position])
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(item: UserResponse)
    }
}