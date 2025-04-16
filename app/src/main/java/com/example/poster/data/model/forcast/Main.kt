package com.example.poster.data.model.forcast


import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("feels_like")
    val feelsLike: String?,
    @SerializedName("grnd_level")
    val grndLevel: String?,
    @SerializedName("humidity")
    val humidity: String?,
    @SerializedName("pressure")
    val pressure: String?,
    @SerializedName("sea_level")
    val seaLevel: String?,
    @SerializedName("temp")
    val temp: String?,
    @SerializedName("temp_kf")
    val tempKf: String?,
    @SerializedName("temp_max")
    val tempMax: String?,
    @SerializedName("temp_min")
    val tempMin: String?
)