package com.example.submissionaplikasigithubuser.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionaplikasigithubuser.data.local.FavoriteUser
import com.example.submissionaplikasigithubuser.databinding.ActivityFavoriteBinding
import com.example.submissionaplikasigithubuser.ui.adapter.FavoriteAdapter
import com.example.submissionaplikasigithubuser.ui.viewmodel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity(), FavoriteAdapter.OnItemClickListener {

    private lateinit var binding: ActivityFavoriteBinding
    private val viewModel: FavoriteViewModel by viewModels()

    private var isGrid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        observeViewModel()

        viewModel.getAllFavoriteUser()
    }

    private fun setupRecyclerView(userList: List<FavoriteUser>) {
        with(binding) {
            rvFavorite.setHasFixedSize(true)
            rvFavorite.layoutManager = if (isGrid) {
                GridLayoutManager(this@FavoriteActivity, 2)
            } else {
                LinearLayoutManager(this@FavoriteActivity)
            }
            rvFavorite.adapter = FavoriteAdapter(userList, isGrid).apply {
                listener = this@FavoriteActivity
            }
        }
    }

    private fun observeViewModel() {
        viewModel.getAllFavoriteUser().observe(this) { favoriteUserList ->
            setupRecyclerView(favoriteUserList)
            Toast.makeText(this, "Data ada", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onItemClicked(item: FavoriteUser) {
        val intentGithubUserDetail = Intent(this, DetailGithubUserActivity::class.java)
        intentGithubUserDetail.putExtra(DetailGithubUserActivity.EXTRA_USERNAME, item.login)
        intentGithubUserDetail.putExtra(DetailGithubUserActivity.EXTRA_ID, item.id)
        intentGithubUserDetail.putExtra(DetailGithubUserActivity.EXTRA_URL, item.avatar_url)
        intentGithubUserDetail.putExtra(DetailGithubUserActivity.EXTRA_HTML, item.html_url)
        startActivity(intentGithubUserDetail)
    }
}