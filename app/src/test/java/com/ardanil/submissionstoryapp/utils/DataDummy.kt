package com.ardanil.submissionstoryapp.utils

import com.ardanil.submissionstoryapp.data.Resource
import com.ardanil.submissionstoryapp.data.Status
import com.ardanil.submissionstoryapp.data.response.*

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

	fun generateStoryLocationEntity(): List<ListStoryItem> {
		val storyList = ArrayList<ListStoryItem>()
		for (i in 0..10) {
			val story = ListStoryItem(
				"https://www.google.com/",
				"2022-10-12T07:30:20Z",
				"Content Title",
				"Content Description",
				100.00,
				"$i",
				1.00
			)
			storyList.add(story)
		}
		return storyList
	}

}