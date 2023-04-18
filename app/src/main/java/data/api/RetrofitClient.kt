package data.api

import android.util.Base64
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://api.openweathermap.org/"
    private const val API_TIMER = 60

    fun provideAPIService(): Api? {
        return provideRetrofit(BASE_URL).create(Api::class.java)
    }

    private fun provideRetrofit(baseUrl: String): Retrofit {
        val headerAuthorizationInterceptor: Interceptor = object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                var request: Request = chain.request()
                val headers: Headers =
                    request.headers.newBuilder().build()
                request = request.newBuilder().headers(headers).build()
                return chain.proceed(request)
            }
        }
        val builder = Retrofit.Builder()
        val httpClient = OkHttpClient.Builder()
        val clientTimer =
            httpClient.connectTimeout(API_TIMER.toLong(), TimeUnit.SECONDS)
                .readTimeout(API_TIMER.toLong(), TimeUnit.SECONDS)
                .writeTimeout(API_TIMER.toLong(), TimeUnit.SECONDS)
                .addInterceptor(headerAuthorizationInterceptor)
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
        return builder.client(clientTimer)
            .client(clientTimer)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}