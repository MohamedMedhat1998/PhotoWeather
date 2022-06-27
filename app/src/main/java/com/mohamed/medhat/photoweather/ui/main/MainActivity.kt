package com.mohamed.medhat.photoweather.ui.main

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mohamed.medhat.photoweather.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

/**
 * The main screen of the app.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
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
            mainViewModel.sendTakePhotoIntent(cameraResultLauncher, this)
        }
    }

    /**
     * Camera result callback (onActivityResult) alternative.
     */
    private val cameraResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // TODO navigate to image modification activity.
            }
        }
}