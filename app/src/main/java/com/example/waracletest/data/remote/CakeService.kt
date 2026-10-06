package com.example.waracletest.data.remote

import retrofit2.http.GET

interface CakeService {
    @GET("cakes")
    suspend fun loadCakes(): List<CakeDto>
}
