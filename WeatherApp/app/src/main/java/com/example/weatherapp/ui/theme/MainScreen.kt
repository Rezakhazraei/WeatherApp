package com.example.weatherapp.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapp.viewmodel.WeatherState
import com.example.weatherapp.viewmodel.WeatherViewModel


@Composable
fun MainScreen(navController: NavController, viewModel: WeatherViewModel = viewModel()) {
    val lastCity by viewModel.lastCity.collectAsState()
    var city by remember { mutableStateOf(TextFieldValue(lastCity)) }
//  var weatherInfo by remember { mutableStateOf("Enter a city and press search") }
    val weatherState by viewModel.weatherState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Weather App",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Enter city name") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { viewModel.fetchWeather(city.text) }),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                // API call will be added here
                // weatherInfo = "Fetching weather info for ${city.text}..."
                viewModel.fetchWeather(city.text)
                viewModel.saveCity(city.text) // Save city with dataStore
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth()
        ){
            Text("Search", fontSize = MaterialTheme.typography.bodyLarge.fontSize)
        }
        Spacer(modifier = Modifier.height(16.dp))
        when (weatherState) {
            is WeatherState.Loading -> {
                CircularProgressIndicator() // Show loading indicator
            }
            is WeatherState.Success -> {
                val weather = (weatherState as WeatherState.Success).data
                Text(text = "Temperature: ${weather.main.temp}°C")
                Text(text = "Humidity: ${weather.main.humidity}%")
                Text(text = "Wind Speed: ${weather.wind.speed} m/s")
                Text(text = "Condition: ${weather.weather[0].description}")

//                val iconUrl = "https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png"
//                Image(
//                    painter = rememberAsyncImagePainter(iconUrl),
//                    contentDescription = "Weather Icon",
//                    modifier = Modifier.size(100.dp)
//                )
            }
            is WeatherState.Error -> {
                Text(text = (weatherState as WeatherState.Error).message,
                    color = MaterialTheme.colorScheme.error)
            }
            else -> {
                Text(text = "Enter a city and press search",
                    style = MaterialTheme.typography.bodyLarge)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { navController.navigate("info") }) {
            Text("Info")
        }
    }
}