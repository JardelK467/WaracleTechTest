package com.example.waracletest.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waracletest.domain.usecase.GetCakesUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CakeListViewModel(
    private val getCakes: GetCakesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<CakeListUiState>(CakeListUiState.Loading)
    val uiState: StateFlow<CakeListUiState> = _uiState.asStateFlow()

    fun loadCakes() {
        viewModelScope.launch {
            _uiState.value = CakeListUiState.Loading

            _uiState.value = try {
                CakeListUiState.Success(getCakes())
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                CakeListUiState.Error(message = "Unable to load cakes.")
            }
        }
    }

    fun refresh() = loadCakes()
}
