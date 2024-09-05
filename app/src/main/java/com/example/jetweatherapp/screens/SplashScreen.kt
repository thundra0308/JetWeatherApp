package com.example.jetweatherapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.jetweatherapp.navigation.WeatherScreens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(key1 = Unit) {
        delay(2000)
        navController.navigate(WeatherScreens.MainScreen.name) {
            this.popUpTo(WeatherScreens.SplashScreen.name)
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

    }
}