package com.example.poster.data.model.forcast


import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("description")
    val description: String?,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("main")
    val main: String?
)