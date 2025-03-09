package com.example.weatherapp.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationManager(context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location? {
        return suspendCancellableCoroutine { continuation ->
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setWaitForAccurateLocation(true)
                .setMinUpdateIntervalMillis(10000)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location = locationResult.lastLocation
                    if (location != null && continuation.isActive) {
                        continuation.resume(location)
                        fusedLocationClient.removeLocationUpdates(this) // Stop updates
                    }
                }
            }

            // First, try getting the last known location
            val locationTask: Task<Location> = fusedLocationClient.lastLocation
            locationTask.addOnSuccessListener { location ->
                if (location != null && continuation.isActive) {
                    continuation.resume(location)
                } else {
                    // If no last location, request fresh location updates
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
                }
            }
            locationTask.addOnFailureListener { exception ->
                Log.e("LocationManager", "Failed to get location: ${exception.message}")
                if (continuation.isActive) continuation.resumeWithException(exception)
            }

            // Ensure that only one callback is triggered
            continuation.invokeOnCancellation {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
    }
}