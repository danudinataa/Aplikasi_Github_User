package com.example.submissionaplikasigithubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionaplikasigithubuser.data.response.UserFollowResponse
import com.example.submissionaplikasigithubuser.databinding.ItemGithubUserBinding

class FollowAdapter(private val followersList: ArrayList<UserFollowResponse>)
    : RecyclerView.Adapter<FollowAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(private val binding: ItemGithubUserBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserFollowResponse){
            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .skipMemoryCache(true)
                .into(binding.imgItemAvatar)
            binding.tvItemName.text = user.login
            binding.tvItemGithubUrl.text = user.htmlUrl
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder {
        return CustomViewHolder(
            ItemGithubUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return followersList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(followersList[position])
    }

}