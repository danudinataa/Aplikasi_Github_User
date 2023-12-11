package com.example.submissionaplikasigithubuser.data.retrofit

import com.example.submissionaplikasigithubuser.BuildConfig
import com.example.submissionaplikasigithubuser.data.response.DetailUserResponse
import com.example.submissionaplikasigithubuser.data.response.SearchResponse
import com.example.submissionaplikasigithubuser.data.response.UserFollowResponse
import com.example.submissionaplikasigithubuser.data.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    @GET("users")
    @Headers("Authorization: token $API_TOKEN")
    fun getListUser() : Call<ArrayList<UserResponse>>

    @GET("users/{username}")
    @Headers("Authorization: token $API_TOKEN")
    fun getDetailUser(
        @Path("username") username: String
    ) : Call<DetailUserResponse>

    @GET("search/users?q=")
    @Headers("Authorization: token $API_TOKEN")
    fun getSearchUser(
        @Query("q")
        username: String
    ) : Call<SearchResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token $API_TOKEN")
    fun getFollowers(
        @Path("username")
        username: String
    ) : Call<ArrayList<UserFollowResponse>>

    @GET("users/{username}/following")
    @Headers("Authorization: token $API_TOKEN")
    fun getFollowing(
        @Path("username")
        username: String
    ) : Call<ArrayList<UserFollowResponse>>

    companion object {
        private const val API_TOKEN = BuildConfig.API_KEY
    }
}