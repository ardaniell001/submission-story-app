package com.ardanil.submissionstoryapp.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ardanil.submissionstoryapp.config.StoryPagingSource
import com.ardanil.submissionstoryapp.data.preference.AuthPref
import com.ardanil.submissionstoryapp.data.response.ListStoryItem
import com.ardanil.submissionstoryapp.data.response.LoginResponse
import com.ardanil.submissionstoryapp.data.response.RegisterResponse
import com.ardanil.submissionstoryapp.data.response.StoryResponse
import com.ardanil.submissionstoryapp.utils.DataDummy
import com.ardanil.submissionstoryapp.utils.MainDispatcherRule
import com.ardanil.submissionstoryapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.*
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryRepositoryTest {

	@get:Rule
	var instantExecutorRule = InstantTaskExecutorRule()

	@get:Rule
	var mainDispatcherRule = MainDispatcherRule()

	@Mock
	private lateinit var storyRepository: StoryRepository
	private lateinit var storyPageSource: StoryPagingSource
	private lateinit var authPref: AuthPref
	private lateinit var testContext: Context


	@Before
	fun setUp() {
		testContext = Mockito.mock(Context::class.java)
		val testCoroutineDispatcher = UnconfinedTestDispatcher()
		val testCoroutineScope = TestScope(testCoroutineDispatcher + Job())
		val testDataStore: DataStore<Preferences> =
			PreferenceDataStoreFactory.create(
				scope = testCoroutineScope,
				produceFile =
				{ testContext.preferencesDataStoreFile("preferences-file-test") }
			)
		authPref = AuthPref.getInstance(testDataStore)
		storyPageSource = StoryPagingSource(AuthPref.getInstance(testDataStore))
	}

	@Test
	fun `when get Story Data Paging Should Not Null`() = mainDispatcherRule.run {
		val pagingSourceFactory = storyPageSource
		val pagingDataFlow: LiveData<PagingData<ListStoryItem>> = Pager(
			config = PagingConfig(
				pageSize = 5
			),
			pagingSourceFactory = { pagingSourceFactory }
		).liveData
		Mockito.`when`(storyRepository.getStories()).thenReturn(pagingDataFlow)
		val actualStory = storyRepository.getStories().getOrAwaitValue()
		assertNotNull(actualStory)
	}

	@Test
	fun `when get Story Location Data Should Not Null`() = mainDispatcherRule.run {
		val dummyStoryLocation = DataDummy.generateStoryLocationEntity()
		val expectedResponse = MutableLiveData<Resource<StoryResponse>>()
		expectedResponse.value = Resource(Status.SUCCESS, StoryResponse(dummyStoryLocation, false, "Success"))
		Mockito.`when`(storyRepository.getStoriesWithLocation()).thenReturn(expectedResponse)
		val actualResponse = storyRepository.getStoriesWithLocation().getOrAwaitValue()
		assertNotNull(actualResponse)
		assertTrue(actualResponse.status == Status.SUCCESS)
	}

	@Test
	fun `when Login Success`() = mainDispatcherRule.run {
		val dummyLoginSuccess = DataDummy.generateLoginSuccess()
		val expectedResponse = MutableLiveData<Resource<LoginResponse>>()
		expectedResponse.value = dummyLoginSuccess
		Mockito.`when`(storyRepository.login("ardanil", "123456")).thenReturn(expectedResponse)
		val actualResponse = storyRepository.login("ardanil", "123456").getOrAwaitValue()
		assertNotNull(actualResponse)
		assertTrue(actualResponse.status == Status.SUCCESS)
	}

	@Test
	fun `when Login Failed`() = mainDispatcherRule.run {
		val dummyLoginFailed = DataDummy.generateLoginFailed()
		val expectedResponse = MutableLiveData<Resource<LoginResponse>>()
		expectedResponse.value = dummyLoginFailed
		Mockito.`when`(storyRepository.login("ardanil", "123456")).thenReturn(expectedResponse)
		val actualResponse = storyRepository.login("ardanil", "123456").getOrAwaitValue()
		assertTrue(actualResponse.status == Status.ERROR)
	}

	@Test
	fun `when Register Success`() = mainDispatcherRule.run {
		val dummyRegisterSuccess = DataDummy.generateRegisterSuccess()
		val expectedResponse = MutableLiveData<Resource<RegisterResponse>>()
		expectedResponse.value = dummyRegisterSuccess
		Mockito.`when`(storyRepository.registerUser("Ardanil", "ardanil@gmail.com", "123456"))
			.thenReturn(expectedResponse)
		val actualResponse =
			storyRepository.registerUser("Ardanil", "ardanil@gmail.com", "123456").getOrAwaitValue()
		assertNotNull(actualResponse)
		assertTrue(actualResponse.status == Status.SUCCESS)
	}

	@Test
	fun `when Register Failed`() = mainDispatcherRule.run {
		val dummyRegisterFailed = DataDummy.generateRegisterFailed()
		val expectedResponse = MutableLiveData<Resource<RegisterResponse>>()
		expectedResponse.value = dummyRegisterFailed
		Mockito.`when`(storyRepository.registerUser("Ardanil", "ardanil@gmail.com", "123456"))
			.thenReturn(expectedResponse)
		val actualResponse =
			storyRepository.registerUser("Ardanil", "ardanil@gmail.com", "123456").getOrAwaitValue()
		assertTrue(actualResponse.status == Status.ERROR)
	}
}