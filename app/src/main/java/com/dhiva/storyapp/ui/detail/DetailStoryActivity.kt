package com.dhiva.storyapp.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dhiva.storyapp.databinding.ActivityDetailStoryBinding
import com.dhiva.storyapp.model.Story
import com.dhiva.storyapp.ui.main.MainActivity
import com.dhiva.storyapp.utils.loadImage

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        getIntentData()
    }

    private fun init() {
        binding.ibBack.setOnClickListener { finish() }
    }

    private fun getIntentData() {
        val story = intent.getParcelableExtra<Story>(MainActivity.EXTRA_STORY)
        story?.let { it ->
            with(binding) {
                ivStory.loadImage(it.photoUrl)
                tvName.text = it.name
                tvDescription.text = it.description
            }
        }
    }
}