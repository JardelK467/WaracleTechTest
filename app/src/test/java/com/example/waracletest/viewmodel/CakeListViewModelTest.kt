package com.example.waracletest.presentation

import com.example.waracletest.domain.model.Cake
import com.example.waracletest.domain.repository.CakeRepository
import com.example.waracletest.domain.usecase.GetCakesUseCase
import kotlinx.coroutines.CompletableDeferred
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CakeListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val apple = cake("Apple Cake")
    private val banana = cake("Banana Cake")

    @Test
    fun `when load succeeds cakes are presented without duplicates and sorted`() {
        val repository = FakeCakeRepository {
            listOf(banana, apple, banana.copy(description = "A duplicate title"))
        }

        val viewModel = viewModelWith(repository)

        assertEquals(
            CakeListUiState.Success(listOf(apple, banana)),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `when load fails an error is presented`() {
        val repository = FakeCakeRepository { throw IllegalStateException("No network") }

        val viewModel = viewModelWith(repository)

        assertEquals(
            CakeListUiState.Error("Unable to load cakes."),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `refresh keeps the current list visible until the reload finishes`() {
        val reload = CompletableDeferred<List<Cake>>()
        val repository = FakeCakeRepository.firstLoadThen(listOf(apple)) { reload.await() }
        val viewModel = viewModelWith(repository)

        viewModel.refresh()

        assertTrue(viewModel.isRefreshing.value)
        assertEquals(CakeListUiState.Success(listOf(apple)), viewModel.uiState.value)

        reload.complete(listOf(banana))

        assertFalse(viewModel.isRefreshing.value)
        assertEquals(CakeListUiState.Success(listOf(banana)), viewModel.uiState.value)
    }

    @Test
    fun `refresh is ignored while a refresh is already running`() {
        val reload = CompletableDeferred<List<Cake>>()
        val repository = FakeCakeRepository.firstLoadThen(listOf(apple)) { reload.await() }
        val viewModel = viewModelWith(repository)

        viewModel.refresh()
        viewModel.refresh()

        // One call for the initial load and one for the single accepted refresh.
        assertEquals(2, repository.callCount)
    }

    private fun viewModelWith(repository: CakeRepository) =
        CakeListViewModel(GetCakesUseCase(repository))

    private fun cake(title: String) = Cake(
        title = title,
        description = "Description for $title",
        imageUrl = "https://example.com/${title.lowercase().replace(" ", "-")}.jpg",
    )
}

private class FakeCakeRepository(
    private val onGetCakes: suspend () -> List<Cake>,
) : CakeRepository {
    var callCount = 0
        private set

    override suspend fun getCakes(): List<Cake> {
        callCount++
        return onGetCakes()
    }

    companion object {
        /** Answers the first call immediately, then hands every later call to [laterCalls]. */
        fun firstLoadThen(
            firstLoad: List<Cake>,
            laterCalls: suspend () -> List<Cake>,
        ): FakeCakeRepository {
            var isFirstCall = true
            return FakeCakeRepository {
                if (isFirstCall) {
                    isFirstCall = false
                    firstLoad
                } else {
                    laterCalls()
                }
            }
        }
    }
}