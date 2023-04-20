package com.enginecore.app.weatherapp

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.enginecore.app.weatherapp.databinding.ActivityMainBinding
import data.model.WeatherResponse
import utils.DialogManager
import viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val REQUEST_CHECK_SETTINGS = 100
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            if (permissions.containsValue(false)) {
                    Toast.makeText(this, "Necesitar permisos de ubicacion", Toast.LENGTH_SHORT).show()
            } else {
                if (!isLocationEnabled()) {
                    showPopupDialogError("Tienes deshabilitada la ubicacion en tu dispositivo, por favor enciendela.")
                } else {
                    weatherViewModel.getWeather(19.428398, -99.165612)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initVieModel()
//        if (!isLocationEnabled()) {
//            Toast.makeText(this,"Tienes deshabilitada la ubicacion en tu dispositivo, por favor enciendela.", Toast.LENGTH_SHORT).show()
//            val intent = Intent(ACTION_LOCATION_SOURCE_SETTINGS)
//            startActivity(intent)
//        } else {
//            weatherViewModel.getWeather("44.34", "10.99")
//        }

    }

    private fun initVieModel() {
        viewModelWeather()
        isLoading()
        onErrorServer()
        onError()
    }

    override fun onResume() {
        super.onResume()
        requestLocationPermission()
//        weatherViewModel.getWeather("44.34", "10.99")
    }

    private fun viewModelWeather() {
        weatherViewModel.weatherModelSucces.observe(this, Observer {
            if (it != null) {
                val getWeather: WeatherResponse? = it.body()
                if (getWeather?.cod == 200) {
                    //mostrar la info en la pantalla, si da tiempo
                    binding.tvWeatherCondition.text = getWeather.weather[0].main
                    binding.tvDegreeCondition.text = getWeather.main.temp.toString() + "%"
                    binding.tvTempMax.text = "Temp Max " + getWeather.main.temp_max.toString()
                    binding.tvTempMin.text = "Temp Min " + getWeather.main.temp_min.toString()
                    binding.tvMilesHour.text = getWeather.wind.speed.toString() + " miles/hour"
                    binding.tvSunrise.text = getDateTime(getWeather.sys.sunrise.toString())
                    binding.tvSunset.text = getDateTime(getWeather.sys.sunset.toString())
                    binding.tvName.text = getWeather.name

                    Log.d("TAG", getDateTime(getWeather.sys.sunrise.toString()).toString())
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
            progressbar(it)
        })
    }

    private fun getDateTime(s: String): String? {
        return try {
            val sdf = SimpleDateFormat("dd MMMM yyyy, HH:mm:ss")
            val netDate = Date(s.toLong() * 1000)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
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

    private fun progressbar(isLoading: Boolean) {
        if (isLoading) {
            DialogManager.progressBar(this)
        } else {
            DialogManager.progressBar(this).dismissAllowingStateLoss()
        }
    }

    private fun showPopupDialogError(mensaje: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_fragment)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window?.attributes?.windowAnimations = R.style.dialogAnimation
        val tvMensaje = dialog.findViewById<TextView>(R.id.tv_error_mensaje)
        val btnAceptar = dialog.findViewById<TextView>(R.id.btn_aceptar)
        val btnCancelar = dialog.findViewById<TextView>(R.id.btn_cancelar)
        tvMensaje.text = mensaje
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        btnAceptar.setOnClickListener {
            val intent = Intent(ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun requestLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                //Permisos aceptados
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) && ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
                Log.i("TAG", "Show permissions dialog")
            }
            else -> {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(
            LocationManager.GPS_PROVIDER
        ) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}