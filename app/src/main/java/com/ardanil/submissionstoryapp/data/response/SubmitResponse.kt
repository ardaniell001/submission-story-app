package com.ardanil.submissionstoryapp.data.response

import com.google.gson.annotations.SerializedName

data class SubmitResponse(

	@SerializedName("error")
	val error: Boolean?,

	@SerializedName("message")
	val message: String?
)
