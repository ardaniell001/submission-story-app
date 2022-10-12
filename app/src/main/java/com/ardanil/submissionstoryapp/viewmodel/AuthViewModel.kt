package com.ardanil.submissionstoryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ardanil.submissionstoryapp.data.StoryRepository
import com.ardanil.submissionstoryapp.data.model.AuthModel
import kotlinx.coroutines.launch

class AuthViewModel(private val storyRepository: StoryRepository) : ViewModel() {

	internal fun saveAuth(authModel: AuthModel) {
		viewModelScope.launch {
			storyRepository.saveAuth(authModel)
		}
	}

	fun getAuth(): LiveData<AuthModel> = storyRepository.getAuth()

	fun login(email: String, password: String) = storyRepository.login(email, password)

}