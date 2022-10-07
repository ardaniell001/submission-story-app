package com.ardanil.submissionstoryapp.view.auth

import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import com.ardanil.submissionstoryapp.R
import com.ardanil.submissionstoryapp.data.Status
import com.ardanil.submissionstoryapp.databinding.ActivityRegisterBinding
import com.ardanil.submissionstoryapp.view.BaseActivity
import com.ardanil.submissionstoryapp.viewmodel.RegisterViewModel

class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

	private val registerViewModel by viewModels<RegisterViewModel>()

	override fun getViewBinding(): ActivityRegisterBinding =
		ActivityRegisterBinding.inflate(layoutInflater)

	override fun initViews() {
		super.initViews()
		setupValidation()
		initLiveData()
	}

	private fun setupValidation() {
		binding.etName.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				binding.etName.error = if (p0.toString().isNotEmpty()) {
					null
				} else getString(R.string.validate_nama_empty)
				binding.btnSubmit.isEnabled = validateRegister()
			}

			override fun afterTextChanged(p0: Editable?) {}

		})
		binding.etConfPassword.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				binding.etConfPassword.error = if (p0.toString() == binding.etPassword.text.toString()) {
					null
				} else getString(R.string.validate_password_confirm)
				binding.btnSubmit.isEnabled = validateRegister()
			}

			override fun afterTextChanged(p0: Editable?) {}

		})
		binding.etPassword.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				binding.btnSubmit.isEnabled = validateRegister()
			}

			override fun afterTextChanged(p0: Editable?) {}

		})
		binding.etEmail.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				binding.btnSubmit.isEnabled = validateRegister()
			}

			override fun afterTextChanged(p0: Editable?) {}

		})
		binding.btnSubmit.setOnClickListener {
			registerViewModel.registerUser(
				binding.etName.text.toString(),
				binding.etEmail.text.toString(),
				binding.etPassword.text.toString()
			)
		}
	}

	private fun validateRegister(): Boolean {
		var isValid = false
		if (
			binding.etName.text.toString().isNotEmpty() &&
			!binding.etEmail.isHasError() &&
			!binding.etPassword.isHasError() &&
			!binding.etConfPassword.isHasError() &&
			binding.etPassword.text.toString() == binding.etConfPassword.text.toString()
		) {
			isValid = true
		}
		return isValid
	}

	private fun initLiveData() {
		registerViewModel.registerLiveData.observe(this) {
			when (it.status) {
				Status.LOADING -> loadingDialog.show()
				Status.SUCCESS -> {
					loadingDialog.dismiss()
					Toast.makeText(applicationContext, it.item?.message, Toast.LENGTH_SHORT)
						.show()
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