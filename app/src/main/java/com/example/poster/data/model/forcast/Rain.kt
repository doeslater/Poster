package com.example.poster.data.model.forcast


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("3h")
    val h: String?
)