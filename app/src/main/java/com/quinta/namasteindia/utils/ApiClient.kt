package com.quinta.namasteindia.utils

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private val okClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS) // Adjust the connect timeout
        .readTimeout(30, TimeUnit.SECONDS)    // Adjust the read timeout
        .writeTimeout(30, TimeUnit.SECONDS)   // Adjust the write timeout
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://172.18.1.47:443")
        .addConverterFactory(GsonConverterFactory.create())
        .client(ApiClient.okClient)
        .build()

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }

}