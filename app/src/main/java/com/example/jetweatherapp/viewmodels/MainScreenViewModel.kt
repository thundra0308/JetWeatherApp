package com.example.jetweatherapp.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetweatherapp.data.DataOrException
import com.example.jetweatherapp.model.CurrentWeather
import com.example.jetweatherapp.model.WeatherData
import com.example.jetweatherapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel() {

    val currentWeather: MutableState<DataOrException<CurrentWeather, Boolean, Exception>> = mutableStateOf(DataOrException())
    val forecast: MutableState<DataOrException<WeatherData, Boolean, Exception>> = mutableStateOf(DataOrException())

    fun getCurrentWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            currentWeather.value = DataOrException(loading = true)
            val result = repository.getCurrentWeather(latitude, longitude)
            currentWeather.value = result
            if (result.data != null) {
                currentWeather.value = result.copy(loading = false)
            }
        }
    }

    fun get5Day3HourWeatherForecast(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            forecast.value = DataOrException(loading = true)
            val result = repository.get5Day3HourWeatherForecast(latitude, longitude)
            forecast.value = result
            if (result.data != null) {
                forecast.value = result.copy(loading = false)
            }
        }
    }

}