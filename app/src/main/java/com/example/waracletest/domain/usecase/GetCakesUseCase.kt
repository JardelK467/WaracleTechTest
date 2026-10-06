package com.example.waracletest.domain.usecase

import com.example.waracletest.domain.model.Cake
import com.example.waracletest.domain.repository.CakeRepository

class GetCakesUseCase(
    private val cakeRepository: CakeRepository,
) {
    suspend operator fun invoke(): List<Cake> {
        // TODO: Remove duplicate cakes and sort by title when the data flow is implemented.
        return cakeRepository.getCakes()
    }
}
