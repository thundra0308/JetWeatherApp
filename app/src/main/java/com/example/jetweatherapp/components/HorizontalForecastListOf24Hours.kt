package com.example.jetweatherapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.jetweatherapp.model.WeatherData
import com.example.jetweatherapp.screens.getLocalTimeFromUnixTimestamp

@Composable
fun HorizontalForecastListOf24Hours(forecastData: WeatherData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val scrollState = rememberLazyListState()
        LazyRow(
            modifier = Modifier.fadingEdgeEffectHorizontal(scrollState),
            contentPadding = PaddingValues(horizontal = 15.dp, vertical = 0.dp),
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

fun Modifier.fadingEdgeEffectHorizontal(scrollState: LazyListState): Modifier = this.drawBehind {
    // Define the width of the fade at the start and end
    val fadeWidth = 50.dp.toPx()

    // Start (left) gradient fade
    drawRect(
        brush = Brush.horizontalGradient(
            colors = listOf(Color.Transparent, Color.Black),
            startX = 0f,
            endX = fadeWidth
        )
    )

    // End (right) gradient fade
    val lastVisibleItemIndex = scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
    if (lastVisibleItemIndex != null && lastVisibleItemIndex < scrollState.layoutInfo.totalItemsCount - 1) {
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(Color.Black, Color.Transparent),
                startX = size.width - fadeWidth,
                endX = size.width
            )
        )
    }
}