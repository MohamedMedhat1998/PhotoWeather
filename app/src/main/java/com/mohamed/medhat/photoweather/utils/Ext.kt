package com.mohamed.medhat.photoweather.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import java.io.File

// Contains extension functions

/**
 * Creates an intent and a uri for sharing the images and returns it.
 * @param context The context of the app.
 * @param imagePath The path of the image file to share.
 * @return A pair object containing an intent and a uri.
 */
fun ViewModel.createShareImageIntent(context: Context, imagePath: String): Pair<Intent, Uri> {
    val photoFile = File(imagePath)
    val photoURI =
        FileProvider.getUriForFile(
            context,
            "com.mohamed.medhat.photoweather.fileprovider",
            photoFile
        )
    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.type = "image/*"
    intent.putExtra(Intent.EXTRA_STREAM, photoURI)
    return Pair(intent, photoURI)
}