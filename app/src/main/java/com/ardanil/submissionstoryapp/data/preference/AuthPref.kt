package com.ardanil.submissionstoryapp.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ardanil.submissionstoryapp.data.model.AuthModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthPref private constructor(private val dataStore: DataStore<Preferences>) {

	companion object {
		private val KEY_NAME = stringPreferencesKey("name")
		private val KEY_TOKEN = stringPreferencesKey("token")
		private val KEY_IS_LOGIN = booleanPreferencesKey("is_login")
		private val KEY_THEME = stringPreferencesKey("theme")
		private val KEY_LANGUAGE = stringPreferencesKey("language")

		@Volatile
		private var instance: AuthPref? = null

		fun getInstance(dataStore: DataStore<Preferences>): AuthPref {
			return instance ?: synchronized(this) {
				instance ?: AuthPref(dataStore).also { instance = it }
			}
		}
	}

	suspend fun saveAuth(authModel: AuthModel) {
		dataStore.edit { preferences ->
			preferences[KEY_NAME] = authModel.name
			preferences[KEY_TOKEN] = authModel.token
			preferences[KEY_IS_LOGIN] = authModel.isLogin
		}
	}

	fun getAuth(): Flow<AuthModel> {
		return dataStore.data.map { preferences ->
			val name = preferences[KEY_NAME] ?: ""
			val token = preferences[KEY_TOKEN] ?: ""
			val isLogin = preferences[KEY_IS_LOGIN] ?: false
			AuthModel(name, token, isLogin)
		}
	}

	suspend fun logout() {
		dataStore.edit { preferences ->
			preferences.clear()
		}
	}

	fun getTheme(): Flow<String> {
		return dataStore.data.map { preferences ->
			preferences[KEY_THEME] ?: "default"
		}
	}

	suspend fun setTheme(theme: String) {
		dataStore.edit { preferences ->
			preferences[KEY_THEME] = theme
		}
	}

	fun getLanguage(): Flow<String> {
		return dataStore.data.map { preferences ->
			preferences[KEY_LANGUAGE] ?: "en"
		}
	}

	suspend fun setLanguage(lang: String) {
		dataStore.edit { preferences ->
			preferences[KEY_LANGUAGE] = lang
		}
	}

}