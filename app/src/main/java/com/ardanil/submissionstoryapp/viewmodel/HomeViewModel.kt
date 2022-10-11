package com.ardanil.submissionstoryapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ardanil.submissionstoryapp.config.ApiConfig
import com.ardanil.submissionstoryapp.config.StoryPagingRepository
import com.ardanil.submissionstoryapp.data.Resource
import com.ardanil.submissionstoryapp.data.Status
import com.ardanil.submissionstoryapp.data.preference.AuthPref
import com.ardanil.submissionstoryapp.data.response.StoryResponse
import com.ardanil.submissionstoryapp.data.response.SubmitResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
	pagingRepository: StoryPagingRepository,
	private val authPref: AuthPref
) : ViewModel() {

	private val uploadMutableLiveData = MutableLiveData<Resource<SubmitResponse>>()
	internal val uploadLiveData = uploadMutableLiveData

	private val storiesLocationMutableLiveData = MutableLiveData<Resource<StoryResponse>>()
	internal val storiesLocationLiveData = storiesLocationMutableLiveData

	private var token: String = ""

	val getStories = pagingRepository.getStories().cachedIn(viewModelScope)

	init {
		viewModelScope.launch {
			token = authPref.getAuth().first().token
		}
	}

	fun submitStory(image: MultipartBody.Part, description: RequestBody) {
		uploadLiveData.value = Resource(Status.LOADING)
		ApiConfig.getApiService().submitStory("Bearer $token", image, description).enqueue(object :
			Callback<SubmitResponse> {
			override fun onResponse(
				call: Call<SubmitResponse>,
				response: Response<SubmitResponse>
			) {
				if (response.isSuccessful) {
					response.body()?.let {
						uploadLiveData.value = Resource(Status.SUCCESS, it)
					}
				} else {
					val error = response.errorBody()?.string()?.let { JSONObject(it) }
					uploadLiveData.value = Resource(
						Status.ERROR,
						Throwable(error?.getString("message") ?: "Bad Request")
					)
				}
			}

			override fun onFailure(call: Call<SubmitResponse>, t: Throwable) {
				uploadLiveData.value = Resource(Status.ERROR, t)
			}
		})
	}

	fun getStoriesWithLocation() {
		storiesLocationMutableLiveData.value = Resource(Status.LOADING)
		ApiConfig.getApiService().getStoriesLocation(
			"Bearer $token",
			1
		).enqueue(object :
			Callback<StoryResponse> {
			override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
				if (response.isSuccessful) {
					response.body()?.let {
						storiesLocationMutableLiveData.value = Resource(Status.SUCCESS, it)
					}
				} else {
					val error = response.errorBody()?.string()?.let { JSONObject(it) }
					storiesLocationMutableLiveData.value = Resource(
						Status.ERROR,
						Throwable(error?.getString("message") ?: "Bad Request")
					)
				}
			}

			override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
				storiesLocationMutableLiveData.value = Resource(Status.ERROR, t)
			}
		})
	}

}