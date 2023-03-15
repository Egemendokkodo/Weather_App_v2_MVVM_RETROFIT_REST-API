package com.uygulamalarim.weather_app_mvvm_retrofit_rest_api



import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.uygulamalarim.weather_app_mvvm_retrofit_rest_api.Adapter.RecyclerAdapter
import com.uygulamalarim.weather_app_mvvm_retrofit_rest_api.WeatherServices.model.RecyclerModel
import com.uygulamalarim.weather_app_mvvm_retrofit_rest_api.WeatherServices.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {


    private lateinit var viewModel: MainViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var modelClassItemList = ArrayList<RecyclerModel>()

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        toolbar.setNavigationOnClickListener {
            getLocation()
        }

        swipeRefreshAction()

        sharedPreferences = applicationContext.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        if (isNetworkAvailable()) {
            val sehir = sharedPreferences.getString("cityName", null)

            if (sehir != null) {
                viewModel.refreshData(sehir)

            } else {
                swipeRefreshLayout.visibility= View.GONE
            }
        } else {
            swipeRefreshLayout.visibility= View.GONE
            val builder = AlertDialog.Builder(this)
            builder.setTitle("No Internet Connection")
            builder.setMessage("Please check your internet connection.")
            builder.setIcon(R.drawable.ic_baseline_wifi_off_24)
            builder.setPositiveButton("OK") { _, _ -> }
            val dialog = builder.create()
            dialog.show()
        }

        getLiveData()
        searchFunctionality()



    }

    private fun swipeRefreshAction(){

        swipeRefreshLayout.setOnRefreshListener {
            if(isNetworkAvailable()){
                getLiveData()
                swipeRefreshLayout.isRefreshing = false

            }else{
                Toast.makeText(this, "No internet connection.", Toast.LENGTH_SHORT).show()
                swipeRefreshLayout.isRefreshing = false
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun getLiveData() {
        viewModel.weather_data.observe(this) { data ->
            data?.let {

                toolbar.title = data.cityName
                toolbar.subtitle = data.countryCode

                description.text= data.data.get(0).weather.description.uppercase(Locale.getDefault())
                datetime.text=data.data.get(0).validDate
                temperature.text=data.data.get(0).temp.toString()+"°C"
                feelslike.text="Feels like "+data.data.get(0).appMaxTemp.toString()+"°C"

                windspeed.text=data.data.get(0).windSpd.toString()+"Km/h"
                humidity.text=data.data.get(0).rh.toString()+"%"
                visibility.text=data.data.get(0).vis.toString()+"%"

                precipitation.text=data.data.get(0).pop.toString()+"%"
                maxtemp.text=data.data.get(0).maxTemp.toString()+"°C"
                mintemp.text=data.data.get(0).minTemp.toString()+"°C"

                Glide.with(this)
                    .load("https://www.weatherbit.io/static/img/icons/" + data.data.get(0).weather.icon + ".png")
                    .into(weatherPic)

                GlobalScope.launch {
                    for (i in 1..5){
                        modelClassItemList.add(RecyclerModel(data.data.get(i).datetime,data.data.get(i).weather.icon,data.data.get(i).temp.toString()))
                    }
                    modelClassItemList.add(RecyclerModel(data.data.get(0).datetime,data.data.get(0).weather.icon,data.data.get(0).temp.toString()))

                    runOnUiThread{
                        val adapter = RecyclerAdapter(modelClassItemList)
                        val recyclerView = findViewById<RecyclerView>(R.id.recyclerdays)
                        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
                        recyclerView.adapter = adapter
                        swipeRefreshLayout.visibility=View.VISIBLE
                    }

                }

            }
        }
    }
    private fun searchFunctionality(){
        searchview.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchview.clearFocus()
                viewModel.refreshData(query!!)
                getLiveData()
                searchview.isIconified = true
                editor.putString("cityName", query)
                editor.apply()



                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }
    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val list: List<Address> =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1) as List<Address>

                        toolbar.setTitle(list[0].countryName)
                        toolbar.setSubtitle(list[0].locality)
                        getLiveData()
                        viewModel.refreshData(list[0].locality)
                        editor.putString("cityName", list[0].locality)
                        editor.apply()



                    }
                }
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

}