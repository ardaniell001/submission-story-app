package com.ardanil.submissionstoryapp.view.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.ardanil.submissionstoryapp.R
import com.ardanil.submissionstoryapp.config.MyApp
import com.ardanil.submissionstoryapp.data.preference.AuthPref
import com.ardanil.submissionstoryapp.view.auth.LoginActivity
import com.ardanil.submissionstoryapp.view.dataStore
import kotlinx.coroutines.launch

class SettingsFragment : PreferenceFragmentCompat() {

	private lateinit var authPref: AuthPref

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.setting_preferences, rootKey)
		authPref = AuthPref.getInstance(requireContext().dataStore)
		val languagePref = findPreference<ListPreference>("language_app")
		val themeModePref = findPreference<ListPreference>("theme_mode")
		val logoutPref = findPreference<Preference>("logout")

		languagePref?.setOnPreferenceChangeListener { _, newValue ->
			if (authPref.getLanguage() != newValue) {
				lifecycleScope.launch {
					setupLanguage(newValue as String)
				}
			}
			true
		}

		themeModePref?.setOnPreferenceChangeListener { _, newValue ->
			if (authPref.getTheme() != newValue) {
				lifecycleScope.launch {
					setupTheme(newValue as String)
				}
			}
			true
		}

		logoutPref?.setOnPreferenceClickListener { _ ->
			AlertDialog.Builder(requireContext())
				.setTitle(getString(R.string.logout_title))
				.setMessage(getString(R.string.logout_description))
				.setPositiveButton(R.string.general_yes) { _, _ ->
					lifecycleScope.launch {
						authPref.logout()
					}
					startActivity(Intent(requireContext(), LoginActivity::class.java))
					requireActivity().finish()
				}
				.setNegativeButton(R.string.general_no) { _, _ -> }
				.show()
			true
		}
	}

	private suspend fun setupLanguage(newValue: String) {
		MyApp.instance.langApp = newValue
		authPref.setLanguage(newValue)
		activity?.recreate()
	}

	private suspend fun setupTheme(newValue: String) {
		authPref.setTheme(newValue)
		MyApp.instance.applyTheme(newValue)
	}

}