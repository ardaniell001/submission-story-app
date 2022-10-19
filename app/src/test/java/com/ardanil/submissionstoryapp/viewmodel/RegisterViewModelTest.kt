package com.ardanil.submissionstoryapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ardanil.submissionstoryapp.data.Status
import com.ardanil.submissionstoryapp.data.StoryRepository
import com.ardanil.submissionstoryapp.utils.DataDummy
import com.ardanil.submissionstoryapp.utils.MainDispatcherRule
import com.ardanil.submissionstoryapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
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

	@Before
	fun setUp() {
		storyRepository = Mockito.mock(StoryRepository::class.java)
		registerViewModel = RegisterViewModel(storyRepository)
	}

	@Test
	fun `when Register Success`() = runTest {
		val expectedResponse = DataDummy.generateRegisterSuccess()
		Mockito.`when`(storyRepository.registerUser("Ardanil", "ardanil@gmail.com", "123456")).thenReturn(expectedResponse)
		registerViewModel.registerUser("Ardanil", "ardanil@gmail.com", "123456")
		val actualResponse = registerViewModel.registerLiveData.getOrAwaitValue()
		verify(storyRepository).registerUser("Ardanil", "ardanil@gmail.com", "123456")
		assertNotNull(actualResponse)
		assertTrue(actualResponse.status == Status.SUCCESS)
		Assert.assertEquals(expectedResponse, actualResponse.item)
	}

	@Test
	fun `when Register Failed`() = runTest {
		val expectedResponse = DataDummy.generateRegisterFailed()
		Mockito.`when`(storyRepository.registerUser("Ardanil", "ardanil@gmail.com", "123456")).thenReturn(expectedResponse)
		registerViewModel.registerUser("Ardanil", "ardanil@gmail.com", "123456")
		val actualResponse = registerViewModel.registerLiveData.getOrAwaitValue()
		verify(storyRepository).registerUser("Ardanil", "ardanil@gmail.com", "123456")
		assertNotNull(actualResponse)
		assertTrue(actualResponse.status == Status.ERROR)
	}

}