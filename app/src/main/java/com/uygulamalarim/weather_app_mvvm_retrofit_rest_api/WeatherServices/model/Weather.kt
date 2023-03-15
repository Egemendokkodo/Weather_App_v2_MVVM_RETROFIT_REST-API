package com.uygulamalarim.weather_app_mvvm_retrofit_rest_api.WeatherServices.model


import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("code")
    val code: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String
)