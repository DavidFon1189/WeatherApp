package data.api

import data.api.Endpoint.GET_WEATHER
import data.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Api {

    @GET(GET_WEATHER)
    fun get_weather(): Call<WeatherResponse>
}