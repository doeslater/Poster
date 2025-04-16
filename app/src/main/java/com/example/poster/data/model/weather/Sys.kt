package com.example.poster.data.model.weather


import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("country")
    val country: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("sunrise")
    val sunrise: String?,
    @SerializedName("sunset")
    val sunset: String?,
    @SerializedName("type")
    val type: String?
)