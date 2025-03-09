package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.weatherapp.repository.WeatherWorker
import com.example.weatherapp.ui.theme.DarkColorScheme
import com.example.weatherapp.ui.theme.InfoScreen
import com.example.weatherapp.ui.theme.MainScreen
import com.example.weatherapp.ui.theme.WeatherAppTheme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(colorScheme = DarkColorScheme) { 
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "main"){
                        composable("main") { MainScreen(navController) }
                        composable("info") { InfoScreen { navController.popBackStack() } }
                    }
                }
            }
        }
        // Schedule weather updates every 3 hours
        val workRequest = PeriodicWorkRequestBuilder<WeatherWorker>(3, TimeUnit.HOURS)
            .setConstraints(Constraints.Builder().setRequiresBatteryNotLow(true).build())
            .build()

        // Enqueue the work request
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "weather_update",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
        Greeting("Android")
    }
}