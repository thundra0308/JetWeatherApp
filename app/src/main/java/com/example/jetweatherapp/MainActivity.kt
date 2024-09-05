package com.example.jetweatherapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.jetweatherapp.navigation.WeatherNavigation
import com.example.jetweatherapp.ui.theme.JetWeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val splashScreen = installSplashScreen()
            splashScreen.setKeepOnScreenCondition { true }
            CoroutineScope(Dispatchers.IO).launch {
                delay(2000)
                splashScreen.setKeepOnScreenCondition { false }
            }
        }
        setContent {
            WeatherApp()
        }
    }

}

@Preview
@Composable
fun WeatherApp() {
    JetWeatherAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            WeatherNavigation()
        }
    }
}