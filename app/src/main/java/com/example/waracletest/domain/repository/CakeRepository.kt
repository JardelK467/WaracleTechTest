package com.example.waracletest.domain.repository

import com.example.waracletest.domain.model.Cake

// TODO: Extend with observeCakes(): Flow<List<Cake>>, refresh(), and local
//  add, delete and update once a Room database backs the repository.
interface CakeRepository {
    suspend fun getCakes(): List<Cake>
}
