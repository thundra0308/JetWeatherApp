package com.example.jetweatherapp.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.jetweatherapp.R
import com.example.jetweatherapp.navigation.WeatherScreens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val isPlaying = remember {
        mutableStateOf(true)
    }
    val speed = remember {
        mutableFloatStateOf(1f)
    }
    val composition = rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.splash_anime)
    )
    val progress = animateLottieCompositionAsState(
        composition.value,
        iterations = LottieConstants.IterateForever,
        isPlaying = isPlaying.value,
        speed = speed.floatValue,
        restartOnPlay = false
    )
    val scale = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )
        delay(2000L)
        navController.navigate(WeatherScreens.MainScreen.name) {
            popUpTo(WeatherScreens.SplashScreen.name) {inclusive = true}
        }
    }
    Surface (
        modifier = Modifier.scale(scale.value)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .border(2.dp, MaterialTheme.colorScheme.onTertiaryContainer, CircleShape)
                    .clip(CircleShape),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                LottieAnimation(
                    composition.value,
                    progress.value,
                    modifier = Modifier
                        .size(300.dp)
                        .border(BorderStroke(0.dp, Color.Transparent), shape = CircleShape)
                )
            }
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = "Find the Sun ?",
                style = TextStyle(
                    fontSize = 20.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.W500
                )
            )
        }
    }
}