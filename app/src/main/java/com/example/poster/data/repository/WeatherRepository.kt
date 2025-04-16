package com.example.poster.data.repository

import android.util.Log
import com.example.poster.data.model.forcast.ForecastResponse
import com.example.poster.data.model.weather.WeatherResponse
import com.example.poster.data.remote.weather.WeatherRetrofitInstance

class WeatherRepository {
    val TAG = WeatherRepository::class.java.name

    suspend fun getWeather(cityCode: String): WeatherResponse? {
        try {
            val response = WeatherRetrofitInstance.api.getWeatherFromPlace(city = cityCode)

            return when {
                response.isSuccessful -> {
                    response.body()
                }

                else -> {
                    null
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getWeather: ${e.message}")
            return null
        }
    }

    suspend fun getForecast(cityCode: String): ForecastResponse? {
        try {
            val response = WeatherRetrofitInstance.api.getForecastFromPlace(city = cityCode)

            return when {
                response.isSuccessful -> {
                    response.body()
                }

                else -> {
                    null
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getForecast: ${e.message}")
            return null
        }
    }
}