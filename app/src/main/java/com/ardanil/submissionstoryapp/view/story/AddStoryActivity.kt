package com.ardanil.submissionstoryapp.view.story

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.ardanil.submissionstoryapp.R
import com.ardanil.submissionstoryapp.config.ImageUtils
import com.ardanil.submissionstoryapp.data.Status
import com.ardanil.submissionstoryapp.data.preference.AuthPref
import com.ardanil.submissionstoryapp.databinding.ActivityAddStoryBinding
import com.ardanil.submissionstoryapp.view.BaseActivity
import com.ardanil.submissionstoryapp.view.dataStore
import com.ardanil.submissionstoryapp.viewmodel.HomeViewModel
import com.ardanil.submissionstoryapp.viewmodel.HomeViewModelFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException

class AddStoryActivity : BaseActivity<ActivityAddStoryBinding>() {

	private var mFile: File? = null

	private val viewModel by viewModels<HomeViewModel> {
		HomeViewModelFactory.getInstance(AuthPref.getInstance(dataStore))
	}

	override fun getViewBinding(): ActivityAddStoryBinding = ActivityAddStoryBinding.inflate(layoutInflater)

	override fun getActionToolbar(): Toolbar = binding.toolbar

	override fun configureActionBar(actionBar: ActionBar) {
		with(actionBar) {
			title = getString(R.string.detail_story_title)
			setHomeButtonEnabled(true)
			setDisplayHomeAsUpEnabled(true)
			setDisplayShowTitleEnabled(true)
		}
	}

	override fun initViews() {
		super.initViews()
		binding.btnCamera.setOnClickListener {
			if (!checkPermissions()) {
				ActivityCompat.requestPermissions(
					this,
					REQUIRED_PERMISSIONS,
					REQUEST_PERMISSION_CODE
				)
				return@setOnClickListener
			}
			startCamera()
		}

		binding.btnGallery.setOnClickListener {
			val intent = Intent()
			intent.action = Intent.ACTION_GET_CONTENT
			intent.type = "image/*"
			val chooser = Intent.createChooser(intent, getString(R.string.add_image_gallery))
			launchIntentGallery.launch(chooser)
		}

		binding.btnSubmit.setOnClickListener {
			submitPost()
		}

		viewModel.getToken()
		initLiveData()

	}

	private fun initLiveData() {
		viewModel.uploadLiveData.observe(this) { res ->
			when (res.status) {
				Status.LOADING -> loadingDialog.show()
				Status.SUCCESS -> {
					loadingDialog.dismiss()
					Toast.makeText(applicationContext, getString(R.string.add_image_success), Toast.LENGTH_SHORT)
						.show()
					setResult(RESULT_OK, Intent())
					finish()
				}
				Status.ERROR -> {
					loadingDialog.dismiss()
					Toast.makeText(applicationContext, res.throwable?.message, Toast.LENGTH_SHORT)
						.show()
				}
			}
		}
	}

	@SuppressLint("QueryPermissionsNeeded")
	private fun startCamera() {
		val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
		if (cameraIntent.resolveActivity(packageManager) != null) {
			try {
				mFile = ImageUtils.createImageFile(this)
			} catch (ex: IOException) {
				ex.printStackTrace()
			}
			mFile?.let{
				val photoURI = FileProvider.getUriForFile(
					this,
					"com.ardanil.submissionstoryapp",
					it
				)
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
				launchIntentCamera.launch(cameraIntent)
			}
		} else {
			Toast.makeText(this, "Failed open camera", Toast.LENGTH_LONG).show()
		}
	}

	private val launchIntentCamera = registerForActivityResult(
		ActivityResultContracts.StartActivityForResult()
	) { result ->
		if (result.resultCode == RESULT_OK) {
			mFile?.let {
				binding.ivImg.setImageURI(Uri.fromFile(it))
			}
		}
	}

	private val launchIntentGallery = registerForActivityResult(
		ActivityResultContracts.StartActivityForResult()
	) { result ->
		if (result.resultCode == RESULT_OK) {
			val selectedImg: Uri = result.data?.data as Uri
			val myFile = ImageUtils.uriToFile(selectedImg, this)

			mFile = myFile
			binding.ivImg.setImageURI(selectedImg)
		}
	}

	private fun checkPermissions() = REQUIRED_PERMISSIONS.all { permission ->
		ContextCompat.checkSelfPermission(baseContext, permission) == PackageManager.PERMISSION_GRANTED
	}

	private fun submitPost() {
		val desc = binding.etDescription.text.toString()
		if (desc.isEmpty() || mFile == null) {
			Toast.makeText(this, getString(R.string.add_image_validate_error), Toast.LENGTH_LONG).show()
			return
		}
		mFile?.let {
			val compressImage = ImageUtils.compressImage(it)
			val description = desc.toRequestBody("text/plain".toMediaTypeOrNull())
			val currentImageFile = compressImage.asRequestBody("image/jpeg".toMediaTypeOrNull())
			val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
				"photo",
				compressImage.name,
				currentImageFile
			)
			viewModel.submitStory(imageMultipart, description)
		}
	}

	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<out String>,
		grantResults: IntArray
	) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		if (requestCode == REQUEST_PERMISSION_CODE) {
			if (!checkPermissions()) {
				Toast.makeText(this, getString(R.string.add_permission_validate_error), Toast.LENGTH_SHORT).show()
				finish()
			} else {
				startCamera()
			}
		}
	}

	companion object {
		private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
		private const val REQUEST_PERMISSION_CODE = 999
	}

}