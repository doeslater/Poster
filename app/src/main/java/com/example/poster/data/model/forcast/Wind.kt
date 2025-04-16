package com.example.poster.data.model.forcast


import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("deg")
    val deg: String?,
    @SerializedName("gust")
    val gust: String?,
    @SerializedName("speed")
    val speed: String?
)