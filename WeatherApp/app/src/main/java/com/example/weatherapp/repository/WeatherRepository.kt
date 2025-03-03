package com.example.weatherapp.repository

import com.example.weatherapp.data.WeatherApi
import com.example.weatherapp.data.WeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRepository {
    private val api: WeatherApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(WeatherApi::class.java)
    }

    suspend fun getWeather(city: String, apiKey: String): WeatherResponse {
        return api.getWeather(city, apiKey)
    }
}