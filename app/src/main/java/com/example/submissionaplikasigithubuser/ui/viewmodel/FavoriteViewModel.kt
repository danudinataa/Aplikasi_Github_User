package com.example.submissionaplikasigithubuser.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.submissionaplikasigithubuser.data.local.FavoriteUser
import com.example.submissionaplikasigithubuser.data.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val favoriteRepository: FavoriteRepository = FavoriteRepository(application)

    private val _favoriteUserList = MutableLiveData<List<FavoriteUser>>()
    val favoriteUserList: LiveData<List<FavoriteUser>> get() = _favoriteUserList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = favoriteRepository.getAllFavoriteUser()
}