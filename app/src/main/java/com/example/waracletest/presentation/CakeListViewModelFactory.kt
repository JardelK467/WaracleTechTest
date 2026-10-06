package com.example.waracletest.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.waracletest.domain.usecase.GetCakesUseCase

class CakeListViewModelFactory(
    private val getCakes: GetCakesUseCase,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CakeListViewModel::class.java)) {
            return CakeListViewModel(getCakes) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
