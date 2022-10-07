package com.ardanil.submissionstoryapp.config

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.ardanil.submissionstoryapp.data.preference.AuthPref
import com.ardanil.submissionstoryapp.view.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MyApp : Application() {

	companion object {
		lateinit var instance: MyApp
			private set
	}

	var langApp: String = "en"

	override fun onCreate() {
		super.onCreate()
		instance = this
		initMyApp()
	}

	private fun initMyApp() {
		try {
			val authPref = AuthPref.getInstance(this.dataStore)
			runBlocking {
				authPref.getTheme().first().let {
					applyTheme(it)
				}
				authPref.getLanguage().first().let {
					langApp = it
				}
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	fun applyTheme(theme: String) {
		when (theme) {
			"dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
			"light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
			else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
		}
	}

}