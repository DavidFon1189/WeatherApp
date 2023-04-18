package viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enginecore.app.weatherapp.R
import data.model.WeatherResponse
import domain.WeatherCU
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherViewModel: ViewModel() {

    val weatherModelSucces = MutableLiveData <Response<WeatherResponse>?>()
    val weatherModelErrorServer = MutableLiveData<Int>()
    val isLoading = MutableLiveData<Boolean>()
    val errorGeneral = MutableLiveData<String>()

    fun getWeather(lat: String, lon: String){
        isLoading.postValue(true)
        val getWeatherCU = WeatherCU(lat, lon)
        viewModelScope.launch {
            val categorias: Call<WeatherResponse>? = getWeatherCU()
            categorias?.enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    if (response.isSuccessful){
                        weatherModelSucces.postValue(response)
                        isLoading.postValue(false)
                    } else {
                        isLoading.postValue(false)
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        val errorCode = jsonObj.getString("codigo")
                        val mensaje = jsonObj.getString("mensaje")
                        errorGeneral.postValue("$errorCode $mensaje")
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    weatherModelErrorServer.postValue(R.string.error_de_comunicacion_servidor)
                    isLoading.postValue(false)
                }
            })
        }
    }
}