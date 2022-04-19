package com.dhiva.storyapp.ui.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dhiva.storyapp.R
import com.dhiva.storyapp.data.remote.Resource
import com.dhiva.storyapp.databinding.ActivityAddStoryBinding
import com.dhiva.storyapp.ui.camera.CameraActivity
import com.dhiva.storyapp.ui.main.MainActivity
import com.dhiva.storyapp.utils.ViewModelFactory
import com.dhiva.storyapp.utils.rotateBitmap
import com.dhiva.storyapp.utils.toast
import com.dhiva.storyapp.utils.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private val addStoryViewModel: AddStoryViewModel by viewModels {
        ViewModelFactory(this)
    }
    private var getFile: File? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var location: Location? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        init()
        getLocation()
    }

    private fun getLocation(){
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    this.location = location
                } else {
                    this.toast(getString(R.string.location_not_found))
                }
            }
        }
    }

    private fun init() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        with(binding) {
            ibBack.setOnClickListener {
                finish()
            }
            btGallery.setOnClickListener {
                startGallery()
            }
            btCamera.setOnClickListener {
                startCameraX()
            }
            btUpload.setOnClickListener {
                uploadImage()
            }
        }
    }

    private fun uploadImage() {
        if (getFile != null) {
            upload()
        } else {
            this.toast(resources.getString(R.string.no_file))
        }
    }

    private fun upload() {
        addStoryViewModel.uploadStory(getFile, binding.etDesc.text.toString(), location).observe(this) { result ->
            when (result) {
                is Resource.Loading -> isLoadingShown(true)
                is Resource.Success -> {
                    isLoadingShown(false)
                    result.data?.let {
                        this.toast(it.message)
                        if (!it.error) {
                            val intent = Intent()
                            setResult(MainActivity.INTENT_RESULT, intent)
                            finish()
                        }
                    }
                }
                is Resource.Error -> {
                    isLoadingShown(false)
                    this.toast(result.message ?: resources.getString(R.string.something_wrong))
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!checkPermission(Manifest.permission.CAMERA)) {
                this.toast(resources.getString(R.string.no_permission))
                finish()
            } else {
                getLocation()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra(CameraActivity.EXTRA_PICTTURE) as File
            val isBackCamera =
                it.data?.getBooleanExtra(CameraActivity.EXTRA_IS_BACK_CAMERA, true) as Boolean

            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )

            binding.ivStory.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this)

            getFile = myFile

            binding.ivStory.setImageURI(selectedImg)
        }
    }

    private fun isLoadingShown(isShow: Boolean) {
        binding.pbLoading.visibility = if (isShow) View.VISIBLE else View.GONE
        binding.layout.visibility = if (isShow) View.GONE else View.VISIBLE
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}