package com.mohamed.medhat.photoweather.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mohamed.medhat.photoweather.model.StateHolder
import com.mohamed.medhat.photoweather.utils.State
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File

/**
 * A [ViewModel] that provides functionalities that are common among most of the ViewModels.
 */
@SuppressLint("StaticFieldLeak") // Suppressed because the passed context is an application context.
open class BaseViewModel(@ApplicationContext private val context: Context) : ViewModel() {

    // Introduced to prevent the activity from automatically sharing the image
    var canShareImage = false

    private val _state = MutableLiveData<StateHolder>()
    val state: LiveData<StateHolder> = _state

    private val _shareIntent = MutableLiveData<Pair<Intent, Uri>>()
    val shareIntent: LiveData<Pair<Intent, Uri>> = _shareIntent

    /**
     * Used to share the image with the passed [imagePath].
     * @param imagePath The path of the image to share.
     */
    fun shareImage(imagePath: String) {
        canShareImage = true
        _shareIntent.postValue(createShareImageIntent(imagePath))
    }

    /**
     * Updates the value of the state to [State.STATE_ERROR].
     * @param message The error message to use.
     */
    fun announceErrorState(message: String) {
        _state.postValue(StateHolder(State.STATE_ERROR, message))
    }

    /**
     * A setter for the state object.
     * @param stateHolder The new state to set.
     */
    protected fun setState(stateHolder: StateHolder) {
        _state.postValue(stateHolder)
    }

    /**
     * Creates an intent and a uri for sharing the images and returns it.
     * @param imagePath The path of the image file to share.
     * @return A pair object containing an intent and a uri.
     */
    private fun createShareImageIntent(imagePath: String): Pair<Intent, Uri> {
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
}