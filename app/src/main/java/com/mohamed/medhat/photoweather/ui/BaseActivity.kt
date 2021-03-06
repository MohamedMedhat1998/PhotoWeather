package com.mohamed.medhat.photoweather.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar.Duration
import com.mohamed.medhat.photoweather.R

const val REQUEST_PERMISSIONS_REQUEST_CODE = 2

/**
 * An activity that contains useful methods that are common among most of the activities.
 */
open class BaseActivity : AppCompatActivity() {

    private var onPermissionsGranted = {}
    private var onPermissionsDenied = {}
    private val displayedDialogs = mutableListOf<AlertDialog>()

    /**
     * Executes the passed lambda if the passed permissions are granted.
     * @param permissions The required permissions to execute the code.
     * @param permissionsFriendlyNames Permission names that will be displayed to the user.
     * @param onPermissionsGranted The code to execute when the permissions are granted.
     * @param onPermissionsDenied The code to execute when the permissions are denied.
     */
    fun requirePermissions(
        permissions: Array<String>,
        permissionsFriendlyNames: Array<String>,
        onPermissionsGranted: () -> Unit,
        onPermissionsDenied: () -> Unit = {}
    ) {
        this.onPermissionsGranted = onPermissionsGranted
        this.onPermissionsDenied = onPermissionsDenied
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
                showAlertDialog(
                    title = getString(R.string.requesting_permissions_title),
                    message = getString(
                        R.string.requesting_permissions_message,
                        friendlyNamesBuilder.toString()
                    ),
                    positiveButtonLabel = getString(R.string.requesting_permissions_ok),
                    negativeButtonLabel = getString(R.string.requesting_permissions_cancel),
                    onPositiveButtonClicked = {
                        requestPermissions(permissions)
                    },
                    onNegativeButtonClicked = {
                        onPermissionsDenied.invoke()
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
     * Safely Displays a dialog to the user without leaking the window on configuration changes.
     * @param title The title of the dialog.
     * @param message The message of the dialog.
     * @param positiveButtonLabel The text on the positive button.
     * @param negativeButtonLabel The text on the negative button. Leave it empty to delete the negative button.
     * @param onPositiveButtonClicked A Lambda executed when the positive button is clicked.
     * @param onNegativeButtonClicked A Lambda executed when the negative button is clicked.
     */
    fun showAlertDialog(
        title: String,
        message: String,
        positiveButtonLabel: String,
        negativeButtonLabel: String = "",
        onPositiveButtonClicked: () -> Unit = {},
        onNegativeButtonClicked: () -> Unit = {}
    ) {
        val dialog = AlertDialog.Builder(this)
            .setCancelable(false)
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
                showAlertDialog(
                    title = getString(R.string.permission_denied_title),
                    message = getString(R.string.permission_denied_message),
                    positiveButtonLabel = getString(R.string.permissions_denied_ok),
                    onPositiveButtonClicked = {
                        onPermissionsDenied.invoke()
                    }
                )
            }
        }
    }

    /**
     * Shares an image.
     * @param intent The original intent from which a chooser will be created
     * @param uri The uri of the resource to share.
     */
    fun shareImage(intent: Intent, uri: Uri) {
        try {
            val shareIntent = Intent.createChooser(intent, "Share Image")
            val packageName =
                packageManager.resolveActivity(shareIntent, 0)?.activityInfo?.packageName
            grantUriPermission(
                packageName,
                uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            startActivity(shareIntent)
        } catch (e: Exception) {
            e.printStackTrace()
            showToast(getString(R.string.no_share_apps_error))
        }
    }

    /**
     * Displays a toast message to the user.
     * @param message The toast text content.
     * @param duration The duration the toast should last.
     */
    private fun showToast(message: String, @Duration duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(this, message, duration).show()
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