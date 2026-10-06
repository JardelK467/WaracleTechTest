package com.example.waracletest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.waracletest.data.repository.CakeRepositoryImpl
import com.example.waracletest.data.remote.CakeServiceFactory
import com.example.waracletest.domain.usecase.GetCakesUseCase
import com.example.waracletest.presentation.CakeListScreenRoot
import com.example.waracletest.presentation.CakeListViewModel
import com.example.waracletest.presentation.CakeListViewModelFactory
import com.example.waracletest.ui.theme.WaracletestTheme

class MainActivity : ComponentActivity() {
    private val cakeListViewModel: CakeListViewModel by viewModels {
        CakeListViewModelFactory(
            getCakes = GetCakesUseCase(
                cakeRepository = CakeRepositoryImpl(
                    cakeService = CakeServiceFactory.create(),
                ),
            ),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WaracletestTheme {
                CakeListScreenRoot(
                    viewModel = cakeListViewModel,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
