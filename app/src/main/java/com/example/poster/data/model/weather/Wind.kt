package com.example.poster.data.model.weather


import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("deg")
    val deg: String?,
    @SerializedName("speed")
    val speed: String?
)