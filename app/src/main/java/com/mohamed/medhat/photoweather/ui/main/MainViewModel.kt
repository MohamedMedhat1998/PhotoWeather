package com.mohamed.medhat.photoweather.ui.main

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * An mvvm [ViewModel] for the [MainActivity].
 */
@SuppressLint("StaticFieldLeak")
@HiltViewModel
class MainViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {

    var latestImageLocation = ""

    /**
     * Takes a photo using the device's camera.
     * @param resultLauncher The call back that will receive the image from the camera.
     */
    fun sendTakePhotoIntent(resultLauncher: ActivityResultLauncher<Intent>) {
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
        try {
            latestImageLocation = path
            resultLauncher.launch(takePictureIntent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }
}