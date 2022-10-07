package com.ardanil.submissionstoryapp.data.response

import com.google.gson.annotations.SerializedName

data class StoryResponse(

	@SerializedName("listStory")
	val listStory: List<ListStoryItem>?,

	@SerializedName("error")
	val error: Boolean?,

	@SerializedName("message")
	val message: String?

)


