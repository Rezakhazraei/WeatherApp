package com.example.weatherapp.repository

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherapp.data.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val repository = WeatherRepository()
    private val locationManager = LocationManager(context)

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val location = locationManager.getCurrentLocation()
                if (location != null) {
                    val response: WeatherResponse = repository.getWeatherByCoordinates(
                        location.latitude,
                        location.longitude,
                        "b3c9d0b6308e48bad5a80f61967a3ab0"
                    )
                }
                Result.success()
            } catch (e: Exception) {
                Result.retry()
            }
        }
    }
}