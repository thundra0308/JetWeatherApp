package com.example.jetweatherapp.viewmodels

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(@ApplicationContext private val context: Context) : ViewModel() {
    val visiblePermissionDialogQueue = mutableStateListOf<String>()
    val isLocationPermissionGranted = mutableStateOf(false)

    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    fun onPermissionResult(permission: String, isGranted: Boolean
    ) {
        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
        isLocationPermissionGranted()
    }

    fun shouldShowRationale(permission: String): Boolean {
        val activity = context as? Activity
        return activity?.shouldShowRequestPermissionRationale(permission) ?: false
    }

    private fun isLocationPermissionGranted() {
        viewModelScope.launch {
            if(ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
                isLocationPermissionGranted.value = true
            }
        }
    }
}