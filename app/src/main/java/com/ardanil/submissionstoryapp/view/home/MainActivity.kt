package com.ardanil.submissionstoryapp.view.home

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ardanil.submissionstoryapp.R
import com.ardanil.submissionstoryapp.databinding.ActivityMainBinding
import com.ardanil.submissionstoryapp.view.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {

	override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

	override fun initViews() {
		super.initViews()
		setSupportActionBar(binding.toolbar)
		val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
		val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_home, R.id.navigation_maps, R.id.navigation_settings))
		setupActionBarWithNavController(navController, appBarConfiguration)
		binding.btmNavigation.setupWithNavController(navController)
	}
}