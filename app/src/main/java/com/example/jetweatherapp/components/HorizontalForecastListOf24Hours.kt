package com.example.jetweatherapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jetweatherapp.model.WeatherData
import com.example.jetweatherapp.screens.getLocalTimeFromUnixTimestamp

@Composable
fun HorizontalForecastListOf24Hours(forecastData: WeatherData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(forecastData.list?.subList(0, 10)!!) {
                UpcomingForecast(
                    iconId = it.weather?.get(0)?.icon!!,
                    time = getLocalTimeFromUnixTimestamp(it.dt.toString()),
                    temperature = it.main?.temp?.toInt().toString()
                )
            }
        }
    }
}