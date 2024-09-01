package com.example.jetweatherapp.network

import com.example.jetweatherapp.model.LocationData
import com.example.jetweatherapp.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface GeocodingApi {

    @GET("geo/1.0/direct")
    suspend fun getCoordinatesByLocationName(
        @Query("q") locationName: String,
        @Query("limit") limit: Int = 1,
        @Query("appid") apiKey: String = Constants.API_KEY
    ): LocationData

    @GET("geo/1.0/zip")
    suspend fun getLocationByZipCode(
        @Query("zip") zipCode: String,
        @Query("appid") apiKey: String = Constants.API_KEY
    ): LocationData

}