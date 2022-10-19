package com.ardanil.submissionstoryapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.ardanil.submissionstoryapp.data.Status
import com.ardanil.submissionstoryapp.data.StoryRepository
import com.ardanil.submissionstoryapp.data.model.AuthModel
import com.ardanil.submissionstoryapp.utils.DataDummy
import com.ardanil.submissionstoryapp.utils.MainCoroutineRule
import com.ardanil.submissionstoryapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
internal class AuthViewModelTest {

	@get:Rule
	val instantExecutorRule = InstantTaskExecutorRule()

	@get:Rule
	var mainCoroutineRule = MainCoroutineRule()

	@Mock
	private lateinit var storyRepository: StoryRepository
	private lateinit var authViewModel: AuthViewModel

	@Before
	fun setUp() {
		storyRepository = mock(StoryRepository::class.java)
		authViewModel = AuthViewModel(storyRepository)
	}

	@Test
	fun `when Login Success`() = runTest {
		val expectedResponse = DataDummy.generateLoginSuccess()
		Mockito.`when`(storyRepository.login("ardanil", "123456")).thenReturn(expectedResponse)
		authViewModel.login("ardanil", "123456")
		val actualResponse = authViewModel.loginLiveData.getOrAwaitValue()
		Mockito.verify(storyRepository).login("ardanil", "123456")
		assertNotNull(actualResponse)
		assertTrue(actualResponse.status == Status.SUCCESS)
		assertEquals(expectedResponse, actualResponse.item)
	}

	@Test
	fun `when Login Failed`() = runTest {
		val expectedResponse = DataDummy.generateLoginFailed()
		Mockito.`when`(storyRepository.login("ardanil", "123456")).thenReturn(expectedResponse)
		authViewModel.login("ardanil", "123456")
		val actualResponse = authViewModel.loginLiveData.getOrAwaitValue()
		Mockito.verify(storyRepository).login("ardanil", "123456")
		assertNotNull(actualResponse)
		assertTrue(actualResponse.status == Status.ERROR)
	}

	@Test
	fun `when Saved And Get Auth Is Same`() {
		val authModel = AuthModel(
			name = "Ardanil",
			token = "qwerty",
			isLogin = true
		)
		val expectedResponse = MutableLiveData<AuthModel>()
		expectedResponse.value = authModel
		Mockito.`when`(storyRepository.getAuth()).thenReturn(expectedResponse)
		val actualResponse = authViewModel.getAuth().getOrAwaitValue()
		Mockito.verify(storyRepository).getAuth()
		assertNotNull(actualResponse)
		assertEquals(actualResponse, authModel)
	}

}