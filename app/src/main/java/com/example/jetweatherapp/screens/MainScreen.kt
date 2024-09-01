package com.example.jetweatherapp.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.jetweatherapp.components.LocationPermissionTextProvider
import com.example.jetweatherapp.components.PermissionDialog
import com.example.jetweatherapp.components.UpcomingForecast
import com.example.jetweatherapp.model.CurrentWeather
import com.example.jetweatherapp.model.WeatherData
import com.example.jetweatherapp.viewmodels.LocationViewModel
import com.example.jetweatherapp.viewmodels.MainScreenViewModel
import com.example.jetweatherapp.viewmodels.PermissionViewModel
import com.example.jetweatherapp.widgets.TemperatureText
import com.example.jetweatherapp.widgets.WormIndicator
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    mainScreenViewModel: MainScreenViewModel,
    permissionViewModel: PermissionViewModel,
    locationViewModel: LocationViewModel
) {
    SetUp(permissionViewModel, locationViewModel)

    // States
    val currentLocation = locationViewModel.currentLocation.value
    val currentWeather = mainScreenViewModel.currentWeather.value
    val forecast = mainScreenViewModel.forecast.value
    val scrollState = rememberScrollState()
    val maxScrollOffset = 150f
    val scrollProgress = (scrollState.value / maxScrollOffset).coerceIn(0f, 1f)
    val textSize by animateFloatAsState(
        targetValue = lerp(32f, 22f, scrollProgress),
        label = "TopAppBar Title Animation"
    )
    val elevation by animateDpAsState(
        targetValue = lerp(0.dp, 4.dp, scrollProgress),
        label = "TopAppBar Elevation Animation"
    )

    // Side Effects
    LaunchedEffect(key1 = currentLocation) {
        if (currentLocation != null) {
            mainScreenViewModel.getCurrentWeather(
                currentLocation.latitude,
                currentLocation.longitude
            )
            mainScreenViewModel.get5Day3HourWeatherForecast(
                currentLocation.latitude,
                currentLocation.longitude
            )
        }
    }

    // Ui
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (currentWeather.loading == true || forecast.loading == true) {
                val (cpiMain) = createRefs()
                CircularProgressIndicator(
                    modifier = Modifier.constrainAs(cpiMain) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
            } else if (currentWeather.exception != null || forecast.exception != null) {
                Snackbar {
                    Column {
                        Text(text = currentWeather.exception.toString())
                        Text(text = currentWeather.exception.toString())
                    }
                }
            } else {
                val (colTop, colMain) = createRefs()
                Column(
                    modifier = Modifier
                        .constrainAs(colTop) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .fillMaxWidth()
                        .wrapContentHeight(),
                ) {
                    Surface(
                        shadowElevation = elevation
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                        ) {
                            SearchBar(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp, end = 20.dp),
                                shadowElevation = 0.dp,
                                tonalElevation = 5.dp,
                                shape = RoundedCornerShape(15.dp),
                                query = "",
                                onQueryChange = {},
                                onSearch = {},
                                active = false,
                                onActiveChange = {}
                            ) {

                            }
                            Spacer(modifier = Modifier.height(30.dp))
                            Text(
                                text = "${currentWeather.data?.name}, ${currentWeather.data?.sys?.country}",
                                style = TextStyle(fontSize = textSize.sp)
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .constrainAs(colMain) {
                            top.linkTo(colTop.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(start = 25.dp, end = 25.dp)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    currentWeather.data?.let {
                        forecast.data?.let { it1 ->
                            MainContent(
                                currentWeather = it,
                                forecastData = it1
                            )
                        }
                    }
                    repeat(10) { index ->
                        Text(
                            text = "Item $index",
                            fontSize = 24.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainContent(currentWeather: CurrentWeather, forecastData: WeatherData) {
    var currentIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })
    val textList = listOf("First Text", "Second Text")
    LaunchedEffect(Unit) {
        while (true) {
            delay(4000) // Change every 2 seconds
            currentIndex = (currentIndex + 1) % textList.size
        }
    }
    TemperatureText(
        modifier = Modifier
            .padding(top = 50.dp),
        style1 = TextStyle(
            fontSize = 120.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default
        ),
        style2 = TextStyle(
            fontSize = 80.sp,
            fontWeight = FontWeight.Bold
        ),
        temperature = currentWeather.main?.temp?.toInt()!!,
        unit = "C"
    )
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Partly Cloudy 26° / 35° Ait Quality: 81 - Satisfactory",
        style = TextStyle(fontSize = 18.sp, textAlign = TextAlign.Center, lineHeight = 26.sp)
    )
    Spacer(modifier = Modifier.height(70.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
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
                    targetState = textList[currentIndex],
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
                            .padding(start = 25.dp, end = 25.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = targetText,
                            style = TextStyle(fontSize = 18.sp)
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
    Spacer(modifier = Modifier.height(10.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardDefaults.cardColors().containerColor.copy(
                alpha = 0.5f
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                horizontalArrangement = Arrangement.spacedBy(25.dp)
            ) {
                items(forecastData.list!!) {
                    UpcomingForecast(
                        iconId = it.weather?.get(0)?.icon!!,
                        time = getLocalTimeFromUnixTimestamp(it.dt.toString()),
                        temperature = it.main?.temp?.toInt().toString()
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardDefaults.cardColors().containerColor.copy(
                alpha = 0.5f
            )
        )
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize(),
        ) {
            val (hzpager,wormi) = createRefs()
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.constrainAs(hzpager) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ) { page ->
                PageContent(page = page)
            }
            WormIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .padding(5.dp)
                    .constrainAs(wormi) {
                        top.linkTo(hzpager.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                count = 3
            )
        }
    }
}

@Composable
fun PageContent(page: Int) {
    val heading = when (page) {
        0 -> "Tomorrow's Temperature"
        1 -> "Six Hour Storm Outlook"
        2 -> "Tomorrow's Feels Like Temperature"
        else -> "Not Available"
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = heading, style = TextStyle(fontSize = 18.sp))
        Text(text = heading, style = TextStyle(fontSize = 14.sp))
    }
}

@Composable
fun lerp(start: Float, end: Float, fraction: Float): Float {
    return start + fraction * (end - start)
}

@Composable
fun lerp(start: Dp, end: Dp, fraction: Float): Dp {
    return start + fraction * (end - start)
}

@Composable
fun SetUp(permissionViewModel: PermissionViewModel, locationViewModel: LocationViewModel) {
    val context = LocalContext.current

    val permissionsToRequest = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
    )

    val dialogQueue = permissionViewModel.visiblePermissionDialogQueue

    val multiplePermissionResultLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { perms ->
                permissionsToRequest.forEach { permission ->
                    permissionViewModel.onPermissionResult(
                        permission = permission, isGranted = perms[permission] == true
                    )
                }
            })

    LaunchedEffect(key1 = Unit) {
        multiplePermissionResultLauncher.launch(permissionsToRequest)
    }

    LaunchedEffect(locationViewModel.currentLocation.value) {
        if (!dialogQueue.containsAll(permissionsToRequest.toList())) {
            locationViewModel.startLocationUpdates()
        } else {
            locationViewModel.stopLocationUpdates()
        }
        Log.d(
            "LOCATION_TAG",
            "${locationViewModel.currentLocation.value?.latitude} ${locationViewModel.currentLocation.value?.longitude}"
        )
    }

    dialogQueue.reversed().forEach { permission ->
        PermissionDialog(permissionTextProvider = when (permission) {
            Manifest.permission.ACCESS_FINE_LOCATION -> {
                LocationPermissionTextProvider()
            }

            Manifest.permission.ACCESS_COARSE_LOCATION -> {
                LocationPermissionTextProvider()
            }

            else -> return@forEach
        },
            isPermanentlyDeclined = !permissionViewModel.shouldShowRationale(permission),
            onDismiss = permissionViewModel::dismissDialog,
            onOkClick = {
                permissionViewModel.dismissDialog()
                multiplePermissionResultLauncher.launch(
                    arrayOf(permission)
                )
            },
            onGoToAppSettingsClick = {
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.packageName, null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            })
    }

}

fun getLocalTimeFromUnixTimestamp(unixTimestamp: String): String {
    val instant = Instant.ofEpochSecond(unixTimestamp.toLong())
    val zoneId = ZoneId.systemDefault()
    val zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId)
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    return zonedDateTime.format(timeFormatter)
}