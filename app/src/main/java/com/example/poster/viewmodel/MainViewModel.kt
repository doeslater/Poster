package com.example.poster.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.poster.data.model.Article
import com.example.poster.data.model.forcast.ForecastResponse
import com.example.poster.data.model.weather.WeatherResponse
import com.example.poster.data.repository.NewsRepository
import com.example.poster.data.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers

class MainViewModel() : ViewModel() {

    private val newsRepository = NewsRepository()
    private val weatherRepository = WeatherRepository()

    fun getBreakingNews(): LiveData<List<Article>?>{
        return liveData(Dispatchers.Default) {
            emit(newsRepository.getBreakingNews())
        }
    }

    fun getWeather(): LiveData<WeatherResponse?>{
        return liveData(Dispatchers.Default) {
            emit(weatherRepository.getWeather("London"))
        }
    }

    fun getForecast(): LiveData<ForecastResponse?>{
        return liveData(Dispatchers.Default) {
            emit(weatherRepository.getForecast("London"))
        }
    }

    companion object {
        val TAG = MainViewModel::class.java.name
    }
}