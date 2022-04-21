package com.dhiva.storyapp.ui.maps

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.dhiva.storyapp.R
import com.dhiva.storyapp.data.remote.Resource
import com.dhiva.storyapp.databinding.ActivityMapsBinding
import com.dhiva.storyapp.utils.ViewModelFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val mapsViewModel: MapsViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        initViewModel()
        setMapStyle()
    }

    private fun initViewModel() {
        mapsViewModel.stories().observe(this) {
            when(it){
                is Resource.Loading -> Log.d("", "Loading")
                is Resource.Error -> Log.e("", it.message ?: "")
                is Resource.Success -> {
                    it.data?.let { listStory ->
                        for (story in listStory){
                            if (story.lat != null && story.lon != null){
                                val latLng = LatLng(story.lat.toDouble(), story.lon.toDouble())
                                mMap.addMarker(MarkerOptions().position(latLng).title(story.name))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setMapStyle() {
        try {
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
        } catch (exception: Resources.NotFoundException) {
            exception.printStackTrace()
        }
    }
}