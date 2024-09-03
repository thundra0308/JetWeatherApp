package com.example.jetweatherapp.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
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
            popUpTo(WeatherScreens.SplashScreen.name) { inclusive = true }
        }
    }
}