package domain

import data.model.WeatherResponse
import data.network.RepositoryViewModel
import retrofit2.Call

class WeatherCU(
    private val lat: String,
    private val lon: String
) {
        private val repositoryViewModel = RepositoryViewModel()
        suspend operator fun invoke(): Call<WeatherResponse>?{
            return repositoryViewModel.getWeather()
        }
}