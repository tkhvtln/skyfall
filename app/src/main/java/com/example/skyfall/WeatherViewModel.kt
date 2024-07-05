package com.example.skyfall

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WeatherViewModel: ViewModel() {
    val weatherList = MutableLiveData<List<WeatherData>>()
}