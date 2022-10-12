package com.ardanil.submissionstoryapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ardanil.submissionstoryapp.config.ApiConfig
import com.ardanil.submissionstoryapp.config.StoryPagingSource
import com.ardanil.submissionstoryapp.data.model.AuthModel
import com.ardanil.submissionstoryapp.data.preference.AuthPref
import com.ardanil.submissionstoryapp.data.response.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository(private val authPref: AuthPref) {

	private val registerLiveData = MediatorLiveData<Resource<RegisterResponse>>()
	private val loginLiveData = MediatorLiveData<Resource<LoginResponse>>()
	private val storiesLocationLiveData = MediatorLiveData<Resource<StoryResponse>>()
	private val uploadLocationLiveData = MediatorLiveData<Resource<SubmitResponse>>()

	internal fun registerUser(name: String, email: String, password: String): LiveData<Resource<RegisterResponse>> {
		registerLiveData.value = Resource(Status.LOADING)
		ApiConfig.getApiService().registerUser(name, email, password).enqueue(object :
			Callback<RegisterResponse> {
			override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
				if (response.isSuccessful) {
					response.body()?.let {
						registerLiveData.value = Resource(Status.SUCCESS, it)
					}
				} else {
					val error = response.errorBody()?.string()?.let { JSONObject(it) }
					registerLiveData.value = Resource(Status.ERROR, Throwable(error?.getString("message") ?: "Bad Request"))
				}
			}

			override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
				registerLiveData.value = Resource(Status.ERROR, t)
			}
		})
		return registerLiveData
	}

	fun login(email: String, password: String): LiveData<Resource<LoginResponse>> {
		loginLiveData.value = Resource(Status.LOADING)
		ApiConfig.getApiService().login(email, password).enqueue(object :
			Callback<LoginResponse> {
			override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
				if (response.isSuccessful) {
					response.body()?.let {
						loginLiveData.value = Resource(Status.SUCCESS, it)
					}
				} else {
					val error = response.errorBody()?.string()?.let { JSONObject(it) }
					loginLiveData.value = Resource(Status.ERROR, Throwable(error?.getString("message") ?: "Bad Request"))
				}
			}

			override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
				loginLiveData.value = Resource(Status.ERROR, t)
			}
		})
		return loginLiveData
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

	fun submitStory(image: MultipartBody.Part, description: RequestBody): LiveData<Resource<SubmitResponse>> {
		uploadLocationLiveData.value = Resource(Status.LOADING)
		val token = runBlocking { authPref.getAuth().first().token }
		ApiConfig.getApiService().submitStory("Bearer $token", image, description).enqueue(object :
			Callback<SubmitResponse> {
			override fun onResponse(
				call: Call<SubmitResponse>,
				response: Response<SubmitResponse>
			) {
				if (response.isSuccessful) {
					response.body()?.let {
						uploadLocationLiveData.value = Resource(Status.SUCCESS, it)
					}
				} else {
					val error = response.errorBody()?.string()?.let { JSONObject(it) }
					uploadLocationLiveData.value = Resource(
						Status.ERROR,
						Throwable(error?.getString("message") ?: "Bad Request")
					)
				}
			}

			override fun onFailure(call: Call<SubmitResponse>, t: Throwable) {
				uploadLocationLiveData.value = Resource(Status.ERROR, t)
			}
		})
		return uploadLocationLiveData
	}

	fun getStoriesWithLocation(): LiveData<Resource<StoryResponse>> {
		storiesLocationLiveData.value = Resource(Status.LOADING)
		val token = runBlocking { authPref.getAuth().first().token }
		ApiConfig.getApiService().getStoriesLocation(
			"Bearer $token",
			1
		).enqueue(object :
			Callback<StoryResponse> {
			override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
				if (response.isSuccessful) {
					response.body()?.let {
						storiesLocationLiveData.value = Resource(Status.SUCCESS, it)
					}
				} else {
					val error = response.errorBody()?.string()?.let { JSONObject(it) }
					storiesLocationLiveData.value = Resource(
						Status.ERROR,
						Throwable(error?.getString("message") ?: "Bad Request")
					)
				}
			}

			override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
				storiesLocationLiveData.value = Resource(Status.ERROR, t)
			}
		})
		return storiesLocationLiveData
	}

	internal suspend fun saveAuth(authModel: AuthModel) {
		authPref.saveAuth(authModel)
	}

	internal fun getAuth(): LiveData<AuthModel> {
		return authPref.getAuth().asLiveData()
	}

}