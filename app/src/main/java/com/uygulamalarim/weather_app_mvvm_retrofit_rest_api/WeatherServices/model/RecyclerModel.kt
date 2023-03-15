package com.uygulamalarim.weather_app_mvvm_retrofit_rest_api.WeatherServices.model

import com.google.gson.annotations.SerializedName

data class RecyclerModel(

    @SerializedName("datetime")
    val datetime: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("temp")
    val temp: String
)
