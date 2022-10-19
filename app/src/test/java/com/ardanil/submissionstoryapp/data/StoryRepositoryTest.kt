package com.ardanil.submissionstoryapp.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.ardanil.submissionstoryapp.config.ApiService
import com.ardanil.submissionstoryapp.config.StoryPagingSource
import com.ardanil.submissionstoryapp.data.preference.AuthPref
import com.ardanil.submissionstoryapp.data.response.StoryResponse
import com.ardanil.submissionstoryapp.utils.DataDummy
import com.ardanil.submissionstoryapp.utils.MainCoroutineRule
import com.ardanil.submissionstoryapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryRepositoryTest {

	@get:Rule
	var instantExecutorRule = InstantTaskExecutorRule()

	@get:Rule
	var mainCoroutineRule = MainCoroutineRule()

	@Mock
	private lateinit var storyRepository: StoryRepository
	private lateinit var storyPageSource: StoryPagingSource
	private lateinit var apiService: ApiService
	private lateinit var authPref: AuthPref
	private val testContext: Context = mock(Context::class.java)
	private val testCoroutineDispatcher = UnconfinedTestDispatcher()
	private val testCoroutineScope = TestScope(testCoroutineDispatcher + Job())
	private val testDataStore: DataStore<Preferences> =
		PreferenceDataStoreFactory.create(
			scope = testCoroutineScope,
			produceFile =
			{ testContext.preferencesDataStoreFile("test-preferences-file") }
		)
	private lateinit var file: File
	private lateinit var descriptions: RequestBody
	private lateinit var imageMultipart: MultipartBody.Part

	@Before
	fun setUp() {
		authPref = AuthPref.getInstance(testDataStore)
		storyPageSource = StoryPagingSource(AuthPref.getInstance(testDataStore))
		apiService = mock(ApiService::class.java)
		storyRepository = StoryRepository(authPref, apiService)
		file = File("")
		descriptions = "Description Text".toRequestBody("text/plain".toMediaType())
		val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
		imageMultipart = MultipartBody.Part.createFormData(
			"photo",
			file.name,
			requestImageFile
		)
	}

	@Test
	fun `when get Story Data Paging Should Not Null`() = runTest {
		val actualStory = storyRepository.getStories().getOrAwaitValue()
		assertNotNull(actualStory)
	}

	@Test
	fun `when Upload Success`() = runTest {
		val dummyUploadSuccess = DataDummy.generateSubmitStorySuccess()
		val token = "Bearer Token"
		Mockito.`when`(apiService.submitStory(token, imageMultipart, descriptions)).thenReturn(dummyUploadSuccess)
		val actualResponse = storyRepository.submitStory(token, imageMultipart, descriptions)
		Mockito.verify(apiService).submitStory(token, imageMultipart, descriptions)
		assertNotNull(actualResponse)
		assertEquals(actualResponse, dummyUploadSuccess)
	}

	@Test
	fun `when get Story Location Data Should Not Null`() = runTest {
		val dummyStoryLocation = StoryResponse(
			listStory = DataDummy.generateStoryLocationEntity(),
			error = false,
			message = "Success"
		)
		val token = "Bearer Token"
		Mockito.`when`(apiService.getStoriesLocation(token, 1)).thenReturn(dummyStoryLocation)
		val actualResponse =  storyRepository.getStoriesWithLocation(token)
		Mockito.verify(apiService).getStoriesLocation(token, 1)
		assertNotNull(actualResponse)
		assertEquals(actualResponse, dummyStoryLocation)
	}

	@Test
	fun `when Login Success`() = runTest {
		val dummyLoginSuccess = DataDummy.generateLoginSuccess()
		Mockito.`when`(apiService.login("ardanil", "123456")).thenReturn(dummyLoginSuccess)
		val actualResponse = storyRepository.login("ardanil", "123456")
		Mockito.verify(apiService).login("ardanil", "123456")
		assertNotNull(actualResponse)
		assertEquals(actualResponse, dummyLoginSuccess)
	}

	@Test
	fun `when Login Failed`() = runTest {
		val dummyLoginFailed = DataDummy.generateLoginFailed()
		Mockito.`when`(apiService.login("ardanil", "123456")).thenReturn(dummyLoginFailed)
		val actualResponse = storyRepository.login("ardanil", "123456")
		Mockito.verify(apiService).login("ardanil", "123456")
		assertNotNull(actualResponse)
		assertEquals(actualResponse, dummyLoginFailed)
	}

	@Test
	fun `when Register Success`() = runTest {
		val dummyRegisterSuccess = DataDummy.generateRegisterSuccess()
		Mockito.`when`(apiService.registerUser("Ardanil", "ardanil@gmail.com", "123456")).thenReturn(dummyRegisterSuccess)
		val actualResponse = storyRepository.registerUser("Ardanil", "ardanil@gmail.com", "123456")
		Mockito.verify(apiService).registerUser("Ardanil", "ardanil@gmail.com", "123456")
		assertNotNull(actualResponse)
		assertEquals(actualResponse, dummyRegisterSuccess)
	}

	@Test
	fun `when Register Failed`() = runTest {
		val dummyRegisterFailed = DataDummy.generateRegisterFailed()
		Mockito.`when`(apiService.registerUser("Ardanil", "ardanil@gmail.com", "123456")).thenReturn(dummyRegisterFailed)
		val actualResponse = storyRepository.registerUser("Ardanil", "ardanil@gmail.com", "123456")
		Mockito.verify(apiService).registerUser("Ardanil", "ardanil@gmail.com", "123456")
		assertNotNull(actualResponse)
		assertEquals(actualResponse.error, dummyRegisterFailed.error)
	}

	@Test
	fun getAuth() = runTest {
		val actualResponse = storyRepository.getAuth()
		assertNotNull(actualResponse)
	}

	@Test
	fun getAuthModel() = runTest {
		val actualResponse = storyRepository.getAuthModel()
		assertNotNull(actualResponse)
	}
}