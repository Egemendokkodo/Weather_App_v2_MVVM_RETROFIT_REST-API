package com.uygulamalarim.weather_app_mvvm_retrofit_rest_api.WeatherServices.model


import com.google.gson.annotations.SerializedName
import com.uygulamalarim.weather_app_mvvm_retrofit_rest_api.WeatherServices.model.Data

data class WeatherModal(
    @SerializedName("city_name")
    val cityName: String,
    @SerializedName("country_code")
    val countryCode: String,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("lat")
    val lat: String,
    @SerializedName("lon")
    val lon: String,
    @SerializedName("state_code")
    val stateCode: String,
    @SerializedName("timezone")
    val timezone: String
)