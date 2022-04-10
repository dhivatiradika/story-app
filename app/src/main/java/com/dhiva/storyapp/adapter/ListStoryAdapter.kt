package com.dhiva.storyapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dhiva.storyapp.R
import com.dhiva.storyapp.databinding.ItemStoryBinding
import com.dhiva.storyapp.model.Story
import com.dhiva.storyapp.utils.loadImage

class ListStoryAdapter(private val listStories: List<Story>) :
    RecyclerView.Adapter<ListStoryAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = listStories[position]

        holder.binding.tvName.text = story.name
        holder.binding.ivStory.loadImage(story.photoUrl)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(story) }
    }

    override fun getItemCount(): Int = listStories.size

    interface OnItemClickCallback {
        fun onItemClicked(data: Story)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = ItemStoryBinding.bind(itemView)
    }
}