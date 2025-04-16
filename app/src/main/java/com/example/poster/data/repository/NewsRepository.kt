package com.example.poster.data.repository

import android.util.Log
import com.example.poster.data.model.Article
import com.example.poster.data.remote.news.NewsRetrofitInstance

class NewsRepository {
    val TAG = NewsRepository::class.java.name
    suspend fun getBreakingNews(): List<Article>? {
        try {
            val response = NewsRetrofitInstance.api.getBreakingNews()

            return when {
                response.isSuccessful -> {
                    response.body()?.articles
                }
                else -> {
                    null
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getBreakingNews: ${e.message}")
            return null
        }
    }
}
