package com.example.submissionaplikasigithubuser.data.repository

import com.example.submissionaplikasigithubuser.data.response.SearchResponse
import com.example.submissionaplikasigithubuser.data.response.UserResponse
import com.example.submissionaplikasigithubuser.data.retrofit.APIService
import retrofit2.Callback

class UserRepository(private val apiService: APIService) {

    fun getListUser(callback: Callback<ArrayList<UserResponse>>) {
        apiService.getListUser().enqueue(callback)
    }

    fun getSearchUser(query: String, callback: Callback<SearchResponse>) {
        apiService.getSearchUser(query).enqueue(callback)
    }
}
