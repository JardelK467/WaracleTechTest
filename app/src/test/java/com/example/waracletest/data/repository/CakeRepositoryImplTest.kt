package com.example.waracletest.data.repository

import com.example.waracletest.data.remote.CakeDto
import com.example.waracletest.data.remote.CakeService
import com.example.waracletest.domain.model.Cake
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.fail
import org.junit.Test

class CakeRepositoryImplTest {
    @Test
    fun `maps service DTOs to domain cakes`() =
        runBlocking {
            val service =
                FakeCakeService(
                    listOf(
                        CakeDto(
                            title = "Dundee Cake",
                            desc = "A staple with Dundonians",
                            image = "https://example.com/dundee.jpg",
                        ),
                    ),
                )
            val repository = CakeRepositoryImpl(service)

            assertEquals(
                listOf(
                    Cake(
                        title = "Dundee Cake",
                        description = "A staple with Dundonians",
                        imageUrl = "https://example.com/dundee.jpg",
                    ),
                ),
                repository.getCakes(),
            )
        }

    @Test
    fun `lets service failures propagate to the caller`() =
        runBlocking {
            val failure = IllegalStateException("Service unavailable")
            val repository = CakeRepositoryImpl(FailingCakeService(failure))

            try {
                repository.getCakes()
                fail("Expected the service failure to propagate")
            } catch (exception: IllegalStateException) {
                assertSame(failure, exception)
            }
        }
}

private class FakeCakeService(
    private val cakes: List<CakeDto>,
) : CakeService {
    override suspend fun loadCakes(): List<CakeDto> = cakes
}

private class FailingCakeService(
    private val failure: Exception,
) : CakeService {
    override suspend fun loadCakes(): List<CakeDto> = throw failure
}
