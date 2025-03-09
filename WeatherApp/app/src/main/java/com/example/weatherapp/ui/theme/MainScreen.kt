package com.example.weatherapp.ui.theme

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapp.viewmodel.WeatherState
import com.example.weatherapp.viewmodel.WeatherViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, viewModel: WeatherViewModel = viewModel()) {
    var city by remember { mutableStateOf(TextFieldValue("")) }
    val cityName by viewModel.cityName.collectAsState()
//  var weatherInfo by remember { mutableStateOf("Enter a city and press search") }
    val weatherState by viewModel.weatherState.collectAsState()

    // Location permission request launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                viewModel.fetchWeatherByLocation()
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(
                    text = "Weather App",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )}
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surface,
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    IconButton(
                        onClick = {locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)},
                    ) {
                        Icon(Icons.Filled.LocationOn, contentDescription = "Use My Location")
                    }
                    IconButton(onClick = { navController.navigate("info") }) {
                        Icon(Icons.Filled.Info, contentDescription = "Info")
                    }
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Enter city name") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { viewModel.fetchWeather(city.text) }),
                singleLine = true,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            // API call will be added here
                            // weatherInfo = "Fetching weather info for ${city.text}..."
                            viewModel.fetchWeather(city.text)
                        }
                    ){
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                }

            )

            Spacer(modifier = Modifier.height(16.dp))

            if(cityName.isNotEmpty()) {
                Text(text = "City: $cityName", fontSize = 20.sp, style = MaterialTheme.typography.bodyMedium)
            }

            when (weatherState) {
                is WeatherState.Loading -> CircularProgressIndicator()
                is WeatherState.Success -> WeatherInfo((weatherState as WeatherState.Success).data)
                is WeatherState.Error -> Text(
                    text = (weatherState as WeatherState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
                else -> Text(text = "Enter a city and press search",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}