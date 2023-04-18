package com.enginecore.app.weatherapp

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.enginecore.app.weatherapp.databinding.ActivityMainBinding
import data.model.WeatherResponse
import viewmodel.WeatherViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val weatherViewModel: WeatherViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initVieModel()
        weatherViewModel.getWeather("44.34", "10.99")

    }

    private fun initVieModel() {
        viewModelWeather()
        isLoading()
        onErrorServer()
        onError()
    }

    private fun viewModelWeather() {
        weatherViewModel.weatherModelSucces.observe(this, Observer {
            if (it != null) {
                val getWeather: WeatherResponse? = it.body()
                if (getWeather?.cod == 200) {
                    //mostrar la info en la pantalla, si da tiempo
                    Log.d("TAG", getWeather.name)
                } else {
                    Toast.makeText(this, " no encontradas", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, " no encontradas", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun isLoading() {
        weatherViewModel.isLoading.observe(this, Observer {
//            progressbar(it)
        })
    }

    private fun onErrorServer() {
        weatherViewModel.weatherModelErrorServer.observe(this, Observer {
            showPopupDialogError("Error de comunicación con el servidor")
        })
    }

    private fun onError() {
        weatherViewModel.errorGeneral.observe(this, Observer {
            showPopupDialogError(it)
        })
    }

    private fun showPopupDialogError(subtitulo: String){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_fragment)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window?.attributes?.windowAnimations = R.style.dialogAnimation
        val tvSubtituloDialog = dialog.findViewById<TextView>(R.id.tv_error)
        tvSubtituloDialog.text = subtitulo
        dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialog.show()
    }
}