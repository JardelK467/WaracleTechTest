package com.example.waracletest.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waracletest.domain.model.Cake
import com.example.waracletest.domain.usecase.GetCakesUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// TODO: If the screen gains actions such as add or delete, replace this sealed
//  interface with a single UiState data class so loading, refreshing and
//  pending actions can be tracked together.
sealed interface CakeListUiState {
    data object Loading : CakeListUiState

    data class Success(
        val cakes: List<Cake>,
    ) : CakeListUiState

    data class Error(
        val message: String,
    ) : CakeListUiState
}

class CakeListViewModel(
    private val getCakes: GetCakesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<CakeListUiState>(CakeListUiState.Loading)
    val uiState: StateFlow<CakeListUiState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        loadCakes()
    }

    private fun loadCakes() {
        viewModelScope.launch {
            _uiState.value = CakeListUiState.Loading
            _uiState.value = fetchCakes()
        }
    }

    fun refresh() {
        if (_isRefreshing.value) return

        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                _uiState.value = fetchCakes()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private suspend fun fetchCakes(): CakeListUiState =
        try {
            CakeListUiState.Success(getCakes())
        } catch (exception: CancellationException) {
            throw exception
        } catch (_: Exception) {
            CakeListUiState.Error(message = "Unable to load cakes.")
        }
}
