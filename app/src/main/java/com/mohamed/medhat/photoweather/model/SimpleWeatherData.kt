package com.mohamed.medhat.photoweather.model

import com.google.gson.annotations.SerializedName

data class SimpleWeatherData(

	@field:SerializedName("main")
	val main: Main,

	@field:SerializedName("sys")
	val sys: Sys,

	@field:SerializedName("weather")
	val weather: List<WeatherItem>,

	@field:SerializedName("name")
	val name: String
)

data class Main(
	@field:SerializedName("temp")
	val temp: Double
)



data class WeatherItem(
	@field:SerializedName("description")
	val description: String
)

data class Sys(
	@field:SerializedName("country")
	val country: String
)
