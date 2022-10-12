package com.ardanil.submissionstoryapp.utils

import com.ardanil.submissionstoryapp.data.Resource
import com.ardanil.submissionstoryapp.data.Status
import com.ardanil.submissionstoryapp.data.response.LoginResponse
import com.ardanil.submissionstoryapp.data.response.LoginResult
import com.ardanil.submissionstoryapp.data.response.RegisterResponse
import com.ardanil.submissionstoryapp.data.response.SubmitResponse

object DataDummy {

	fun generateRegisterSuccess(): Resource<RegisterResponse> {
		return Resource(Status.SUCCESS, RegisterResponse(false, "User registered successfully"))
	}

	fun generateRegisterFailed(): Resource<RegisterResponse> {
		return Resource(Status.ERROR, RegisterResponse(true, "Failed Register"))
	}

	fun generateLoginSuccess(): Resource<LoginResponse> {
		return Resource(Status.SUCCESS, LoginResponse(LoginResult(
			name = "Ardanil",
			userId = "ardanil",
			token = "qwerty"
		), false, "Login Success"))
	}

	fun generateLoginFailed(): Resource<LoginResponse> {
		return Resource(Status.ERROR, LoginResponse(null, true, "Login Failed"))
	}

	fun generateSubmitStorySuccess(): Resource<SubmitResponse> {
		return Resource(Status.SUCCESS, SubmitResponse(false, "Story submitted successfully"))
	}

	fun generateSubmitStoryFailed(): Resource<SubmitResponse> {
		return Resource(Status.ERROR, SubmitResponse(true, "Failed Upload"))
	}

}