package com.ardanil.submissionstoryapp.utils

import com.ardanil.submissionstoryapp.data.response.*

object DataDummy {

	fun generateRegisterSuccess(): RegisterResponse {
		return RegisterResponse(false, "User registered successfully")
	}

	fun generateRegisterFailed(): RegisterResponse {
		return RegisterResponse(true, "Failed Register")
	}

	fun generateLoginSuccess(): LoginResponse {
		return LoginResponse(LoginResult(
			name = "Ardanil",
			userId = "ardanil",
			token = "qwerty"
		), false, "Login Success")
	}

	fun generateLoginFailed(): LoginResponse {
		return LoginResponse(null, true, "Login Failed")
	}

	fun generateSubmitStorySuccess(): SubmitResponse {
		return SubmitResponse(false, "Story submitted successfully")
	}

	fun generateSubmitStoryFailed(): SubmitResponse {
		return SubmitResponse(true, "Failed Upload")
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