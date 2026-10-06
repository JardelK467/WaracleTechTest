package com.example.waracletest.data.repository

import com.example.waracletest.data.remote.CakeApi
import com.example.waracletest.domain.model.Cake
import com.example.waracletest.domain.repository.CakeRepository

class CakeRepositoryImpl(
    private val cakeApi: CakeApi,
) : CakeRepository {
    override suspend fun getCakes(): List<Cake> {
        // TODO: Fetch DTOs from CakeApi and map them to domain Cakes.
        throw UnsupportedOperationException("Cake fetching has not been implemented yet.")
    }
}
