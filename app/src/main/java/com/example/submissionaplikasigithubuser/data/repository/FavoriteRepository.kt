package com.example.submissionaplikasigithubuser.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.submissionaplikasigithubuser.data.local.FavoriteDatabase
import com.example.submissionaplikasigithubuser.data.local.FavoriteUser
import com.example.submissionaplikasigithubuser.data.local.FavoriteUserDao

class FavoriteRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteUserDao

    init {
        val db = FavoriteDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoriteUserDao()
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getFavoriteUser()

}