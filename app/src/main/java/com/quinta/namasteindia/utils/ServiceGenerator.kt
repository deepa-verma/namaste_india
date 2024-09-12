package com.quinta.namasteindia.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceGenerator {
    private val gson : Gson by lazy {
        GsonBuilder().setLenient().create()
    }
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS) // Adjust the connect timeout
        .readTimeout(30, TimeUnit.SECONDS)    // Adjust the read timeout
        .writeTimeout(30, TimeUnit.SECONDS)   // Adjust the write timeout
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.prodUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }



}