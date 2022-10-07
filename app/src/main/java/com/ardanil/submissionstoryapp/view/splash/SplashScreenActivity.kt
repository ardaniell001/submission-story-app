package com.ardanil.submissionstoryapp.view.splash

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Pair
import android.view.View
import android.view.animation.BounceInterpolator
import androidx.lifecycle.ViewModelProvider
import com.ardanil.submissionstoryapp.data.preference.AuthPref
import com.ardanil.submissionstoryapp.databinding.ActivitySplashScreenBinding
import com.ardanil.submissionstoryapp.view.BaseActivity
import com.ardanil.submissionstoryapp.view.auth.LoginActivity
import com.ardanil.submissionstoryapp.view.dataStore
import com.ardanil.submissionstoryapp.view.home.MainActivity
import com.ardanil.submissionstoryapp.viewmodel.AuthViewModel
import com.ardanil.submissionstoryapp.viewmodel.AuthViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : BaseActivity<ActivitySplashScreenBinding>() {

	private lateinit var authViewModel: AuthViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		authViewModel = ViewModelProvider(this, AuthViewModelFactory(AuthPref.getInstance(this.dataStore)))[AuthViewModel::class.java]
		super.onCreate(savedInstanceState)
	}

	override fun getViewBinding(): ActivitySplashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)

	override fun initViews() {
		super.initViews()
		Handler(Looper.getMainLooper()).postDelayed({
			initLiveData()
		}, 3000)
		setAnimation()
	}

	private fun setAnimation() {
		val iconImg = ObjectAnimator.ofFloat(binding.ivImg, View.TRANSLATION_Y, -150f, 0f).setDuration(2500)
		val textTitle = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(2000)
		iconImg.interpolator = BounceInterpolator()
		iconImg.start()
		AnimatorSet().apply {
			playTogether(iconImg, textTitle)
			start()
		}
		iconImg.addListener(object : Animator.AnimatorListener{
			override fun onAnimationStart(animator: Animator) {}

			override fun onAnimationEnd(animator: Animator) {
				//Do Nothing
			}

			override fun onAnimationCancel(animator: Animator) {}

			override fun onAnimationRepeat(animator: Animator) {}

		})
	}

	private fun initLiveData() {
		authViewModel.getAuth().observe(this) {
			if (it.isLogin) {
				startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
			} else {
				val pairs = arrayOf(
					Pair<View, String>(binding.ivImg, "logo_img"),
					Pair<View, String>(binding.tvTitle, "logo_title")
				)
				startActivity(
					Intent(this@SplashScreenActivity, LoginActivity::class.java),
					ActivityOptions.makeSceneTransitionAnimation(this@SplashScreenActivity, *pairs).toBundle()
				)
			}
		}
	}

	override fun onStop() {
		super.onStop()
		finish()
	}

}