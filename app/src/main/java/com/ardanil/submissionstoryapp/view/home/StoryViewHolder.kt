package com.ardanil.submissionstoryapp.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ardanil.submissionstoryapp.config.DateUtils
import com.ardanil.submissionstoryapp.data.response.ListStoryItem
import com.ardanil.submissionstoryapp.databinding.ItemStoryBinding
import com.bumptech.glide.Glide

class StoryViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {

	companion object {
		fun newInstance(parent: ViewGroup): StoryViewHolder {
			val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
			return StoryViewHolder(binding)
		}
	}

	fun render(
		item: ListStoryItem,
		onItemClick: (ListStoryItem, ItemStoryBinding) -> Unit
	) {
		binding.root.context ?: return
		binding.apply {
			tvNameInitial.text = item.name?.firstOrNull()?.toString()?.uppercase() ?: "?"
			tvName.text = item.name
			tvDescription.text = item.description
			val stringToDate = item.createdAt?.let {
				DateUtils.stringToDate(it)
			}
			tvDate.text = stringToDate?.let { DateUtils.dateToString(it) } ?: ""
			Glide.with(root.context)
				.load(item.photoUrl)
				.into(ivPhoto)
			root.setOnClickListener {
				onItemClick(item, binding)
			}
		}
	}
}