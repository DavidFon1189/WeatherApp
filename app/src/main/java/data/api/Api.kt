package data.api

import data.api.Endpoint.GET_WEATHER
import data.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface Api {

    @GET(GET_WEATHER)
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Call<WeatherResponse>

//    @GET("/maps/api/geocode/json?sensor=false")
//    fun getPositionByZip(@Query("address") address: String?, cb: Callback<String?>?)
//
//    interface FooService {
//        @GET("/maps/api/geocode/json?address={zipcode}&sensor=false")
//        fun getPositionByZip(@Path("zipcode") zipcode: Int, cb: Callback<String?>?)
//    }
}