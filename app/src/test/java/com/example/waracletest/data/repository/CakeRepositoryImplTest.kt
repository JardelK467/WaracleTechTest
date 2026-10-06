package com.example.waracletest.data.repository

import com.example.waracletest.data.remote.CakeDto
import com.example.waracletest.data.remote.CakeService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertSame
import org.junit.Assert.fail
import org.junit.Test

class CakeRepositoryImplTest {
    @Test
    fun `wraps a service failure as a cake load failure`() = runBlocking {
        val serviceFailure = IllegalStateException("Service unavailable")
        val repository = CakeRepositoryImpl(FailingCakeService(serviceFailure))

        try {
            repository.getCakes()
            fail("Expected CakeLoadException")
        } catch (exception: CakeLoadException) {
            assertSame(serviceFailure, exception.cause)
        }
    }
}

private class FailingCakeService(
    private val failure: Exception,
) : CakeService {
    override suspend fun loadCakes(): List<CakeDto> = throw failure
}
