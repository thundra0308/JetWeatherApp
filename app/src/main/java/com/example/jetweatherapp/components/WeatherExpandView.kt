package com.example.jetweatherapp.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetweatherapp.model.CurrentWeather
import kotlinx.coroutines.delay

@Composable
fun WeatherExpandView(currentWeather: CurrentWeather) {
    val currentIndex = remember { mutableIntStateOf(0) }
    val textList = listOf(
        "Wind Speed : ${currentWeather.wind?.speed}m/s",
        "Wind Direction : ${currentWeather.wind?.deg}°"
    )
    LaunchedEffect(Unit) {
        while (true) {
            delay(4000) // Change every 2 seconds
            currentIndex.intValue = (currentIndex.intValue + 1) % textList.size
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp),
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardDefaults.cardColors().containerColor.copy(
                alpha = 0.5f
            )
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnimatedContent(
                    targetState = textList[currentIndex.intValue],
                    transitionSpec = {
                        (slideInVertically(
                            initialOffsetY = { height -> height },
                            animationSpec = tween(durationMillis = 2000),
                        ) + fadeIn(animationSpec = tween(durationMillis = 2000))) togetherWith
                                ((slideOutVertically(
                                    targetOffsetY = { height -> -height },
                                    animationSpec = tween(durationMillis = 2000)
                                ) + fadeOut(animationSpec = tween(durationMillis = 2000))))
                    },
                    label = "Vertical Slide Transition"
                ) { targetText ->
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(start = 18.dp, end = 15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = targetText,
                            style = TextStyle(fontSize = 14.sp,fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
                IconButton(onClick = {

                }) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = "dropdown icon"
                    )
                }
            }
        }
    }
}