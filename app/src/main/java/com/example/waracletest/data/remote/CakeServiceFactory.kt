package com.example.waracletest.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object CakeServiceFactory {
    private const val defaultBaseUrl =
        "https://raw.githubusercontent.com/Waracle/mobile-coding-test-api/refs/heads/main/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    fun create(baseUrl: String = defaultBaseUrl): CakeService {
        // TODO: Externalise the base URL through build configuration if multiple environments are added.
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

        return retrofit.create(CakeService::class.java)
    }
}
