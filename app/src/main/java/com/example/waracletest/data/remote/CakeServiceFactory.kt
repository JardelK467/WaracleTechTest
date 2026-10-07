package com.example.waracletest.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

// TODO: Replace this factory and the manual wiring in MainActivity with Hilt or Koin
//  once there is more than one screen. Also externalise the base URL through build
//  configuration if multiple environments are added.
object CakeServiceFactory {
    private const val BASE_URL =
        "https://raw.githubusercontent.com/Waracle/mobile-coding-test-api/refs/heads/main/"

    private val json =
        Json {
            ignoreUnknownKeys = true
        }

    fun create(baseUrl: String = BASE_URL): CakeService {
        val retrofit =
            Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()

        return retrofit.create(CakeService::class.java)
    }
}
