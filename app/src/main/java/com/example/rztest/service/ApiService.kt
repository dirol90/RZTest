package com.example.rztest.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiService {

    companion object {
        private val retrofit = Retrofit.Builder()
            .baseUrl("https://api.unsplash.com/")
            .client(generateHttpClient().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fun <S> createService(serviceClass: Class<S>?): S {
            return retrofit.create(serviceClass)
        }

        private fun generateHttpClient(): OkHttpClient.Builder {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)
            return httpClient
        }
    }
}