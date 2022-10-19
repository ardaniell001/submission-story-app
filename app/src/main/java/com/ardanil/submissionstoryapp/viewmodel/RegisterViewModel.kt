package com.ardanil.submissionstoryapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ardanil.submissionstoryapp.data.Resource
import com.ardanil.submissionstoryapp.data.Status
import com.ardanil.submissionstoryapp.data.StoryRepository
import com.ardanil.submissionstoryapp.data.response.RegisterResponse
import kotlinx.coroutines.launch

class RegisterViewModel(private val storyRepository: StoryRepository) : ViewModel() {

	internal val registerLiveData = MutableLiveData<Resource<RegisterResponse>>()

	internal fun registerUser(name: String, email: String, password: String) {
		registerLiveData.value = Resource(Status.LOADING)
		viewModelScope.launch {
			val response = storyRepository.registerUser(name, email, password)
			if (response.error != null && !response.error) {
				response.let {
					registerLiveData.value = Resource(Status.SUCCESS, response)
				}
			} else {
				registerLiveData.value = Resource(Status.ERROR, Throwable(response.message))
			}
		}
	}

}