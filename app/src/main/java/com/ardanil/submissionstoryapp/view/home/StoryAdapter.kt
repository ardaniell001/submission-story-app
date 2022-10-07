package com.ardanil.submissionstoryapp.view.home

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.ardanil.submissionstoryapp.data.response.ListStoryItem
import com.ardanil.submissionstoryapp.databinding.ItemStoryBinding

class StoryAdapter(
	private val onItemClick: (ListStoryItem, ItemStoryBinding) -> Unit
) : PagingDataAdapter<ListStoryItem, StoryViewHolder>(DIFF_CALLBACK) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
		return StoryViewHolder.newInstance(parent)
	}

	override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
		val item = getItem(position)
		if (item != null) {
			holder.render(item, onItemClick)
		}
	}

	companion object {
		val DIFF_CALLBACK = object: DiffUtil.ItemCallback<ListStoryItem>() {

			override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
				return oldItem.id == newItem.id
			}

			override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
				return oldItem.id == newItem.id && oldItem.createdAt == newItem.createdAt
			}

		}
	}
}