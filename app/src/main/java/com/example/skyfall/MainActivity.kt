package com.example.skyfall

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.skyfall.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject

const val API = "36f9315375fa4756a8884746240107"
const val DEGREE_SIGN = "\u00B0"

class MainActivity : AppCompatActivity(), WeatherAdapter.ItemClickListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var weatherList: ArrayList<WeatherData>
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var url: String

    private var city: String = "Moscow"
    private var days: Int = 0
    private var selectedDay = 0;

    private val converter: Converter = Converter()
    private val animatorController = AnimatorController()

    private val weatherModel: WeatherViewModel by viewModels()

    private val backgrounds = arrayOf(
        R.drawable.gradient_background_1,
        R.drawable.gradient_background_2,
        R.drawable.gradient_background_3,
        R.drawable.gradient_background_4,
        R.drawable.gradient_background_5,
        R.drawable.gradient_background_6,
        R.drawable.gradient_background_7,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initRecyclerView()

        setCity("Sibay")
        setDays(7)

        clearWeatherData()
        requestWeather()
        updateWeather()

        animatorController.rotate(binding.ivBackground, 5000)
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

        val weatherData = WeatherData(currentTemperature, "Today", currentImageCondition, R.drawable.gradient_background_1)
        weatherList.add(weatherData)
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

            val weatherData = WeatherData(temperature, dayOfWeek, imageCondition, R.drawable.gradient_background_2)
            weatherList.add(weatherData)
        }
    }

    private fun updateWeather() {
        weatherModel.weatherList.observe(this) {
            weatherAdapter.submitList(it)

            binding.tvDayOfWeek.text = "Today"
            weatherAdapter.updateSelectedIndex(0)
        }
    }

    private fun initRecyclerView() {
        weatherAdapter = WeatherAdapter(this)
        binding.rvWeathers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvWeathers.adapter = weatherAdapter
    }

    private fun clearWeatherData() {
        binding.tvDayOfWeek.text = ""
        binding.tvTemperature.text = ""
        binding.tvDayOfWeek.text = ""
        binding.imgWeather.setImageResource(0);
    }

    override fun onItemClickListener(indexDay: Int) {
        weatherAdapter.updateSelectedIndex(indexDay)

        animatorController.move(binding.tvTemperature, weatherList[indexDay].temperature, 300)
        animatorController.move(binding.imgWeather, weatherList[indexDay].condition, 250)
        animatorController.fade(binding.tvDayOfWeek, weatherList[indexDay].dayOfWeek, 200)
        animatorController.changeBackground(binding.ivBackground, backgrounds[selectedDay], backgrounds[indexDay], 200)

        selectedDay = indexDay
    }
}