package com.ardanil.submissionstoryapp.view.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ardanil.submissionstoryapp.R
import com.ardanil.submissionstoryapp.data.Status
import com.ardanil.submissionstoryapp.data.preference.AuthPref
import com.ardanil.submissionstoryapp.databinding.ActivityLoginBinding
import com.ardanil.submissionstoryapp.view.BaseActivity
import com.ardanil.submissionstoryapp.view.dataStore
import com.ardanil.submissionstoryapp.view.home.MainActivity
import com.ardanil.submissionstoryapp.viewmodel.AuthViewModel
import com.ardanil.submissionstoryapp.viewmodel.AuthViewModelFactory

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

	private lateinit var authViewModel: AuthViewModel

	override fun getViewBinding(): ActivityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		authViewModel = ViewModelProvider(this, AuthViewModelFactory(AuthPref.getInstance(dataStore)))[AuthViewModel::class.java]
		initLiveData()
	}

	override fun initViews() {
		super.initViews()
		binding.tvRegister.setOnClickListener {
			startActivity(Intent(this, RegisterActivity::class.java))
		}

		binding.btnSubmit.setOnClickListener {
			authViewModel.login(binding.etEmail.text.toString(), binding.etPassword.text.toString())
		}

		binding.etPassword.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				binding.btnSubmit.isEnabled = validateLogin()
			}

			override fun afterTextChanged(p0: Editable?) {}

		})
		binding.etEmail.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				binding.btnSubmit.isEnabled = validateLogin()
			}

			override fun afterTextChanged(p0: Editable?) {}

		})
	}

	private fun validateLogin(): Boolean {
		return !binding.etEmail.isHasError() && !binding.etPassword.isHasError()
	}

	private fun initLiveData() {
		authViewModel.loginLiveData.observe(this) {
			when (it.status) {
				Status.LOADING -> loadingDialog.show()
				Status.SUCCESS -> {
					loadingDialog.dismiss()
					Toast.makeText(applicationContext, getString(R.string.login_success), Toast.LENGTH_SHORT)
						.show()
					startActivity(Intent(this, MainActivity::class.java))
					finish()
				}
				Status.ERROR -> {
					loadingDialog.dismiss()
					Toast.makeText(applicationContext, it.throwable?.message, Toast.LENGTH_SHORT)
						.show()
				}
			}
		}
	}

}