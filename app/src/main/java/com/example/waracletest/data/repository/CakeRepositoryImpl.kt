package com.example.waracletest.data.repository

import com.example.waracletest.data.remote.CakeDto
import com.example.waracletest.data.remote.CakeService
import com.example.waracletest.domain.model.Cake
import com.example.waracletest.domain.repository.CakeRepository

// TODO: Add a Room database as the single source of truth. Persist the network
//  response, expose cakes as a Flow from the database, and keep showing cached
//  data when a refresh fails (for example when offline).
// TODO: Map network failures to a typed domain error if the UI needs to tell
//  offline apart from a server error.
class CakeRepositoryImpl(
    private val cakeService: CakeService,
) : CakeRepository {
    override suspend fun getCakes(): List<Cake> = cakeService.loadCakes().map(CakeDto::toDomain)
}

private fun CakeDto.toDomain(): Cake =
    Cake(
        title = title,
        description = desc,
        imageUrl = image,
    )
