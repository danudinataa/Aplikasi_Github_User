package com.example.submissionaplikasigithubuser.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionaplikasigithubuser.R
import com.example.submissionaplikasigithubuser.data.datastore.SettingPreferences
import com.example.submissionaplikasigithubuser.data.datastore.dataStore
import com.example.submissionaplikasigithubuser.data.response.UserResponse
import com.example.submissionaplikasigithubuser.databinding.ActivityMainBinding
import com.example.submissionaplikasigithubuser.ui.DetailGithubUserActivity.Companion.EXTRA_HTML
import com.example.submissionaplikasigithubuser.ui.DetailGithubUserActivity.Companion.EXTRA_ID
import com.example.submissionaplikasigithubuser.ui.DetailGithubUserActivity.Companion.EXTRA_URL
import com.example.submissionaplikasigithubuser.ui.DetailGithubUserActivity.Companion.EXTRA_USERNAME
import com.example.submissionaplikasigithubuser.ui.adapter.UserAdapter
import com.example.submissionaplikasigithubuser.ui.viewmodel.MainViewModel
import com.example.submissionaplikasigithubuser.ui.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity(), UserAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var pref: SettingPreferences
    private val viewModel: MainViewModel by viewModels{ MainViewModelFactory(pref)}

    private var isGrid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = SettingPreferences.getInstance(application.dataStore)

        viewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }

        binding.switchTheme.setThumbResource(R.drawable.thumb)

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }

        binding.btnFavorite.setOnClickListener {
            startActivity(
                Intent(this, FavoriteActivity::class.java)
            )
        }

        setupViews()
        observeViewModel()
        viewModel.loadGitHubUsers()
    }

    private fun setupRecyclerView(userList: List<UserResponse>) {
        with(binding) {
            rvGithubUser.setHasFixedSize(true)
            rvGithubUser.layoutManager = if (isGrid) {
                GridLayoutManager(this@MainActivity, 2)
            } else {
                LinearLayoutManager(this@MainActivity)
            }
            rvGithubUser.adapter = UserAdapter(userList, isGrid).apply {
                listener = this@MainActivity
            }
        }
    }

    private fun setupViews() {
        with(binding) {
            btnSearch.setOnClickListener {
                showSearchRecyclerView()
            }

            etQuery.setOnKeyListener { _, keyCode, keyEvent ->
                if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    showSearchRecyclerView()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }

            etQuery.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s.isNullOrBlank()) showRecyclerView()
                    else showSearchRecyclerView()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            })

            fabGrid.setOnClickListener {
                isGrid = !isGrid

                if (isGrid) showRecyclerView() else showSearchRecyclerView()
                Toast.makeText(this@MainActivity,
                    "Change to ${if (isGrid) "Grid" else "List"}",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.githubUserList.observe(this) { githubUserList ->
            setupRecyclerView(githubUserList)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            showToast(errorMessage)
        }
    }

    private fun showRecyclerView() {
        viewModel.loadGitHubUsers()
    }

    private fun showSearchRecyclerView() {
        val query = binding.etQuery.text.toString()
        viewModel.searchGitHubUsers(query)
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClicked(item: UserResponse) {
        val intentGithubUserDetail = Intent(this, DetailGithubUserActivity::class.java)
        intentGithubUserDetail.putExtra(EXTRA_USERNAME, item.login)
        intentGithubUserDetail.putExtra(EXTRA_ID, item.id)
        intentGithubUserDetail.putExtra(EXTRA_URL, item.avatarUrl)
        intentGithubUserDetail.putExtra(EXTRA_HTML, item.htmlUrl)
        startActivity(intentGithubUserDetail)
    }
}