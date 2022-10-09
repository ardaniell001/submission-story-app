package com.ardanil.submissionstoryapp.view.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.ardanil.submissionstoryapp.R

class EditTextEmail : AppCompatEditText {

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
		addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
				//Do Nothing
			}

			override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
				when {
					s.toString().isEmpty() -> {
						isHasError = true
						error = context.getString(R.string.validate_email_empty)
					}
					!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches() -> {
						isHasError = true
						error = context.getString(R.string.validate_email_invalid)
					}
					else -> {
						isHasError = false
						error = null
					}
				}
			}

			override fun afterTextChanged(s: Editable?) {
				//Do Nothing
			}

		})
	}

}