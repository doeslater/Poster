package com.example.poster.data.remote.news

import com.example.poster.data.model.NewsResponse
import com.example.poster.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    // https://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=3548626ccf3146fbadd58d44f730c531
    @GET("/v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("sources")
        sources: String = "bbc-news",
        @Query("apiKey")
        apiKey: String = Constants.Companion.API_KEY_NEWS_API
    ): Response<NewsResponse>

}