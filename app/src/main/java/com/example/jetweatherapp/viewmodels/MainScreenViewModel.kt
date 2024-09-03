package com.example.jetweatherapp.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetweatherapp.data.DataOrException
import com.example.jetweatherapp.model.CurrentWeather
import com.example.jetweatherapp.model.LocationDataItem
import com.example.jetweatherapp.model.WeatherData
import com.example.jetweatherapp.repository.GeocodingRepository
import com.example.jetweatherapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(private val repository: WeatherRepository, private val geocodingRepository: GeocodingRepository) : ViewModel() {

    private val _currentWeather = MutableStateFlow(DataOrException<CurrentWeather, Boolean, Exception>())
    val currentWeather: StateFlow<DataOrException<CurrentWeather, Boolean, Exception>> = _currentWeather.asStateFlow()

    private val _forecast = MutableStateFlow(DataOrException<WeatherData, Boolean, Exception>())
    val forecast: StateFlow<DataOrException<WeatherData, Boolean, Exception>> = _forecast.asStateFlow()

    private val _coordinates = MutableStateFlow(DataOrException<ArrayList<LocationDataItem>, Boolean, Exception>())
    val coordinates: StateFlow<DataOrException<ArrayList<LocationDataItem>, Boolean, Exception>> = _coordinates.asStateFlow()

    fun getCurrentWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _currentWeather.emit(DataOrException(loading = true))
            val result = repository.getCurrentWeather(latitude, longitude)
            _currentWeather.emit(result)
            if (result.data != null) {
                _currentWeather.emit(result.copy(loading = false))
            }
        }
    }

    fun get5Day3HourWeatherForecast(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _forecast.emit(DataOrException(loading = true))
            val result = repository.get5Day3HourWeatherForecast(latitude, longitude)
            _forecast.emit(result)
            if (result.data != null) {
                _forecast.emit(result.copy(loading = false))
            }
        }
    }

    fun getCoordinatesByLocationName(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) return@launch
            _coordinates.emit(DataOrException(loading = true))
            val result = geocodingRepository.getCoordinatesByLocationName(query)
            _coordinates.emit(result)
            if (result.data != null && result.data!!.isNotEmpty()) {
                _coordinates.emit(result.copy(loading = false))
            }
        }
    }

    fun getCoordinatesByZipCode(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) return@launch
            _coordinates.emit(DataOrException(loading = true))
            val result = geocodingRepository.getCoordinatesByZipCode(query)
            _coordinates.emit(result)
            if (result.data != null && result.data!!.isNotEmpty()) {
                _coordinates.emit(result.copy(loading = false))
            }
        }
    }

}