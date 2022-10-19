package com.ardanil.submissionstoryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ardanil.submissionstoryapp.data.Resource
import com.ardanil.submissionstoryapp.data.Status
import com.ardanil.submissionstoryapp.data.StoryRepository
import com.ardanil.submissionstoryapp.data.model.AuthModel
import com.ardanil.submissionstoryapp.data.response.LoginResponse
import kotlinx.coroutines.launch

class AuthViewModel(private val storyRepository: StoryRepository) : ViewModel() {

	internal val loginLiveData = MutableLiveData<Resource<LoginResponse>>()

	internal fun saveAuth(authModel: AuthModel) {
		viewModelScope.launch {
			storyRepository.saveAuth(authModel)
		}
	}

	fun getAuth(): LiveData<AuthModel> = storyRepository.getAuth()

	fun login(email: String, password: String) {
		loginLiveData.value = Resource(Status.LOADING)
		viewModelScope.launch {
			val response = storyRepository.login(email, password)
			if (response.error != null && !response.error) {
				response.let {
					loginLiveData.value = Resource(Status.SUCCESS, response)
				}
			} else {
				loginLiveData.value = Resource(Status.ERROR, Throwable(response.message))
			}
		}
	}

}