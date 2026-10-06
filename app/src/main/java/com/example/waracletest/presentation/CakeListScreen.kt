package com.example.waracletest.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.waracletest.ui.theme.WaracletestTheme

@Composable
fun CakeListScreenRoot(
    viewModel: CakeListViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    CakeListScreen(
        uiState = uiState.value,
        onRefresh = viewModel::refresh,
        modifier = modifier,
    )
}

@Composable
fun CakeListScreen(
    uiState: CakeListUiState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Render CakeListUiState and add list, refresh, and error UI in later iterations.
    Box(modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun CakesListScreenPreview() {
    WaracletestTheme {
        CakeListScreen(
            uiState = CakeListUiState.Loading,
            onRefresh = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
