package com.anibalbastias.android.pulentapp.presentation.util

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_PHONE_STATE
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.anibalbastias.android.pulentapp.presentation.ui.resultitem.ResultItemFragment.Companion.REQUEST_ID_MULTIPLE_PERMISSIONS


/**
 * Created by anibalbastias on 2019-09-08.
 */

fun Activity.checkAndRequestPermissions(): Boolean {
    if (SDK_INT >= Build.VERSION_CODES.M) {
        val permissionReadPhoneState =
            ContextCompat.checkSelfPermission(this, READ_PHONE_STATE)
        val permissionStorage =
            ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)
        val listPermissionsNeeded = arrayListOf<String>()

        if (permissionReadPhoneState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(READ_PHONE_STATE)
        }

        if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(READ_EXTERNAL_STORAGE)
        }

        return if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            false
        } else {
            true
        }
    }
    return false
}