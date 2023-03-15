package com.uygulamalarim.weather_app_mvvm_retrofit_rest_api.WeatherServices.service

import com.uygulamalarim.weather_app_mvvm_retrofit_rest_api.WeatherServices.model.WeatherModal
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("forecast/daily?key=b83a1e04cf2544da9775d69d35cdc647")
    fun getData(
        @Query("city") cityName:String
    ): Single<WeatherModal>


}