package com.example.submissionaplikasigithubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.submissionaplikasigithubuser.R
import com.example.submissionaplikasigithubuser.data.datastore.SettingPreferences
import com.example.submissionaplikasigithubuser.data.repository.UserRepository
import com.example.submissionaplikasigithubuser.data.response.SearchResponse
import com.example.submissionaplikasigithubuser.data.response.UserResponse
import com.example.submissionaplikasigithubuser.data.retrofit.APIConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {

    private val userRepository = UserRepository(APIConfig.getAPIService())

    private val _githubUserList = MutableLiveData<List<UserResponse>>()
    val githubUserList: LiveData<List<UserResponse>> get() = _githubUserList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun loadGitHubUsers() {
        _isLoading.value = true
        userRepository.getListUser(object : Callback<ArrayList<UserResponse>> {
            override fun onResponse(call: Call<ArrayList<UserResponse>>, response: Response<ArrayList<UserResponse>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _githubUserList.value = response.body() ?: emptyList()
                } else {
                    _errorMessage.value = R.string.on_response_failed.toString()
                }
            }

            override fun onFailure(call: Call<ArrayList<UserResponse>>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "${R.string.on_failure_failed} ${t.message}"
            }
        })
    }

    fun searchGitHubUsers(query: String) {
        if (query.isEmpty()) {
            loadGitHubUsers()
            return
        }

        _isLoading.value = true
        userRepository.getSearchUser(query, object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _githubUserList.value = response.body()?.items ?: emptyList()
                } else {
                    _errorMessage.value = "${R.string.on_failure_failed} ${response.code()}"
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "${R.string.on_failure_failed} ${t.message}"
            }
        })
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}
