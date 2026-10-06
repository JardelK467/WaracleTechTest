package com.example.waracletest.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.waracletest.domain.model.Cake
import com.example.waracletest.ui.theme.WaracletestTheme

@Composable
fun CakeListScreenRoot(
    viewModel: CakeListViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    CakeListScreen(
        uiState = uiState,
        isRefreshing = isRefreshing,
        onRefresh = viewModel::refresh,
        modifier = modifier,
    )
}

@Composable
fun CakeListScreen(
    uiState: CakeListUiState,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CakeListContent(
        uiState = uiState,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier
    )
}

object CakeListPreviewData {
    val cakes = listOf(
        Cake(
            title = "Dundee Cake",
            description = "A staple with Dundonians",
            imageUrl = "https://example.com/dundee.jpg",
        ),
        Cake(
            title = "Cheesecake",
            description = "A classic Italian dessert",
            imageUrl = "https://example.com/cheesecake.jpg",
        ),
        Cake(
            title = "Tiramisu",
            description = "A popular Italian dessert",
            imageUrl = "https://example.com/tiramisu.jpg",
        ),
        Cake(
            title = "Apple Pie",
            description = "A popular American dessert",
            imageUrl = "https://example.com/applepie.jpg",
        ),
        Cake(
            title = "Carrot Cake",
            description = "A popular American dessert",
            imageUrl = "https://example.com/carrotcake.jpg",
        ),
    )
}

@Preview(name = "Phone", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun CakeListPhonePreview() {
    WaracletestTheme {
        CakeListScreen(
            uiState = CakeListUiState.Success(CakeListPreviewData.cakes),
            isRefreshing = false,
            onRefresh = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(name = "Tablet", showBackground = true, widthDp = 1000, heightDp = 720)
@Composable
fun CakeListTabletPreview() {
    WaracletestTheme {
        CakeListScreen(
            uiState = CakeListUiState.Success(CakeListPreviewData.cakes),
            isRefreshing = false,
            onRefresh = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(name = "Load error", showBackground = true)
@Composable
fun CakeListErrorPreview() {
    WaracletestTheme {
        CakeListScreen(
            uiState = CakeListUiState.Error("Unable to load cakes."),
            isRefreshing = false,
            onRefresh = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
