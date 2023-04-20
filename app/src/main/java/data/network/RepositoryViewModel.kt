package data.network

import data.model.WeatherResponse
import retrofit2.Call

class RepositoryViewModel {

    private val api = ServicesViewModel()

    suspend fun getWeather(lat: Double, lon: Double): Call<WeatherResponse>?{
        val response = api.getWeather(lat, lon)
        return response
    }
}