package com.example.weatherapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.WeatherResponse
import com.example.weatherapp.repository.DataStoreManager
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
    private val _weatherState = MutableStateFlow<WeatherState?>(null)
    val weatherState = _weatherState.asStateFlow()
    private val apiKey = "b3c9d0b6308e48bad5a80f61967a3ab0"
    private val dataStoreManager = DataStoreManager(application)
    private val _lastCity = MutableStateFlow("")
    val lastCity = _lastCity.asStateFlow()

    init {
        viewModelScope.launch {
            dataStoreManager.lastCity.collect { city ->
                _lastCity.value = city ?: ""
            }
        }
    }

    fun saveCity(city: String) {
        viewModelScope.launch {
            dataStoreManager.saveLastCity(city)
        }
    }

    fun fetchWeather(city: String) {
        _weatherState.value = WeatherState.Loading // Show loading before API call
        viewModelScope.launch {
            try {
                val response = repository.getWeather(city, apiKey)
                _weatherState.value = WeatherState.Success(response)
            } catch (e: Exception) {
                _weatherState.value = WeatherState.Error("Failed to load weather data") // Handle Error
            }
        }
    }
}