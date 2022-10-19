package com.ardanil.submissionstoryapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ardanil.submissionstoryapp.data.Status
import com.ardanil.submissionstoryapp.data.StoryRepository
import com.ardanil.submissionstoryapp.data.model.AuthModel
import com.ardanil.submissionstoryapp.data.response.StoryResponse
import com.ardanil.submissionstoryapp.utils.DataDummy
import com.ardanil.submissionstoryapp.utils.MainDispatcherRule
import com.ardanil.submissionstoryapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
internal class HomeViewModelTest {

	@get:Rule
	val instantExecutorRule = InstantTaskExecutorRule()

	@get:Rule
	var mainDispatcherRule = MainDispatcherRule()

	@Mock
	private lateinit var storyRepository: StoryRepository
	private lateinit var homeViewModel: HomeViewModel
	private lateinit var file: File
	private lateinit var descriptions: RequestBody
	private lateinit var imageMultipart: MultipartBody.Part
	private lateinit var token: String

	@Before
	fun setUp() {
		file = File("")
		descriptions = "Description Text".toRequestBody("text/plain".toMediaType())
		val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
		imageMultipart = MultipartBody.Part.createFormData(
			"photo",
			file.name,
			requestImageFile
		)
		token = "DummyToken"
		homeViewModel = HomeViewModel(storyRepository)
	}

	@Test
	fun `when Get Token`() = runTest {
		val expectedResponse = AuthModel(
			name = "DummyName",
			isLogin = true,
			token = token
		)
		Mockito.`when`(storyRepository.getAuthModel()).thenReturn(expectedResponse)
		homeViewModel.getToken()
		val tokenFromAuth = homeViewModel.tokenAuth
		Mockito.verify(storyRepository).getAuthModel()
		assertNotNull(tokenFromAuth)
		Assert.assertEquals("Bearer $token", tokenFromAuth)
	}

	@Test
	fun `when Add Story Success`() = runTest {
		val expectedResponse = DataDummy.generateSubmitStorySuccess()
		homeViewModel.tokenAuth = token
		Mockito.`when`(storyRepository.submitStory(token, imageMultipart, descriptions)).thenReturn(expectedResponse)
		homeViewModel.submitStory(imageMultipart, descriptions)
		val actualResponse = homeViewModel.uploadLiveData.getOrAwaitValue()
		Mockito.verify(storyRepository).submitStory(token, imageMultipart, descriptions)
		assertNotNull(actualResponse)
		assertTrue(actualResponse.status == Status.SUCCESS)
		Assert.assertEquals(expectedResponse, actualResponse.item)
	}

	@Test
	fun `when Add Story Failed`() = runTest {
		val expectedResponse = DataDummy.generateSubmitStoryFailed()
		homeViewModel.tokenAuth = token
		Mockito.`when`(storyRepository.submitStory(token, imageMultipart, descriptions)).thenReturn(expectedResponse)
		homeViewModel.submitStory(imageMultipart, descriptions)
		val actualResponse = homeViewModel.uploadLiveData.getOrAwaitValue()
		Mockito.verify(storyRepository).submitStory(token, imageMultipart, descriptions)
		assertNotNull(actualResponse)
		assertTrue(actualResponse.status == Status.ERROR)
	}

	@Test
	fun `when Get Story By Location Should Not Null and Return Success`() = runTest {
		val expectedResponse = StoryResponse(
			listStory = DataDummy.generateStoryLocationEntity(),
			error = false,
			message = "Success"
		)
		homeViewModel.tokenAuth = token
		Mockito.`when`(storyRepository.getStoriesWithLocation(token)).thenReturn(expectedResponse)
		homeViewModel.getStoriesWithLocation()
		val actualResponse = homeViewModel.storiesLocationLiveData.getOrAwaitValue()
		Mockito.verify(storyRepository).getStoriesWithLocation(token)
		assertNotNull(actualResponse)
		assertTrue(actualResponse.status == Status.SUCCESS)
		Assert.assertEquals(expectedResponse, actualResponse.item)
	}

	@Test
	fun `when Get Story By Location Error`() = runTest {
		val expectedResponse = StoryResponse(
			listStory = null,
			error = true,
			message = "Failed"
		)
		homeViewModel.tokenAuth = token
		Mockito.`when`(storyRepository.getStoriesWithLocation(token)).thenReturn(expectedResponse)
		homeViewModel.getStoriesWithLocation()
		val actualResponse = homeViewModel.storiesLocationLiveData.getOrAwaitValue()
		Mockito.verify(storyRepository).getStoriesWithLocation(token)
		assertNotNull(actualResponse)
		assertTrue(actualResponse.status == Status.ERROR)
	}

}