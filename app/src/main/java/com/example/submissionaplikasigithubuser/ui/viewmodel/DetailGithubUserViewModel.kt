package com.example.submissionaplikasigithubuser.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.submissionaplikasigithubuser.R
import com.example.submissionaplikasigithubuser.data.local.FavoriteDatabase
import com.example.submissionaplikasigithubuser.data.local.FavoriteUser
import com.example.submissionaplikasigithubuser.data.local.FavoriteUserDao
import com.example.submissionaplikasigithubuser.data.response.DetailUserResponse
import com.example.submissionaplikasigithubuser.data.retrofit.APIConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailGithubUserViewModel(application: Application) : AndroidViewModel(application) {

    private var userDao: FavoriteUserDao?
    private var userDb: FavoriteDatabase?

    init {
        userDb = FavoriteDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    private val _detailUserLiveData = MutableLiveData<DetailUserResponse?>()
    val detailUserLiveData: MutableLiveData<DetailUserResponse?>
        get() = _detailUserLiveData

    private val _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveData

    fun getDetailUser(username: String) {
        _loadingLiveData.value = true

        val client = APIConfig.getAPIService()
        client.getDetailUser(username).enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(call: Call<DetailUserResponse>, response: Response<DetailUserResponse>) {
                _loadingLiveData.value = false
                if (response.isSuccessful) {
                    val dataUser = response.body()
                    if (dataUser != null) {
                        _detailUserLiveData.value = dataUser
                    } else {
                        _errorLiveData.value = R.string.no_data.toString()
                    }
                } else {
                    _errorLiveData.value = R.string.on_response_failed.toString()
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _loadingLiveData.value = false
                _errorLiveData.value = "${R.string.on_failure_failed} ${t.message}"
            }
        })
    }

    fun addToFavorite(username: String, id: Int, avatarUrl: String, htmlUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.addToFavorite(FavoriteUser(username, id, avatarUrl, htmlUrl))
        }
    }

    suspend fun checkUser(id: Int) = userDao?.checkUser(id)

    fun removeFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromFavorite(id)
        }
    }
}
