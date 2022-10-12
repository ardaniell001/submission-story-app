package com.ardanil.submissionstoryapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ardanil.submissionstoryapp.data.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class HomeViewModel(
	private val storyRepository: StoryRepository
) : ViewModel() {

	fun getStories() = storyRepository.getStories().cachedIn(viewModelScope)

	fun submitStory(image: MultipartBody.Part, description: RequestBody) = storyRepository.submitStory(image, description)

	fun getStoriesWithLocation() = storyRepository.getStoriesWithLocation()

}