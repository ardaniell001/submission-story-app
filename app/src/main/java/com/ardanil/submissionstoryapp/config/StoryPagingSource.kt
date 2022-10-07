package com.ardanil.submissionstoryapp.config

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ardanil.submissionstoryapp.data.preference.AuthPref
import com.ardanil.submissionstoryapp.data.response.ListStoryItem
import kotlinx.coroutines.flow.first

class StoryPagingSource(
	private val apiService: ApiService,
	private val pref: AuthPref
) : PagingSource<Int, ListStoryItem>() {

	override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
		return state.anchorPosition?.let {
			val anchorPage = state.closestPageToPosition(it)
			anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
		}
	}

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
		return try {
			val position = params.key ?: PAGE
			val token = pref.getAuth().first().token
			if (token.isNotEmpty()) {
				val response = apiService.getStories(
					"Bearer $token",
					position,
					params.loadSize
				)
				LoadResult.Page(
					data = response.body()?.listStory ?: emptyList(),
					prevKey = if (position == PAGE) null else position - 1,
					nextKey = if (response.body()?.listStory.isNullOrEmpty()) null else position + 1
				)
			} else {
				LoadResult.Error(Exception("Failed"))
			}
		} catch (e: Exception) {
			return LoadResult.Error(e)
		}
	}

	companion object {
		const val PAGE = 1
	}

}