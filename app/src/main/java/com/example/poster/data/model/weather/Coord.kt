package com.example.poster.data.model.weather


import com.google.gson.annotations.SerializedName

data class Coord(
    @SerializedName("lat")
    val lat: String?,
    @SerializedName("lon")
    val lon: String?
)