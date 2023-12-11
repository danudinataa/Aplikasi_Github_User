package com.example.submissionaplikasigithubuser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionaplikasigithubuser.R
import com.example.submissionaplikasigithubuser.data.response.UserFollowResponse
import com.example.submissionaplikasigithubuser.data.retrofit.APIConfig
import com.example.submissionaplikasigithubuser.databinding.FragmentFollowersBinding
import com.example.submissionaplikasigithubuser.ui.adapter.FollowAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FollowersFragment : Fragment() {

    private val binding by lazy {
        FragmentFollowersBinding.inflate(layoutInflater)
    }

    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = arguments?.getString(DetailGithubUserActivity.EXTRA_USERNAME)

        if (!username.isNullOrEmpty()) {
            showRecyclerView()
        }
    }

    private fun showRecyclerView() {
        showLoading(true)

        val client = APIConfig.getAPIService()
        client.getFollowers(username ?: "").enqueue(object : Callback<ArrayList<UserFollowResponse>> {
            override fun onResponse(call: Call<ArrayList<UserFollowResponse>>, response: Response<ArrayList<UserFollowResponse>>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val followersList: ArrayList<UserFollowResponse> = response.body() ?: arrayListOf()

                    with(binding.rvFollowers) {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = FollowAdapter(followersList)
                    }
                } else {
                    showToast(R.string.on_response_failed.toString())
                }
            }

            override fun onFailure(call: Call<ArrayList<UserFollowResponse>>, t: Throwable) {
                showToast("${R.string.on_failure_failed} ${t.message}")
            }
        })
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}