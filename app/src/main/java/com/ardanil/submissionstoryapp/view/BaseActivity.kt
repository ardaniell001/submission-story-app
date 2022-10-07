package com.ardanil.submissionstoryapp.view

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.viewbinding.ViewBinding
import com.ardanil.submissionstoryapp.config.MyApp
import com.ardanil.submissionstoryapp.view.custom.LoadingDialog
import java.util.*

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_settings")

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

	protected lateinit var binding: VB
	protected val loadingDialog by lazy { LoadingDialog(this) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = getViewBinding()
		setContentView(binding.root)
		setSupportActionBar(getActionToolbar())
		supportActionBar?.let(this::configureActionBar)
		initViews()
	}

	abstract fun getViewBinding(): VB

	@CallSuper
	protected open fun initViews() = Unit

	protected open fun getActionToolbar(): Toolbar? = null

	protected open fun configureActionBar(actionBar: ActionBar) = Unit

	override fun attachBaseContext(newBase: Context?) {
		super.attachBaseContext(customConfig(newBase))
	}

	private fun customConfig(context: Context?): Context? {
		val locale = Locale(MyApp.instance.langApp)
		Locale.setDefault(locale)
		val config = Configuration()
		config.setLocale(locale)
		return context?.createConfigurationContext(config)
	}


}