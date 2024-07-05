package com.example.skyfall

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.skyfall.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject
import com.jakewharton.threetenabp.AndroidThreeTen

const val API = "36f9315375fa4756a8884746240107"
const val DEGREE_SIGN = "\u00B0"

class MainActivity : AppCompatActivity(), WeatherAdapter.ItemClickListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var weatherList: ArrayList<WeatherData>
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var url: String

    private val converter: Converter = Converter()
    private var city: String = "Moscow"
    private var days: Int = 0

    private val weatherModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        AndroidThreeTen.init(this)

        initRecyclerView()

        setCity("Ufa")
        setDays(7)

        requestWeather()
        updateWeather()
    }

    private fun setCity(city: String) {
        this.city = city
        binding.tvCity.text = city
    }

    private fun setDays(days: Int) {
        this.days = days - 1
    }

    private fun requestWeather() {
        url = "https://api.weatherapi.com/v1/forecast.json?key=$API&q=$city&days=$days&aqi=no&alerts=no"

        val queue = Volley.newRequestQueue(this)
        val request = StringRequest(Request.Method.GET, url,
            {
                parseWeather(it)
            },
            {

            }
        )

        queue.add(request)
    }

    private fun parseWeather(it: String) {
        val obj = JSONObject(it)
        weatherList = ArrayList(days)

        parseCurrentDay(obj)
        parseNextDays(obj)

        weatherModel.weatherList.value = weatherList
    }

    private fun parseCurrentDay(obj: JSONObject) {
        val currentTemperature = obj.getJSONObject("current").getString("temp_c").split(".")[0] + DEGREE_SIGN
        val currentCondition = obj.getJSONObject("current").getJSONObject("condition").getString("code").toInt()
        val currentImageCondition = converter.getImage(currentCondition)

        binding.tvTemperature.text = currentTemperature
        Picasso.get().load(currentImageCondition).into(binding.imgWeather)

        weatherList.add(WeatherData(currentTemperature, "Today", currentImageCondition))
    }

    private fun parseNextDays(obj: JSONObject) {
        val daysArray = obj.getJSONObject("forecast").getJSONArray("forecastday")

        for (i in 0 until days) {
            val day = daysArray[i] as JSONObject

            val temperature = day.getJSONObject("day").getString("avgtemp_c").split(".")[0] + DEGREE_SIGN
            val condition = day.getJSONObject("day").getJSONObject("condition").getString("code").toInt()
            val imageCondition = converter.getImage(condition)
            val date = day.getString("date")
            val dayOfWeek = converter.getDayOfWeek(date)

            val item = WeatherData(temperature, dayOfWeek, imageCondition)

            weatherList.add(item)
        }
    }

    private fun updateWeather() {
        weatherModel.weatherList.observe(this) {
            weatherAdapter.submitList(it)
        }
    }

    private fun initRecyclerView() {
        weatherAdapter = WeatherAdapter(this)
        binding.rvWeathers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvWeathers.adapter = weatherAdapter

        weatherAdapter.updateSelectedIndex(0)
    }

    override fun onItemClickListener(indexDay: Int) {
        weatherAdapter.updateSelectedIndex(indexDay)
        binding.tvTemperature.text = weatherList[indexDay].temperature
        binding.txtDayOfWeek.text = weatherList[indexDay].dayOfWeek
        Picasso.get().load(weatherList[indexDay].condition).into(binding.imgWeather)
    }
}