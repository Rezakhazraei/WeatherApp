package com.example.weatherapp.viewmodel

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.WeatherResponse
import com.example.weatherapp.repository.LocationManager
import com.example.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class WeatherState {
    object Loading : WeatherState()
    data class Success(val data: WeatherResponse) : WeatherState()
    data class Error(val message: String) : WeatherState()
}

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WeatherRepository()
    private val locationManager = LocationManager(application)
    private val _weatherState = MutableStateFlow<WeatherState?>(null)
    val weatherState = _weatherState.asStateFlow()
    private val apiKey = "b3c9d0b6308e48bad5a80f61967a3ab0"
    private val _cityName = MutableStateFlow("")
    val cityName = _cityName.asStateFlow()

    private var lastFetchedLocation: Location? = null


    fun fetchWeather(city: String) {
        _weatherState.value = WeatherState.Loading // Show loading before API call
        viewModelScope.launch {
            try {
                val response = repository.getWeather(city, apiKey)
                _weatherState.value = WeatherState.Success(response)
                _cityName.value = response.name
            } catch (e: Exception) {
                _weatherState.value = WeatherState.Error("Failed to load weather data") // Handle Error
                _cityName.value = "City not found"
            }
        }
    }

    fun fetchWeatherByLocation() {
        viewModelScope.launch {
            try {
                val location: Location? = locationManager.getCurrentLocation()
                if (location != null && lastFetchedLocation != location) {
                    lastFetchedLocation = location
                    Log.d("WeatherApp", "Location: ${location.latitude}, ${location.longitude}")
                    val response = repository.getWeatherByCoordinates(
                        location.latitude, location.longitude, apiKey
                    )
                    _weatherState.value = WeatherState.Success(response)
                    _cityName.value = response.name
                } else {
                    _cityName.value = "Location not found"
                }
            } catch (e: Exception) {
                Log.e("WeatherApp", "Error fetching location weather: ${e.message}")
                _weatherState.value = null
                _cityName.value = "Error fetching location"
            }
        }
    }
}