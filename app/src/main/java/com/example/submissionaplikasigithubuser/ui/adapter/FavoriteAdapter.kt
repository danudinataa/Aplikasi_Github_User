package com.example.submissionaplikasigithubuser.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionaplikasigithubuser.data.local.FavoriteUser
import com.example.submissionaplikasigithubuser.databinding.ItemGithubUserBinding

class FavoriteAdapter(private val favoriteUserList: List<FavoriteUser>, private val isGrid: Boolean)
    : RecyclerView.Adapter<FavoriteAdapter.CustomViewHolder>() {

    lateinit var listener: OnItemClickListener

    inner class CustomViewHolder(private val binding: ItemGithubUserBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bindList(user: FavoriteUser){
            if (isGrid) {
                Glide.with(itemView.context)
                    .load(user.avatar_url)
                    .skipMemoryCache(true)
                    .into(binding.imgItemAvatar)
                binding.tvItemName.text = user.login
                binding.imgGithubLogo.visibility = View.GONE
            } else {
                Glide.with(itemView.context)
                    .load(user.avatar_url)
                    .skipMemoryCache(true)
                    .into(binding.imgItemAvatar)
                binding.tvItemName.text = user.login
                binding.tvItemGithubUrl.text = user.html_url
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
        return favoriteUserList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindList(favoriteUserList[position])
        holder.itemView.setOnClickListener {
            listener.onItemClicked(favoriteUserList[position])
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(item: FavoriteUser)
    }
}