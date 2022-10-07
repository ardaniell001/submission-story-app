package com.ardanil.submissionstoryapp.view.custom

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.ardanil.submissionstoryapp.R

class LoadingDialog : Dialog {

	constructor(context: Context) : super(context) {
		init()
	}

	constructor(context: Context, themeResId: Int) : super(context, themeResId) {
		init()
	}

	constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener) {
		init()
	}

	@SuppressLint("InflateParams")
	private fun init() {
		setCancelable(false)
		setCanceledOnTouchOutside(false)
		val view = LayoutInflater.from(context).inflate(R.layout.loading_dialog, null)
		window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
		setContentView(view)
	}
}