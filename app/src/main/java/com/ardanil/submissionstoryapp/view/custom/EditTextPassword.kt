package com.ardanil.submissionstoryapp.view.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.ardanil.submissionstoryapp.R

class EditTextPassword : AppCompatEditText {

	private var isHasError = true

	constructor(context: Context) : super(context) {
		init()
	}

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
		init()
	}

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
		init()
	}

	fun isHasError(): Boolean {
		return isHasError
	}

	private fun init() {
		addTextChangedListener(onTextChanged = { s, _, _, _ ->
			when {
				s.toString().isEmpty() -> {
					isHasError = true
					error = context.getString(R.string.validate_password_empty)
				}
				s.toString().trim().length < 6 -> {
					isHasError = true
					error = context.getString(R.string.validate_password_length)
				}
				else -> {
					isHasError = false
					error = null
				}
			}
		})
	}

}