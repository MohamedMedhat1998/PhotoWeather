package com.mohamed.medhat.photoweather.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mohamed.medhat.photoweather.di.MainRepo
import com.mohamed.medhat.photoweather.model.HistoryItem
import com.mohamed.medhat.photoweather.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * An mvvm [ViewModel] for the [MainActivity].
 */
@SuppressLint("StaticFieldLeak") // Suppressed as it is fine to use the application context in the ViewModel.
@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    @MainRepo private val repository: Repository
) : ViewModel() {

    var latestImageLocation = ""

    // Introduced to prevent the activity from automatically open the camera on each configuration change
    var canOpenCamera = false

    private val _cameraIntent = MutableLiveData<Intent>()
    val cameraIntent: LiveData<Intent> = _cameraIntent

    val history: LiveData<List<HistoryItem>> = repository.getHistoryItems()

    /**
     * Takes a photo using the device's camera.
     */
    fun sendTakePhotoIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val root = "${context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)}"
        val name = "${SimpleDateFormat("dd-MM-yyyy HH-mm-ss", Locale.US).format(Date())}.png"
        val path = "$root/$name"
        val photoFile = File(path)
        val photoURI =
            FileProvider.getUriForFile(
                context,
                "com.mohamed.medhat.photoweather.fileprovider",
                photoFile
            )
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        latestImageLocation = path
        canOpenCamera = true
        _cameraIntent.value = takePictureIntent
    }
}