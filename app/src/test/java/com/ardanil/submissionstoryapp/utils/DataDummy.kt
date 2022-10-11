package com.ardanil.submissionstoryapp.utils

import com.ardanil.submissionstoryapp.data.Resource
import com.ardanil.submissionstoryapp.data.Status
import com.ardanil.submissionstoryapp.data.response.RegisterResponse

object DataDummy {

	fun generateRegisterSuccess(): Resource<RegisterResponse> {
		return Resource(Status.SUCCESS, RegisterResponse(false, "User registered successfully"))
	}
}