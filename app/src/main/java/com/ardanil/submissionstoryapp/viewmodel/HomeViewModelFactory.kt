package com.ardanil.submissionstoryapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ardanil.submissionstoryapp.config.Injector
import com.ardanil.submissionstoryapp.data.preference.AuthPref

class HomeViewModelFactory(private val authPref: AuthPref) : ViewModelProvider.NewInstanceFactory() {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return when {
			modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
				HomeViewModel(Injector.providePagingRepository(authPref), authPref) as T
			}
			else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
		}
	}

}