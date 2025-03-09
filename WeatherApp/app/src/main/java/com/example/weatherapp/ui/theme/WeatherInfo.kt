@file:Suppress("DEPRECATION")

package com.example.weatherapp.ui.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import com.airbnb.lottie.compose.*
import com.example.weatherapp.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.data.WeatherResponse

@Composable
fun WeatherInfo(weather: WeatherResponse) {

    val animationResult = when (weather.weather[0].main) {
        "Clear" -> R.raw.sunny_animation // Sunny weather animation
        "Clouds" -> R.raw.cloudy_animation // Cloudy weather animation
        "Rain" -> R.raw.rainy_animation // Rainy weather animation
        "Snow" -> R.raw.snowy_animation // Snowy weather animation
        else -> R.raw.default_animation // Default animation for unknown weather
    }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animationResult))
    val progress by animateLottieCompositionAsState(composition)
    // Display the weather information
    val backgroundColor = when (weather.weather[0].main) {
        "Clear" -> Color(0xFF87CEEB) // Light blue for sunny weather
        "Clouds" -> Color(0xFFB0BEC5) // Gray for cloudy weather
        "Rain" -> Color(0xFF78909C) // Dark blue for rainy weather
        "Snow" -> Color(0xFF78909C) // Dark blue for snowy weather
        else -> MaterialTheme.colorScheme.surface
    }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(animationSpec = tween(1000)),
            modifier = Modifier.absolutePadding(top = 16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LottieAnimation(composition, progress, modifier = Modifier.size(120.dp))
                Text(text = "Temperature: ${weather.main.temp}°C", fontSize = 24.sp)
                Text(text = "Humidity: ${weather.main.humidity}%", fontSize = 18.sp)
                Text(text = "Wind Speed: ${weather.wind.speed} m/s", fontSize = 18.sp)
                Text(text = "Condition: ${weather.weather[0].description}", fontSize = 20.sp)
            }
        }
    }
}