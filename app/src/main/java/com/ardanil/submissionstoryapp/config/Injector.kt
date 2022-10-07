package com.ardanil.submissionstoryapp.config

import com.ardanil.submissionstoryapp.data.preference.AuthPref

object Injector {
	fun providePagingRepository(pref: AuthPref): StoryPagingRepository {
		return StoryPagingRepository(ApiConfig.getApiService(), pref)
	}
}