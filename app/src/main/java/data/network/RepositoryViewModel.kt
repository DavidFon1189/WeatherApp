package data.network

import data.model.WeatherResponse
import retrofit2.Call

class RepositoryViewModel {

    private val api = ServicesViewModel()

    suspend fun getWeather(): Call<WeatherResponse>?{
        val response = api.getWeather()
        return response
    }
}