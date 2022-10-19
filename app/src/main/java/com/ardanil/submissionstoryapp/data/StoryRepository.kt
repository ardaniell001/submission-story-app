package com.ardanil.submissionstoryapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ardanil.submissionstoryapp.config.ApiService
import com.ardanil.submissionstoryapp.config.StoryPagingSource
import com.ardanil.submissionstoryapp.data.model.AuthModel
import com.ardanil.submissionstoryapp.data.preference.AuthPref
import com.ardanil.submissionstoryapp.data.response.*
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.HttpException

class StoryRepository(
	private val authPref: AuthPref,
	private val apiService: ApiService
) {

	suspend fun registerUser(name: String, email: String, password: String) = try {
		val response = apiService.registerUser(name, email, password)
		response
	} catch (e: HttpException) {
		val error = e.response()?.errorBody()?.string()?.let { JSONObject(it) }
		val message = error?.getString("message") ?: "Bad Request"
		RegisterResponse(error = true, message = message)
	}

	suspend fun login(email: String, password: String): LoginResponse = try {
		val response = apiService.login(email, password)
		response
	} catch (e: HttpException) {
		val error = e.response()?.errorBody()?.string()?.let { JSONObject(it) }
		val message = error?.getString("message") ?: "Bad Request"
		LoginResponse(error = true, message = message, loginResult = null)
	}

	fun getStories(): LiveData<PagingData<ListStoryItem>> {
		return Pager(
			config = PagingConfig(
				pageSize = 5
			),
			pagingSourceFactory = {
				StoryPagingSource(authPref)
			}
		).liveData
	}

	suspend fun submitStory(token: String, image: MultipartBody.Part, description: RequestBody) : SubmitResponse = try {
		val response = apiService.submitStory(token, image, description)
		response
	} catch (e: HttpException) {
		val error = e.response()?.errorBody()?.string()?.let { JSONObject(it) }
		val message = error?.getString("message") ?: "Bad Request"
		SubmitResponse(error = true, message = message)
	}

	suspend fun getStoriesWithLocation(token: String): StoryResponse = try {
		val response = apiService.getStoriesLocation(token, 1)
		response
	} catch (e: HttpException) {
		val error = e.response()?.errorBody()?.string()?.let { JSONObject(it) }
		val message = error?.getString("message") ?: "Bad Request"
		StoryResponse(error = true, message = message, listStory = null)
	}

	internal suspend fun saveAuth(authModel: AuthModel) {
		authPref.saveAuth(authModel)
	}

	internal fun getAuth(): LiveData<AuthModel> {
		return authPref.getAuth().asLiveData()
	}

	suspend fun getAuthModel(): AuthModel = try {
		authPref.getAuth().first()
	} catch (e: Exception) {
		AuthModel(name = "", isLogin = false, token = "")
	}

}