package com.dhiva.storyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhiva.storyapp.R
import com.dhiva.storyapp.adapter.ListStoryAdapter
import com.dhiva.storyapp.adapter.LoadingStateAdapter
import com.dhiva.storyapp.data.remote.Resource
import com.dhiva.storyapp.databinding.ActivityMainBinding
import com.dhiva.storyapp.model.Story
import com.dhiva.storyapp.ui.addstory.AddStoryActivity
import com.dhiva.storyapp.ui.detail.DetailStoryActivity
import com.dhiva.storyapp.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ListStoryAdapter
    private val mainViewModel: MainViewModel by viewModels{
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
            launcherActivityAddStory.launch(intent)
        }
        binding.ibSettings.setOnClickListener {
            SettingDialog().show(
                supportFragmentManager,
                "SettingFragment"
            )
        }
        getData()
    }

    private fun getData() {
        adapter = ListStoryAdapter()
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        adapter.addLoadStateListener {
            when(it.source.refresh){
                is LoadState.Loading -> isLoadingShown(true)
                is LoadState.NotLoading -> isLoadingShown(false)
                is LoadState.Error -> showError(resources.getString(R.string.something_wrong))
            }

        }

        adapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Story) {
                val intent = Intent(this@MainActivity, DetailStoryActivity::class.java)
                intent.putExtra(EXTRA_STORY, data)
                startActivity(intent)
            }

        })

        mainViewModel.stories.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }


    private val launcherActivityAddStory = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        adapter.refresh()
    }

    private fun isLoadingShown(isShow: Boolean) {
        binding.pbLoading.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        isLoadingShown(false)
        binding.tvError.text = message
        binding.tvError.visibility = View.VISIBLE
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
        const val INTENT_RESULT = 200
    }
}