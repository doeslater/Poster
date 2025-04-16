package com.example.poster.data.model.forcast


import com.google.gson.annotations.SerializedName

data class Coord(
    @SerializedName("lat")
    val lat: String?,
    @SerializedName("lon")
    val lon: String?
)