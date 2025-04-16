package com.example.poster.data.remote.weather

import com.example.poster.data.model.forcast.ForecastResponse
import com.example.poster.data.model.weather.WeatherResponse
import com.example.poster.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    //    api.openweathermap.org/data/2.5/forecast?q=Tel Aviv&appid=591df54841fcb09547a7729c4905e808&units=metric
    @GET("forecast")
    suspend fun getForecastFromPlace(
        @Query("appid") apiKey: String = Constants.Companion.API_KEY_WEATHER_API,
        @Query("q") city: String,
        @Query("units") units: String = "metric"
    ): Response<ForecastResponse>

    // api.openweathermap.org/data/2.5/weather?q=Tel Aviv&appid=591df54841fcb09547a7729c4905e808&units=metric
    @GET("weather")
    suspend fun getWeatherFromPlace(
        @Query("appid") apiKey: String = Constants.Companion.API_KEY_WEATHER_API,
        @Query("q") city: String,
        @Query("units") units: String = "metric"
    ): Response<WeatherResponse>
}