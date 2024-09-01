package com.example.jetweatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetweatherapp.model.Main
import com.example.jetweatherapp.screens.AboutScreen
import com.example.jetweatherapp.screens.FavouriteScreen
import com.example.jetweatherapp.screens.MainScreen
import com.example.jetweatherapp.screens.SearchScreen
import com.example.jetweatherapp.screens.SettingScreen
import com.example.jetweatherapp.screens.SplashScreen
import com.example.jetweatherapp.viewmodels.LocationViewModel
import com.example.jetweatherapp.viewmodels.MainScreenViewModel
import com.example.jetweatherapp.viewmodels.PermissionViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = WeatherScreens.SplashScreen.name) {
        composable(route = WeatherScreens.SplashScreen.name) {
            SplashScreen(navController)
        }
        composable(route = WeatherScreens.MainScreen.name) {
            MainScreen(navController, hiltViewModel<MainScreenViewModel>(), hiltViewModel<PermissionViewModel>(), hiltViewModel<LocationViewModel>())
        }
        composable(route = WeatherScreens.SearchScreen.name) {
            SearchScreen(navController)
        }
        composable(route = WeatherScreens.SettingScreen.name) {
            SettingScreen(navController)
        }
        composable(route = WeatherScreens.FavouriteScreen.name) {
            FavouriteScreen(navController)
        }
        composable(route = WeatherScreens.AboutScreen.name) {
            AboutScreen(navController)
        }
    }
}