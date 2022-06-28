package com.mohamed.medhat.photoweather.ui

import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mohamed.medhat.photoweather.R

const val REQUEST_PERMISSIONS_REQUEST_CODE = 2

/**
 * An activity that contains useful methods that are common among most of the activities.
 */
open class BaseActivity : AppCompatActivity() {

    private var onPermissionsGranted = {}
    private val displayedDialogs = mutableListOf<AlertDialog>()

    /**
     * Executes the passed lambda if the passed permissions are granted.
     * @param permissions The required permissions to execute the code.
     * @param permissionsFriendlyNames Permission names that will be displayed to the user.
     * @param onPermissionsGranted The code to execute when the permissions are granted.
     */
    fun requirePermissions(
        permissions: Array<String>,
        permissionsFriendlyNames: Array<String>,
        onPermissionsGranted: () -> Unit
    ) {
        this.onPermissionsGranted = onPermissionsGranted
        val friendlyNamesBuilder = StringBuilder().apply {
            permissionsFriendlyNames.forEachIndexed { index, s ->
                append(s)
                if (index != permissionsFriendlyNames.lastIndex) {
                    appendLine()
                }
            }
        }
        when {
            permissions.all {
                ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
            } -> {
                onPermissionsGranted.invoke()
            }
            permissions.any { shouldShowRequestPermissionRationale(it) } -> {
                showDialog(
                    title = getString(R.string.requesting_permissions_title),
                    message = getString(
                        R.string.requesting_permissions_message,
                        friendlyNamesBuilder.toString()
                    ),
                    positiveButtonLabel = getString(R.string.requesting_permissions_ok),
                    negativeButtonLabel = getString(R.string.requesting_permissions_cancel),
                    onPositiveButtonClicked = {
                        requestPermissions(permissions)
                    }
                )
            }
            else -> {
                requestPermissions(permissions)
            }
        }
    }

    /**
     * Requests the passed permissions list.
     * @param permissions The permissions to request.
     */
    private fun requestPermissions(permissions: Array<String>) {
        requestPermissions(permissions, REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    /**
     * Displays a dialog to the user.
     */
    private fun showDialog(
        title: String,
        message: String,
        positiveButtonLabel: String,
        negativeButtonLabel: String = "",
        onPositiveButtonClicked: () -> Unit = {},
        onNegativeButtonClicked: () -> Unit = {}
    ) {
        val dialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonLabel) { d, _ ->
                onPositiveButtonClicked.invoke()
                d.dismiss()
                displayedDialogs.clear()
            }
        if (negativeButtonLabel.isNotEmpty()) {
            dialog.setNegativeButton(negativeButtonLabel) { d, _ ->
                onNegativeButtonClicked.invoke()
                d.dismiss()
                displayedDialogs.clear()
            }
        }
        displayedDialogs.add(dialog.show())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED })) {
                onPermissionsGranted.invoke()
            } else {
                showDialog(
                    title = getString(R.string.permission_denied_title),
                    message = getString(R.string.permission_denied_message),
                    positiveButtonLabel = getString(R.string.permissions_denied_ok)
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        displayedDialogs.forEach {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }
}