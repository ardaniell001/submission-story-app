package com.ardanil.submissionstoryapp.config

import com.ardanil.submissionstoryapp.data.StoryRepository
import com.ardanil.submissionstoryapp.data.preference.AuthPref

object Injector {
	fun provideRepository(pref: AuthPref): StoryRepository {
		return StoryRepository(pref, ApiConfig.getApiService())
	}
}