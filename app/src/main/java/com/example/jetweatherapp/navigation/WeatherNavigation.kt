package com.example.jetweatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetweatherapp.screens.MainScreen
import com.example.jetweatherapp.screens.SettingScreen
import com.example.jetweatherapp.screens.SplashScreen
import com.example.jetweatherapp.viewmodels.LocationViewModel
import com.example.jetweatherapp.viewmodels.MainScreenViewModel
import com.example.jetweatherapp.viewmodels.PermissionViewModel
import com.example.jetweatherapp.viewmodels.SettingScreenViewModel

@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = WeatherScreens.MainScreen.name) {
        composable(route = WeatherScreens.SplashScreen.name) {
            SplashScreen(navController)
        }
        composable(route = WeatherScreens.MainScreen.name) {
            MainScreen(navController, hiltViewModel<MainScreenViewModel>(), hiltViewModel<PermissionViewModel>(), hiltViewModel<LocationViewModel>())
        }
        composable(route = WeatherScreens.SettingScreen.name) {
            SettingScreen(navController, hiltViewModel<SettingScreenViewModel>())
        }
    }
}