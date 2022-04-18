package com.dhiva.storyapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dhiva.storyapp.R
import com.dhiva.storyapp.data.remote.response.ListStoryItem
import com.dhiva.storyapp.databinding.ItemStoryBinding
import com.dhiva.storyapp.model.Story
import com.dhiva.storyapp.model.toModel
import com.dhiva.storyapp.utils.loadImage

class ListStoryAdapter :
    PagingDataAdapter<ListStoryItem, ListStoryAdapter.ListViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val story = getItem(position)?.toModel()

        story?.let { storyItem ->
            holder.binding.tvName.text = storyItem.name
            holder.binding.ivStory.loadImage(storyItem.photoUrl)
            holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(storyItem) }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Story)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = ItemStoryBinding.bind(itemView)
    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}