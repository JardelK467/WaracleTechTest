package com.example.waracletest.domain.repository

import com.example.waracletest.domain.model.Cake

interface CakeRepository {
    suspend fun getCakes(): List<Cake>
}
