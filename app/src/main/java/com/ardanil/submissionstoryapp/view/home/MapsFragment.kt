package com.ardanil.submissionstoryapp.view.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ardanil.submissionstoryapp.R
import com.ardanil.submissionstoryapp.data.Status
import com.ardanil.submissionstoryapp.data.preference.AuthPref
import com.ardanil.submissionstoryapp.databinding.FragmentMapsBinding
import com.ardanil.submissionstoryapp.view.custom.LoadingDialog
import com.ardanil.submissionstoryapp.view.dataStore
import com.ardanil.submissionstoryapp.viewmodel.HomeViewModel
import com.ardanil.submissionstoryapp.viewmodel.HomeViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class MapsFragment : Fragment() {

	private var mMap: GoogleMap? = null
	private lateinit var binding: FragmentMapsBinding
	private val loadingDialog by lazy { LoadingDialog(requireContext()) }
	private val viewModel by viewModels<HomeViewModel> {
		HomeViewModelFactory.getInstance(AuthPref.getInstance(requireContext().dataStore))
	}

	private val callback = OnMapReadyCallback { googleMap ->
		mMap = googleMap

		mMap?.uiSettings?.isZoomControlsEnabled = true
		mMap?.uiSettings?.isIndoorLevelPickerEnabled = true
		mMap?.uiSettings?.isCompassEnabled = true
		mMap?.uiSettings?.isMapToolbarEnabled = true
		setMapStyle()
		setMyLocation()
		viewModel.getToken()
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentMapsBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
		mapFragment?.getMapAsync(callback)
		initLiveData()
	}

	private fun initLiveData() {
		viewModel.tokenLiveData.observe(viewLifecycleOwner) {
			viewModel.getStoriesWithLocation()
		}
		viewModel.storiesLocationLiveData.observe(viewLifecycleOwner) {
			when (it.status) {
				Status.LOADING -> loadingDialog.show()
				Status.SUCCESS -> {
					loadingDialog.dismiss()
					val builder = LatLngBounds.Builder()
					it.item?.listStory?.map { story ->
						val lat = story.lat ?: 0.0
						val long = story.lon ?: 0.0
						val latLng = LatLng(lat, long)
						mMap?.addMarker(MarkerOptions().position(latLng).title(story.name))
						builder.include(latLng)
					}
					val tmpBounds = builder.build()
					mMap?.animateCamera(
						CameraUpdateFactory.newLatLngBounds(
							tmpBounds,
							resources.displayMetrics.widthPixels,
							resources.displayMetrics.heightPixels,
							300
						)
					)
				}
				Status.ERROR -> {
					loadingDialog.dismiss()
					Snackbar.make(binding.root, it.throwable?.message.toString(), Snackbar.LENGTH_INDEFINITE)
						.setAction(
							getString(android.R.string.ok)
						) { viewModel.getStoriesWithLocation() }.show()
				}
			}
		}
	}

	private fun setMapStyle() {
		try {
			val success =
				mMap?.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
			if (success != null && !success) {
				Log.e(TAG, "Style parsing failed.")
			}
		} catch (exception: Resources.NotFoundException) {
			Log.e(TAG, "Can't find style. Error: ", exception)
		}
	}

	@SuppressLint("MissingPermission")
	private fun setMyLocation() {
		if (!checkPermissions()) {
			requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
			return
		}
		mMap?.isMyLocationEnabled = true
	}

	private val requestPermissionLauncher =
		registerForActivityResult(
			ActivityResultContracts.RequestMultiplePermissions()
		) { isGranted ->
			if (isGranted[Manifest.permission.ACCESS_FINE_LOCATION] == true && isGranted[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
				setMyLocation()
			}
		}

	private fun checkPermissions() = REQUIRED_PERMISSIONS.all { permission ->
		ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
	}

	companion object {
		private val REQUIRED_PERMISSIONS = arrayOf(
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.ACCESS_COARSE_LOCATION
		)
		private const val TAG = "MapsFragment"
	}
}