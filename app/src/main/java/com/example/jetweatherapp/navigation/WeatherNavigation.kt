package com.example.jetweatherapp.navigation

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetweatherapp.screens.AboutScreen
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
    NavHost(navController = navController, startDestination = if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.S) WeatherScreens.MainScreen.name else WeatherScreens.SplashScreen.name) {
        composable(route = WeatherScreens.SplashScreen.name) {
            SplashScreen(navController)
        }
        composable(route = WeatherScreens.MainScreen.name) {
            MainScreen(navController, hiltViewModel<MainScreenViewModel>(), hiltViewModel<PermissionViewModel>(), hiltViewModel<LocationViewModel>())
        }
        composable(route = WeatherScreens.SettingScreen.name) {
            SettingScreen(navController, hiltViewModel<SettingScreenViewModel>())
        }
        composable(route = WeatherScreens.AboutScreen.name) {
            AboutScreen(navController)
        }
    }
}