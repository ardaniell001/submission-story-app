package com.ardanil.submissionstoryapp.config

import com.ardanil.submissionstoryapp.data.response.LoginResponse
import com.ardanil.submissionstoryapp.data.response.RegisterResponse
import com.ardanil.submissionstoryapp.data.response.StoryResponse
import com.ardanil.submissionstoryapp.data.response.SubmitResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

	@FormUrlEncoded
	@POST("register")
	suspend fun registerUser(
		@Field("name") name: String,
		@Field("email") email: String,
		@Field("password") password: String
	): RegisterResponse

	@FormUrlEncoded
	@POST("login")
	suspend fun login(
		@Field("email") email: String,
		@Field("password") password: String
	): LoginResponse

	@GET("stories")
	suspend fun getStories(
		@Header("Authorization") authHeader: String,
		@Query("page") page: Int,
		@Query("size") size: Int
	): Response<StoryResponse>

	@Multipart
	@POST("stories")
	suspend fun submitStory(
		@Header("Authorization") authHeader: String,
		@Part file: MultipartBody.Part,
		@Part("description") description: RequestBody,
	): SubmitResponse

	@GET("stories")
	suspend fun getStoriesLocation(
		@Header("Authorization") authHeader: String,
		@Query("location") location: Int,
	): StoryResponse

}