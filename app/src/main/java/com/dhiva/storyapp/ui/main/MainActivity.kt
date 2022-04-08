package com.dhiva.storyapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhiva.storyapp.R
import com.dhiva.storyapp.adapter.ListStoryAdapter
import com.dhiva.storyapp.data.remote.Resource
import com.dhiva.storyapp.databinding.ActivityMainBinding
import com.dhiva.storyapp.model.Story

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
    }

    private fun initViewModel() {
        mainViewModel.getAuthSession().observe(this) { user ->
            user.token?.let { token ->
                mainViewModel.getStories(token)
            }
        }

        mainViewModel.result.observe(this){ result ->
            when(result){
                is Resource.Loading -> isLoadingShown(true)
                is Resource.Success -> {
                    isLoadingShown(false)
                    if (result.data != null && result.data.isNotEmpty()){
                        showRecyclerList(result.data)
                    } else {
                        showError(resources.getString(R.string.no_data))
                    }
                }
                is Resource.Error -> {
                    isLoadingShown(false)
                    showError(result.message ?: resources.getString(R.string.something_wrong))
                }
            }

        }
    }

    private fun showRecyclerList(stories: List<Story>) {
        binding.rvStory.visibility = View.VISIBLE
        binding.rvStory.setHasFixedSize(true)
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        val listAdapter = ListStoryAdapter(stories)
        binding.rvStory.adapter = listAdapter

        listAdapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Story) {

            }
        })
    }

    private fun isLoadingShown(isShow: Boolean){
        binding.pbLoading.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    private fun showError(message: String){
        binding.tvError.text = message
    }
}