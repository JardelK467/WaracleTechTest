package com.example.waracletest.data.remote

interface CakeApi {
    suspend fun getCakes(): List<CakeDto>
}
