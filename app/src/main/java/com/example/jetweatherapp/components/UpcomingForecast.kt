package com.example.jetweatherapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

@Preview
@Composable
fun UpcomingForecast(
    iconId: String = "10d",
    time: String = "20:00",
    temperature: String = "18"
) {
    Card(
        modifier = Modifier
            .size(120.dp)
            .background(color = Color.Transparent),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val painter =
                rememberAsyncImagePainter("https://openweathermap.org/img/wn/$iconId@2x.png")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .shadow(elevation = 0.dp)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = time,
                    style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                )
            }
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .weight(2f)
                    .shadow(elevation = 0.dp)
                    .background(shape = RoundedCornerShape(15.dp), color = if(isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer)
            ) {
                Image(
                    painter = painter,
                    contentDescription = "Loaded Image",
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center),
                    contentScale = ContentScale.Fit
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .shadow(elevation = 0.dp)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = "$temperature°",
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}