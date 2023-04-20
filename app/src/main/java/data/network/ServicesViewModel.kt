package data.network

import data.api.RetrofitClient
import data.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call

class ServicesViewModel {

    private val retrofit = RetrofitClient.provideAPIService()

    suspend fun getWeather(lat: Double, lon: Double): Call<WeatherResponse>?{
        return withContext(Dispatchers.IO){
            val response = retrofit?.getWeather(lat, lon)
            response
        }
    }
}