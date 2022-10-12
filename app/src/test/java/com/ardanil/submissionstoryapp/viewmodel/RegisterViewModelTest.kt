package com.ardanil.submissionstoryapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.ardanil.submissionstoryapp.data.Resource
import com.ardanil.submissionstoryapp.data.Status
import com.ardanil.submissionstoryapp.data.StoryRepository
import com.ardanil.submissionstoryapp.data.response.RegisterResponse
import com.ardanil.submissionstoryapp.utils.DataDummy
import com.ardanil.submissionstoryapp.utils.MainDispatcherRule
import com.ardanil.submissionstoryapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
internal class RegisterViewModelTest {

	@get:Rule
	val instantExecutorRule = InstantTaskExecutorRule()

	@get:Rule
	var mainDispatcherRule = MainDispatcherRule()

	@Mock
	private lateinit var storyRepository: StoryRepository
	private lateinit var registerViewModel: RegisterViewModel
	private val dummyRegisterSuccess = DataDummy.generateRegisterSuccess()
	private val dummyRegisterFailed = DataDummy.generateRegisterFailed()

	@Before
	fun setUp() {
		registerViewModel = RegisterViewModel(storyRepository)
	}

	@Test
	fun `when Register Success`() {
		val expectedResponse = MutableLiveData<Resource<RegisterResponse>>()
		expectedResponse.value = dummyRegisterSuccess
		Mockito.`when`(registerViewModel.registerUser("Ardanil", "ardanil@gmail.com", "123456")).thenReturn(expectedResponse)
		val actualResponse = registerViewModel.registerUser("Ardanil", "ardanil@gmail.com", "123456").getOrAwaitValue()
		verify(storyRepository).registerUser("Ardanil", "ardanil@gmail.com", "123456")
		assertNotNull(actualResponse)
		assertTrue(actualResponse.status == Status.SUCCESS)
	}

	@Test
	fun `when Register Failed`() {
		val expectedRegisterResponse = MutableLiveData<Resource<RegisterResponse>>()
		expectedRegisterResponse.value = dummyRegisterFailed
		Mockito.`when`(registerViewModel.registerUser("Ardanil", "ardanil@gmail.com", "123456")).thenReturn(expectedRegisterResponse)
		val actualResponse = registerViewModel.registerUser("Ardanil", "ardanil@gmail.com", "123456").getOrAwaitValue()
		verify(storyRepository).registerUser("Ardanil", "ardanil@gmail.com", "123456")
		assertNotNull(actualResponse)
		assertTrue(actualResponse.status == Status.ERROR)
	}

}