package com.example.waracletest.domain.usecase

import com.example.waracletest.domain.model.Cake
import com.example.waracletest.domain.repository.CakeRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetCakesUseCaseTest {
    @Test
    fun `returns unique cakes sorted by title`() =
        runBlocking {
            val chocolateCake = cake(title = "Chocolate Cake")
            val cakes =
                listOf(
                    chocolateCake,
                    cake(title = "Banana Cake"),
                    cake(title = "Apple Cake"),
                    chocolateCake.copy(description = "A duplicate title"),
                )
            val useCase = GetCakesUseCase(FakeCakeRepository(cakes))

            assertEquals(
                listOf("Apple Cake", "Banana Cake", "Chocolate Cake"),
                useCase().map(Cake::title),
            )
        }

    @Test
    fun `sorts titles ignoring case`() =
        runBlocking {
            val cakes = listOf(cake("zebra cake"), cake("Apple Cake"), cake("banana cake"))
            val useCase = GetCakesUseCase(FakeCakeRepository(cakes))

            assertEquals(
                listOf("Apple Cake", "banana cake", "zebra cake"),
                useCase().map(Cake::title),
            )
        }

    private fun cake(title: String) =
        Cake(
            title = title,
            description = "Description for $title",
            imageUrl = "https://example.com/${title.lowercase().replace(" ", "-")}.jpg",
        )
}

private class FakeCakeRepository(
    private val cakes: List<Cake>,
) : CakeRepository {
    override suspend fun getCakes(): List<Cake> = cakes
}
