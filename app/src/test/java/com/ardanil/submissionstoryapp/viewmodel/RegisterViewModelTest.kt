package com.ardanil.submissionstoryapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.ardanil.submissionstoryapp.data.Resource
import com.ardanil.submissionstoryapp.data.Status
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
	private lateinit var registerViewModel: RegisterViewModel
	private val dummyRegisterSuccess = DataDummy.generateRegisterSuccess()

	@Before
	fun setUp() {
		registerViewModel = RegisterViewModel()
	}

	@Test
	fun `when Register Success`() {
		val expectedResponse = MutableLiveData<Resource<RegisterResponse>>()
		expectedResponse.value = dummyRegisterSuccess
		val actualResponse = registerViewModel.registerLiveData.getOrAwaitValue()
		verify(registerViewModel).registerUser(
			"Ardanil",
			"ardanil@gmail.com",
			"123456")
		assertNotNull(actualResponse)
		assertTrue(actualResponse.status == Status.SUCCESS)
	}
}