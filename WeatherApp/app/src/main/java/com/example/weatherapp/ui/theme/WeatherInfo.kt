package com.example.weatherapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.weatherapp.data.WeatherResponse

@Composable
fun WeatherInfo(weather: WeatherResponse) {
    // Display the weather information
    val backgroundColor = when (weather.weather[0].main) {
        "Clear" -> Color(0xFF87CEEB) // Light blue for sunny weather
        "Clouds" -> Color(0xFFB0BEC5) // Gray for cloudy weather
        "Rain" -> Color(0xFF78909C) // Dark blue for rainy weather
        else -> MaterialTheme.colorScheme.surface
    }
}