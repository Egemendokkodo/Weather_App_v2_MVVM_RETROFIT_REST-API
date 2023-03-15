package com.uygulamalarim.weather_app_mvvm_retrofit_rest_api.WeatherServices.service

import com.uygulamalarim.weather_app_mvvm_retrofit_rest_api.WeatherServices.model.WeatherModal
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherApiService {
    private val BASE_URL="https://api.weatherbit.io/v2.0/"
    private val api= Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(WeatherApi::class.java)
    fun getDataService(cityName:String): Single<WeatherModal> {
        return api.getData(cityName)
    }
}