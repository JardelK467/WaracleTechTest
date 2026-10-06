package com.example.waracletest.presentation

import com.example.waracletest.domain.model.Cake

sealed interface CakeListUiState {
    data object Loading : CakeListUiState

    data class Success(
        val cakes: List<Cake>,
    ) : CakeListUiState

    data class Error(
        val message: String,
    ) : CakeListUiState
}
