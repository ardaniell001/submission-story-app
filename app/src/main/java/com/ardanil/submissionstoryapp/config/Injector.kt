package com.ardanil.submissionstoryapp.config

import com.ardanil.submissionstoryapp.data.StoryRepository
import com.ardanil.submissionstoryapp.data.preference.AuthPref

object Injector {
	fun provideRepository(pref: AuthPref): StoryRepository {
		return StoryRepository(pref)
	}
	fun providePagingRepository(pref: AuthPref): StoryPagingRepository {
		return StoryPagingRepository(ApiConfig.getApiService(), pref)
	}
}