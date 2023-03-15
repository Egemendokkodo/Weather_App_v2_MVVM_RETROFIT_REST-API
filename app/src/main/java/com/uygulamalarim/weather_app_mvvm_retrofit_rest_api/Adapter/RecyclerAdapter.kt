package com.uygulamalarim.weather_app_mvvm_retrofit_rest_api.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.uygulamalarim.weather_app_mvvm_retrofit_rest_api.R
import com.uygulamalarim.weather_app_mvvm_retrofit_rest_api.WeatherServices.model.RecyclerModel
import com.uygulamalarim.weather_app_mvvm_retrofit_rest_api.WeatherServices.model.WeatherModal
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class RecyclerAdapter(private val dataSet: ArrayList<RecyclerModel>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){



    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_day:TextView=view.findViewById(R.id.tv_day)
        val tv_icon:ImageView=view.findViewById(R.id.tv_icon)
        val tv_temp:TextView=view.findViewById(R.id.tv_temp)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_style, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=dataSet[position]
        holder.tv_day.text=getDay(item.datetime.toString())
        Picasso.get()
            .load("https://www.weatherbit.io/static/img/icons/" + item.icon.toString() + ".png")
            .into(holder.tv_icon)
        holder.tv_temp.text=item.temp.toString()+"°C"

    }

    override fun getItemCount() = dataSet.size

    private fun getDay(date:String):String{
        val formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd")
        val tarih = LocalDate.parse(date, formatter)
        val gun = tarih.dayOfWeek

        val gunAdi = when (gun) {
            java.time.DayOfWeek.MONDAY -> "Monday"
            java.time.DayOfWeek.TUESDAY -> "Tuesday"
            java.time.DayOfWeek.WEDNESDAY -> "Wednesday"
            java.time.DayOfWeek.THURSDAY -> "Thursday"
            java.time.DayOfWeek.FRIDAY -> "Friday"
            java.time.DayOfWeek.SATURDAY -> "Saturday"
            java.time.DayOfWeek.SUNDAY -> "Sunday"
        }
        return gunAdi
    }


}