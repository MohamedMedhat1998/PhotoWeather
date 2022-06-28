package com.mohamed.medhat.photoweather.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.mohamed.medhat.photoweather.databinding.ActivityMainBinding
import com.mohamed.medhat.photoweather.ui.BaseActivity
import com.mohamed.medhat.photoweather.ui.preview.PreviewActivity
import dagger.hilt.android.AndroidEntryPoint

const val IMAGE_PATH = "image-path"

/**
 * The main screen of the app.
 */
@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    /**
     * Initializes the UI.
     */
    private fun initViews() {
        binding.fabMainTakePhoto.setOnClickListener {
            mainViewModel.sendTakePhotoIntent(cameraResultLauncher)
        }
    }

    /**
     * Camera result callback. (onActivityResult) alternative.
     */
    private val cameraResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                startActivity(Intent(this, PreviewActivity::class.java).apply {
                    putExtra(IMAGE_PATH, mainViewModel.latestImageLocation)
                })
            }
        }
}