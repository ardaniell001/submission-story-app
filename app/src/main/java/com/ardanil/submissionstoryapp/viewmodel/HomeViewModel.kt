package com.ardanil.submissionstoryapp.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.ardanil.submissionstoryapp.data.Resource
import com.ardanil.submissionstoryapp.data.Status
import com.ardanil.submissionstoryapp.data.StoryRepository
import com.ardanil.submissionstoryapp.data.model.AuthModel
import com.ardanil.submissionstoryapp.data.response.StoryResponse
import com.ardanil.submissionstoryapp.data.response.SubmitResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class HomeViewModel(
	private val storyRepository: StoryRepository
) : ViewModel() {

	internal val storiesLocationLiveData = MutableLiveData<Resource<StoryResponse>>()
	internal val uploadLiveData = MediatorLiveData<Resource<SubmitResponse>>()
	internal val tokenLiveData = MutableLiveData<AuthModel>()
	internal var tokenAuth = ""

	fun getStories() = storyRepository.getStories().cachedIn(viewModelScope)

	fun submitStory(image: MultipartBody.Part, description: RequestBody) {
		uploadLiveData.value = Resource(Status.LOADING)
		viewModelScope.launch {
			val response = storyRepository.submitStory(tokenAuth, image, description)
			if (response.error != null && !response.error) {
				uploadLiveData.value = Resource(Status.SUCCESS, response)
			} else {
				uploadLiveData.value = Resource(Status.ERROR, Throwable(response.message))
			}
		}
	}

	fun getStoriesWithLocation() {
		storiesLocationLiveData.value = Resource(Status.LOADING)
		viewModelScope.launch {
			val response = storyRepository.getStoriesWithLocation(tokenAuth)
			if (response.error != null && !response.error) {
				storiesLocationLiveData.value = Resource(Status.SUCCESS, response)
			} else {
				storiesLocationLiveData.value = Resource(Status.ERROR, Throwable(response.message))
			}
		}
	}

	fun getToken() {
		viewModelScope.launch {
			val authModel = storyRepository.getAuthModel()
			tokenAuth = "Bearer ${authModel.token}"
			tokenLiveData.value = authModel
		}
	}

}