package com.mohamed.medhat.photoweather.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohamed.medhat.photoweather.databinding.ActivityMainBinding
import com.mohamed.medhat.photoweather.ui.BaseActivity
import com.mohamed.medhat.photoweather.ui.preview.PreviewActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val IMAGE_PATH = "image-path"
private const val TAG = "MainActivity"

/**
 * The main screen of the app.
 */
@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        registerObservers()
    }

    /**
     * Initializes the UI.
     */
    private fun initViews() {
        binding.fabMainTakePhoto.setOnClickListener {
            mainViewModel.sendTakePhotoIntent()
        }
        binding.rvMainHistory.layoutManager = LinearLayoutManager(this)
        binding.rvMainHistory.adapter = historyAdapter
    }

    /**
     * Subscribes to the observable fields in the [mainViewModel].
     */
    private fun registerObservers() {
        mainViewModel.cameraIntent.observe(this) {
            if (mainViewModel.canOpenCamera) {
                mainViewModel.canOpenCamera = false
                cameraResultLauncher.launch(it)
            }
        }
        mainViewModel.history.observe(this) {
            if (it.isEmpty()) {
                binding.rvMainHistory.visibility = View.INVISIBLE
                binding.tvMainNoHistory.visibility = View.VISIBLE
            } else {
                binding.rvMainHistory.visibility = View.VISIBLE
                binding.tvMainNoHistory.visibility = View.INVISIBLE
            }
            historyAdapter.submitList(it)
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