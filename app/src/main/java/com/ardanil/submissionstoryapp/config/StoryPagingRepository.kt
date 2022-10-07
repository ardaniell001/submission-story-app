package com.ardanil.submissionstoryapp.config

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ardanil.submissionstoryapp.data.preference.AuthPref
import com.ardanil.submissionstoryapp.data.response.ListStoryItem

class StoryPagingRepository(
	private val apiService: ApiService,
	private val pref: AuthPref
) {
	fun getStories(): LiveData<PagingData<ListStoryItem>> {
		return Pager(
			config = PagingConfig(
				pageSize = 5
			),
			pagingSourceFactory = {
				StoryPagingSource(apiService, pref)
			}
		).liveData
	}
}