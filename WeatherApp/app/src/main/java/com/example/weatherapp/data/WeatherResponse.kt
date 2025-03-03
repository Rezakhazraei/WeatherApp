package com.example.weatherapp.data

data class WeatherResponse(
    val main: Main,
    val wind: Wind,
    val weather: List<WeatherCondition>
)

data class Main(
    val temp: Double,
    val humidity: Int
)

data class Wind(
    val speed: Double
)

data class WeatherCondition(
    val description: String,
    val icon: String,
    val main: String
)