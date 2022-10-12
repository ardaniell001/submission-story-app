package com.ardanil.submissionstoryapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ardanil.submissionstoryapp.config.Injector
import com.ardanil.submissionstoryapp.data.StoryRepository
import com.ardanil.submissionstoryapp.data.preference.AuthPref

class HomeViewModelFactory(private val storeRepository: StoryRepository) : ViewModelProvider.NewInstanceFactory() {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return when {
			modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
				HomeViewModel(storeRepository) as T
			}
			else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
		}
	}

	companion object {
		@Volatile
		private var instance: HomeViewModelFactory? = null
		fun getInstance(authPref: AuthPref): HomeViewModelFactory =
			instance ?: synchronized(this) {
				instance ?: HomeViewModelFactory(Injector.provideRepository(authPref))
			}.also { instance = it }
	}

}