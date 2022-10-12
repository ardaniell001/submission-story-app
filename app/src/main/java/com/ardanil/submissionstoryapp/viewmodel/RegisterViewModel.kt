package com.ardanil.submissionstoryapp.viewmodel

import androidx.lifecycle.ViewModel
import com.ardanil.submissionstoryapp.data.StoryRepository

class RegisterViewModel(private val storyRepository: StoryRepository) : ViewModel() {

	internal fun registerUser(name: String, email: String, password: String) =
		storyRepository.registerUser(name, email, password)

}