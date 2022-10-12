package com.ardanil.submissionstoryapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.ardanil.submissionstoryapp.config.ApiConfig
import com.ardanil.submissionstoryapp.data.preference.AuthPref
import com.ardanil.submissionstoryapp.data.response.RegisterResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository(private val authPref: AuthPref) {

	private val registerLiveData = MediatorLiveData<Resource<RegisterResponse>>()

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

}