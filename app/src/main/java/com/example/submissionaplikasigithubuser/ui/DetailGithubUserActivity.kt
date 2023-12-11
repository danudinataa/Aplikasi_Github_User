package com.example.submissionaplikasigithubuser.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.submissionaplikasigithubuser.R
import com.example.submissionaplikasigithubuser.data.response.DetailUserResponse
import com.example.submissionaplikasigithubuser.databinding.ActivityDetailGithubUserBinding
import com.example.submissionaplikasigithubuser.ui.adapter.SectionAdapter
import com.example.submissionaplikasigithubuser.ui.viewmodel.DetailGithubUserViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailGithubUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailGithubUserBinding
    private lateinit var viewModel: DetailGithubUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailGithubUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_URL)
        val htmlUrl = intent.getStringExtra(EXTRA_HTML)

        if (!username.isNullOrEmpty()) {
            viewModel = ViewModelProvider(this)[DetailGithubUserViewModel::class.java]
            viewModel.getDetailUser(username)

            viewModel.detailUserLiveData.observe(this) { dataUser ->
                // Update UI with the user data
                dataUser?.let { updateUI(it) }
            }

            viewModel.loadingLiveData.observe(this) { isLoading ->
                // Show/hide loading indicator
                showLoading(isLoading)
            }

            viewModel.errorLiveData.observe(this) { errorMessage ->
                // Handle error messages
                showToast(errorMessage)
            }
        }

        var isChecked =  false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.toggleFavorite.isChecked = true
                        isChecked = true
                    } else {
                        binding.toggleFavorite.isChecked = false
                        isChecked = false
                    }
                }
            }
        }

        binding.toggleFavorite.setOnClickListener {
            isChecked = !isChecked
            if (isChecked) viewModel.addToFavorite(username.toString(), id, avatarUrl.toString(),
                htmlUrl.toString())
            else viewModel.removeFromFavorite(id)
            binding.toggleFavorite.isChecked = isChecked
        }

        val sectionAdapter = SectionAdapter(this, Bundle().apply {
            putString(EXTRA_USERNAME, username)
        })

        val viewPager = binding.viewPager
        viewPager.adapter = sectionAdapter
        val tabs = binding.tabFollow
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun updateUI(dataUser: DetailUserResponse) {
        // Update UI with user data
        with(binding) {
            Glide.with(this@DetailGithubUserActivity)
                .load(dataUser.avatarUrl)
                .into(imgItemAvatar)
            tvItemName.text = dataUser.name
            tvItemUsername.text = dataUser.login
            tvItemCountFollowers.text = dataUser.followers.toString()
            tvItemCountFollowing.text = dataUser.following.toString()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )

        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
        const val EXTRA_HTML = "extra_html"
    }
}
