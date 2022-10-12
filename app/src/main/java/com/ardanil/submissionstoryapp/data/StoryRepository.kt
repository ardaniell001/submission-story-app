package com.ardanil.submissionstoryapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import com.ardanil.submissionstoryapp.config.ApiConfig
import com.ardanil.submissionstoryapp.data.model.AuthModel
import com.ardanil.submissionstoryapp.data.preference.AuthPref
import com.ardanil.submissionstoryapp.data.response.LoginResponse
import com.ardanil.submissionstoryapp.data.response.RegisterResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository(private val authPref: AuthPref) {

	private val registerLiveData = MediatorLiveData<Resource<RegisterResponse>>()
	private val loginLiveData = MediatorLiveData<Resource<LoginResponse>>()

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

	internal suspend fun saveAuth(authModel: AuthModel) {
		authPref.saveAuth(authModel)
	}

	internal fun getAuth(): LiveData<AuthModel> {
		return authPref.getAuth().asLiveData()
	}

}