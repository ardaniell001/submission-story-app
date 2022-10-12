package com.ardanil.submissionstoryapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.ardanil.submissionstoryapp.data.Resource
import com.ardanil.submissionstoryapp.data.Status
import com.ardanil.submissionstoryapp.data.StoryRepository
import com.ardanil.submissionstoryapp.data.response.StoryResponse
import com.ardanil.submissionstoryapp.data.response.SubmitResponse
import com.ardanil.submissionstoryapp.utils.DataDummy
import com.ardanil.submissionstoryapp.utils.MainDispatcherRule
import com.ardanil.submissionstoryapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
	private val dummySuccess = DataDummy.generateSubmitStorySuccess()
	private val dummyFailed = DataDummy.generateSubmitStoryFailed()
	private val dummyStoryLocation = DataDummy.generateStoryLocationEntity()

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
		homeViewModel = HomeViewModel(storyRepository)
	}

	@Test
	fun `when Add Story Success`() {
		val expectedResponse = MutableLiveData<Resource<SubmitResponse>>()
		expectedResponse.value = dummySuccess
		Mockito.`when`(homeViewModel.submitStory(imageMultipart, descriptions)).thenReturn(expectedResponse)
		val actualResponse = homeViewModel.submitStory(imageMultipart, descriptions).getOrAwaitValue()
		Mockito.verify(storyRepository).submitStory(imageMultipart, descriptions)
		assertNotNull(actualResponse)
		assertTrue(actualResponse.status == Status.SUCCESS)
	}

	@Test
	fun `when Add Story Failed`() {
		val expectedResponse = MutableLiveData<Resource<SubmitResponse>>()
		expectedResponse.value = dummyFailed
		Mockito.`when`(homeViewModel.submitStory(imageMultipart, descriptions)).thenReturn(expectedResponse)
		val actualResponse = homeViewModel.submitStory(imageMultipart, descriptions).getOrAwaitValue()
		Mockito.verify(storyRepository).submitStory(imageMultipart, descriptions)
		assertNotNull(actualResponse)
		assertTrue(actualResponse.status == Status.ERROR)
	}

	@Test
	fun `when Get Story By Location Should Not Null and Return Success`() {
		val expectedResponse = MutableLiveData<Resource<StoryResponse>>()
		expectedResponse.value = Resource(Status.SUCCESS, StoryResponse(dummyStoryLocation, false, "Success"))
		Mockito.`when`(homeViewModel.getStoriesWithLocation()).thenReturn(expectedResponse)
		val actualResponse = homeViewModel.getStoriesWithLocation().getOrAwaitValue()
		Mockito.verify(storyRepository).getStoriesWithLocation()
		assertNotNull(actualResponse)
		assertTrue(actualResponse.status == Status.SUCCESS)
		Assert.assertEquals(dummyStoryLocation.size, actualResponse.item?.listStory?.size)
	}

	@Test
	fun `when Get Story By Location Error`() {
		val expectedResponse = MutableLiveData<Resource<StoryResponse>>()
		expectedResponse.value = Resource(Status.ERROR, StoryResponse(null, true, "Failed"))
		Mockito.`when`(homeViewModel.getStoriesWithLocation()).thenReturn(expectedResponse)
		val actualResponse = homeViewModel.getStoriesWithLocation().getOrAwaitValue()
		Mockito.verify(storyRepository).getStoriesWithLocation()
		assertNotNull(actualResponse)
		assertTrue(actualResponse.status == Status.ERROR)
	}

}