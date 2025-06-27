package com.example.lostpals.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// configurare Retrofit pentru a efectua cereri HTTP

object RetrofitClient {

    // adresa url pentru cererile HTTP
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    val api: DemoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // setare url
            .addConverterFactory(GsonConverterFactory.create()) // transforma  obiectele JSON in Kotlin
            .build()
            .create(DemoApiService::class.java) // creeaza implementarea pentru interfata noastra
    }
}
