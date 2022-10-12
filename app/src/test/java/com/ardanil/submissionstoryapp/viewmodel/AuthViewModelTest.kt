package com.ardanil.submissionstoryapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.ardanil.submissionstoryapp.data.Resource
import com.ardanil.submissionstoryapp.data.Status
import com.ardanil.submissionstoryapp.data.StoryRepository
import com.ardanil.submissionstoryapp.data.model.AuthModel
import com.ardanil.submissionstoryapp.data.response.LoginResponse
import com.ardanil.submissionstoryapp.utils.DataDummy
import com.ardanil.submissionstoryapp.utils.MainDispatcherRule
import com.ardanil.submissionstoryapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
internal class AuthViewModelTest {

	@get:Rule
	val instantExecutorRule = InstantTaskExecutorRule()

	@get:Rule
	var mainDispatcherRule = MainDispatcherRule()

	@Mock
	private lateinit var storyRepository: StoryRepository
	private lateinit var authViewModel: AuthViewModel
	private val dummyLoginSuccess = DataDummy.generateLoginSuccess()
	private val dummyLoginFailed = DataDummy.generateLoginFailed()

	@Before
	fun setUp() {
		authViewModel = AuthViewModel(storyRepository)
	}

	@Test
	fun `when Login Success`() {
		val expectedResponse = MutableLiveData<Resource<LoginResponse>>()
		expectedResponse.value = dummyLoginSuccess
		Mockito.`when`(authViewModel.login("ardanil", "123456")).thenReturn(expectedResponse)
		val actualResponse = authViewModel.login("ardanil", "123456").getOrAwaitValue()
		Mockito.verify(storyRepository).login("ardanil", "123456")
		assertNotNull(actualResponse)
		assertTrue(actualResponse.status == Status.SUCCESS)
	}

	@Test
	fun `when Login Failed`() {
		val expectedResponse = MutableLiveData<Resource<LoginResponse>>()
		expectedResponse.value = dummyLoginFailed
		Mockito.`when`(authViewModel.login("ardanil", "123456")).thenReturn(expectedResponse)
		val actualResponse = authViewModel.login("ardanil", "123456").getOrAwaitValue()
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
		Mockito.`when`(authViewModel.getAuth()).thenReturn(expectedResponse)
		val actualResponse = authViewModel.getAuth().getOrAwaitValue()
		Mockito.verify(storyRepository).getAuth()
		assertNotNull(actualResponse)
		Assert.assertEquals(actualResponse, authModel)
	}

}