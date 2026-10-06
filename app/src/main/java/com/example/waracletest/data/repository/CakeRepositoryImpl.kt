package com.example.waracletest.data.repository

import com.example.waracletest.data.remote.CakeService
import com.example.waracletest.data.remote.CakeDto
import com.example.waracletest.domain.model.Cake
import com.example.waracletest.domain.repository.CakeRepository
import kotlinx.coroutines.CancellationException

class CakeRepositoryImpl(
    private val cakeService: CakeService,
) : CakeRepository {
    override suspend fun getCakes(): List<Cake> = try {
        cakeService.loadCakes().map(CakeDto::toDomain)
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        throw CakeLoadException(exception)
    }
}

class CakeLoadException(cause: Throwable) : RuntimeException(cause)

private fun CakeDto.toDomain(): Cake = Cake(
    title = title,
    description = desc,
    imageUrl = image,
)
