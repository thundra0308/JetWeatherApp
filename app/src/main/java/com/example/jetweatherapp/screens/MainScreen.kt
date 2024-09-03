package com.example.jetweatherapp.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jetweatherapp.R
import com.example.jetweatherapp.components.ForecastCard
import com.example.jetweatherapp.components.HorizontalForecastListOf24Hours
import com.example.jetweatherapp.components.LocationPermissionTextProvider
import com.example.jetweatherapp.components.PermissionDialog
import com.example.jetweatherapp.components.TemperatureTextMain
import com.example.jetweatherapp.components.WeatherDescription
import com.example.jetweatherapp.components.WeatherExpandView
import com.example.jetweatherapp.components.WeatherGrid
import com.example.jetweatherapp.components.WeatherViewPager
import com.example.jetweatherapp.model.CurrentWeather
import com.example.jetweatherapp.model.WeatherData
import com.example.jetweatherapp.model.WeatherDataItem
import com.example.jetweatherapp.viewmodels.LocationViewModel
import com.example.jetweatherapp.viewmodels.MainScreenViewModel
import com.example.jetweatherapp.viewmodels.PermissionViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
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
    // States
    val currentLocation = locationViewModel.currentLocationFlow.collectAsState().value
    val currentWeather = mainScreenViewModel.currentWeather.collectAsState().value
    val forecast = mainScreenViewModel.forecast.collectAsState().value

    // Side Effects
    LaunchedEffect(key1 = currentLocation) {
        if (currentLocation != null) {
            mainScreenViewModel.getCurrentWeather(
                currentLocation.latitude, currentLocation.longitude
            )
            mainScreenViewModel.get5Day3HourWeatherForecast(
                currentLocation.latitude, currentLocation.longitude
            )
        }
    }

    // Ui
    SetUp(permissionViewModel, locationViewModel)
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            if (currentWeather.loading == true || forecast.loading == true) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (currentWeather.exception != null || forecast.exception != null) {
                Snackbar {
                    Column {
                        Text(text = currentWeather.exception.toString())
                        Text(text = currentWeather.exception.toString())
                    }
                }
            } else if (currentWeather.data != null && forecast.data != null) {
                val scrollState = rememberScrollState()
                val nestedScrollConnection = object : NestedScrollConnection {}
                val maxScrollOffset = 150f
                val scrollProgress = (scrollState.value / maxScrollOffset).coerceIn(0f, 1f)
                val textSize by animateFloatAsState(
                    targetValue = lerp(32f, 22f, scrollProgress),
                    label = "TopAppBar Title Animation"
                )
                val vectorSize by animateFloatAsState(
                    targetValue = lerp(20f, 0f, scrollProgress),
                    label = "TopAppBar Location Icon Animation"
                )
                val dividerAlpha by animateFloatAsState(
                    targetValue = lerp(0f, 1f, scrollProgress),
                    label = "TopAppBar Elevation Animation"
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clipToBounds(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SearchBar(modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 25.dp, end = 25.dp),
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
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clipToBounds(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            modifier = Modifier
                                .padding(start = 15.dp),
                            text = "${currentWeather.data!!.name}, ${currentWeather.data!!.sys?.country}",
                            style = TextStyle(fontSize = textSize.sp)
                        )
                        Image(
                            modifier = Modifier
                                .padding(start = 15.dp)
                                .size(vectorSize.dp),
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "location icon",
                            colorFilter = ColorFilter.tint(Color.Red),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth(),
                            thickness = 0.5.dp,
                            color = Color.Gray.copy(alpha = dividerAlpha)
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp)
                        .verticalScroll(scrollState)
                        .nestedScroll(nestedScrollConnection)
                        .wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MainContentWrapper(currentWeather.data!!, forecast.data!!)
                }
            }
        }
    }
}

@Composable
fun MainContentWrapper(currentWeather: CurrentWeather, forecastData: WeatherData) {
    MainContent(currentWeather = currentWeather, forecastData = forecastData)
}

@Composable
fun MainContent(currentWeather: CurrentWeather, forecastData: WeatherData) {
    TemperatureTextMain(currentWeather)
    WeatherDescription(currentWeather = currentWeather)
    Spacer(modifier = Modifier.height(70.dp))
    WeatherExpandView(currentWeather = currentWeather)
    Spacer(modifier = Modifier.height(10.dp))
    HorizontalForecastListOf24Hours(forecastData = forecastData)
    Spacer(modifier = Modifier.height(10.dp))
    WeatherViewPager(pagerData = getTomorrowsData(forecastData.list!!))
    Spacer(modifier = Modifier.height(10.dp))
    ForecastCard(forecastData = forecastData)
    Spacer(modifier = Modifier.height(10.dp))
    WeatherGrid(data = getWeatherGridData(currentWeather = currentWeather))
    Spacer(modifier = Modifier.height(10.dp))
    Spacer(modifier = Modifier.height(50.dp))
}

@Composable
fun SetUp(permissionViewModel: PermissionViewModel, locationViewModel: LocationViewModel) {
    val context = LocalContext.current
    val permissionsToRequest = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    val dialogQueue = permissionViewModel.visiblePermissionDialogQueue
    val scope = rememberCoroutineScope()
    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
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
    LaunchedEffect(
        key1 = locationViewModel.currentLocationFlow.collectAsState().value,
        key2 = permissionViewModel.isLocationPermissionGranted.collectAsState().value
    ) {
        if (!dialogQueue.containsAll(permissionsToRequest.toList())) {
            locationViewModel.startLocationUpdates()
        } else {
            locationViewModel.stopLocationUpdates()
        }
    }
    dialogQueue.reversed().forEach { permission ->
        PermissionDialog(permissionTextProvider = when (permission) {
            Manifest.permission.ACCESS_FINE_LOCATION -> {
                LocationPermissionTextProvider()
            }

            else -> return@forEach
        },
            isPermanentlyDeclined = !permissionViewModel.shouldShowRationale(permission),
            onDismiss = permissionViewModel::dismissDialog,
            onOkClick = {
                scope.launch {
                    permissionViewModel.dismissDialog()
                    multiplePermissionResultLauncher.launch(
                        arrayOf(permission)
                    )
                }
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

@Composable
fun lerp(start: Float, end: Float, fraction: Float): Float {
    return start + fraction * (end - start)
}

fun getLocalTimeFromUnixTimestamp(unixTimestamp: String): String {
    val instant = Instant.ofEpochSecond(unixTimestamp.toLong())
    val zoneId = ZoneId.systemDefault()
    val zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId)
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    return zonedDateTime.format(timeFormatter)
}

fun getTomorrowsData(forecastList: List<WeatherDataItem>): List<String> {
    val data = arrayListOf<String>()
    val tomorrowDate = LocalDate.now().plusDays(1).toString()
    forecastList.forEach {
        if (it.dt_txt?.contains(tomorrowDate)!!) {
            data.add("${it.main?.temp.toString()}°")
            data.add("Min : ${it.main?.temp_min}° / Max : ${it.main?.temp_max}°")
            data.add("${it.main?.feels_like.toString()}°")
            return data
        }
    }
    return data
}

fun getDistinctDateData(forecastList: List<WeatherDataItem>): List<WeatherDataItem> {
    val data = arrayListOf<WeatherDataItem>()
    var prev = ""
    for (i in forecastList.indices) {
        val curr = forecastList[i].dt_txt?.split(" ")?.get(0)!!
        if (curr != prev) {
            prev = curr
            data.add(forecastList[i])
        }
    }
    return data
}

fun getWeatherGridData(currentWeather: CurrentWeather): List<List<String>> {
    val data = arrayListOf<ArrayList<String>>()
    data.add(
        arrayListOf(
            R.drawable.ic_sun.toString(),
            "Sunrise",
            getLocalTimeFromUnixTimestamp(currentWeather.sys?.sunrise.toString()),
            ""
        )
    )
    data.add(
        arrayListOf(
            R.drawable.ic_tempmeter.toString(),
            "Feels Like",
            currentWeather.main?.feels_like?.toInt().toString() + "°",
            ""
        )
    )
    data.add(
        arrayListOf(
            R.drawable.ic_humidity.toString(),
            "Humidity",
            currentWeather.main?.humidity.toString() + "%",
            ""
        )
    )
    data.add(
        arrayListOf(
            R.drawable.ic_wind.toString(), "E Wind", currentWeather.wind?.speed.toString(), "mph"
        )
    )
    data.add(
        arrayListOf(
            R.drawable.ic_airquality.toString(),
            "Air Pressure",
            currentWeather.main?.pressure.toString(),
            "hPa"
        )
    )
    data.add(
        arrayListOf(
            R.drawable.ic_visibility.toString(),
            "Visibility",
            currentWeather.visibility.toString(),
            "km"
        )
    )
    return data
}