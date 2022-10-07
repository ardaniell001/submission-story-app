package com.ardanil.submissionstoryapp.viewmodel

import androidx.lifecycle.*
import com.ardanil.submissionstoryapp.config.ApiConfig
import com.ardanil.submissionstoryapp.data.Resource
import com.ardanil.submissionstoryapp.data.Status
import com.ardanil.submissionstoryapp.data.model.AuthModel
import com.ardanil.submissionstoryapp.data.preference.AuthPref
import com.ardanil.submissionstoryapp.data.response.LoginResponse
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(private val pref: AuthPref) : ViewModel() {

	private val loginMutableLiveData = MutableLiveData<Resource<LoginResponse>>()
	internal val loginLiveData = loginMutableLiveData

	private fun saveAuth(authModel: AuthModel) {
		viewModelScope.launch {
			pref.saveAuth(authModel)
		}
	}

	fun getAuth(): LiveData<AuthModel> {
		return pref.getAuth().asLiveData()
	}

	fun logout() {
		viewModelScope.launch {
			pref.logout()
		}
	}

	fun login(email: String, password: String) {
		loginLiveData.value = Resource(Status.LOADING)
		ApiConfig.getApiService().login(email, password).enqueue(object :
			Callback<LoginResponse> {
			override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
				if (response.isSuccessful) {
					response.body()?.let {
						saveAuth(AuthModel(
							it.loginResult?.name ?: "",
							it.loginResult?.token ?: "",
							true
						))
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
	}

}