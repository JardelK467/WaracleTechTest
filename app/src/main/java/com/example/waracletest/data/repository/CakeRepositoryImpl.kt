package com.example.waracletest.data.repository

import com.example.waracletest.data.remote.CakeService
import com.example.waracletest.domain.model.Cake
import com.example.waracletest.domain.repository.CakeRepository

class CakeRepositoryImpl(
    private val cakeService: CakeService,
) : CakeRepository {
    override suspend fun getCakes(): List<Cake> {
        // TODO: Fetch DTOs from CakeService and map them to domain Cakes.
        throw UnsupportedOperationException("Cake fetching has not been implemented yet.")
    }
}
