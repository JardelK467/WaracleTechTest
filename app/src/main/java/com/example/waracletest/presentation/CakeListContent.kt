package com.example.waracletest.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.waracletest.domain.model.Cake
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

private val ContentMaxWidth = 1_200.dp
private val ImageSize = 76.dp
private val ItemPadding = 12.dp
private val ImageToTextSpacing = 16.dp

// Divider starts where the text starts, so it lines up under the titles.
private val DividerStartPadding = ItemPadding + ImageSize + ImageToTextSpacing

// Entrance animation: each item falls down a little while fading in.
private const val ItemAnimationMillis = 350
private const val StaggerDelayMillis = 60L
private const val MaxStaggeredItems = 8
private val ItemFallDistance = 24.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CakeListContent(
    uiState: CakeListUiState,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { CakeTopBar() },
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding()),
        ) {
            AnimatedContent(
                targetState = uiState,
                label = "cake list state",
                modifier = Modifier.fillMaxSize(),
            ) { state ->
                when (state) {
                    CakeListUiState.Loading -> {
                        CakeLoadingContent()
                    }

                    is CakeListUiState.Error -> {
                        CakeErrorContent(
                            message = state.message,
                            onRetry = onRefresh,
                        )
                    }

                    is CakeListUiState.Success -> {
                        CakeSuccessContent(
                            cakes = state.cakes,
                            bottomInset = innerPadding.calculateBottomPadding(),
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CakeTopBar() {
    TopAppBar(
        title = {
            Column {
                Text("Cake list")
                Text(
                    text = "A selection of classic bakes",
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        },
    )
}

@Composable
private fun CakeLoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun CakeErrorContent(
    message: String,
    onRetry: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Couldn’t load cakes",
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = onRetry) {
            Text("Try again")
        }
    }
}

@Composable
private fun CakeSuccessContent(
    cakes: List<Cake>,
    bottomInset: Dp,
) {
    var selectedTitle by rememberSaveable { mutableStateOf<String?>(null) }
    val selectedCake = cakes.firstOrNull { it.title == selectedTitle }

    if (cakes.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text("No cakes are available.")
        }
        return
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        CakeList(
            cakes = cakes,
            bottomInset = bottomInset,
            onCakeSelected = { cake -> selectedTitle = cake.title },
            modifier =
                Modifier
                    .widthIn(max = ContentMaxWidth)
                    .fillMaxHeight(),
        )
    }

    if (selectedCake != null) {
        CakeDescriptionDialog(
            cake = selectedCake,
            onDismiss = { selectedTitle = null },
        )
    }
}

@Composable
private fun CakeList(
    cakes: List<Cake>,
    bottomInset: Dp,
    onCakeSelected: (Cake) -> Unit,
    modifier: Modifier = Modifier,
) {
    val animatedTitles = remember { mutableSetOf<String>() }

    LazyColumn(
        modifier = modifier,
        contentPadding =
            PaddingValues(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 16.dp + bottomInset,
            ),
    ) {
        itemsIndexed(
            items = cakes,
            key = { _, cake -> cake.title },
        ) { index, cake ->
            FallDownFadeIn(
                key = cake.title,
                index = index,
                animatedKeys = animatedTitles,
            ) {
                Column {
                    CakeListItem(
                        cake = cake,
                        onClick = { onCakeSelected(cake) },
                    )

                    if (index < cakes.lastIndex) {
                        HorizontalDivider(modifier = Modifier.padding(start = DividerStartPadding))
                    }
                }
            }
        }
    }
}

@Composable
private fun FallDownFadeIn(
    key: String,
    index: Int,
    animatedKeys: MutableSet<String>,
    content: @Composable () -> Unit,
) {
    val alreadyAnimated = remember { key in animatedKeys }
    val progress = remember { Animatable(if (alreadyAnimated) 1f else 0f) }

    LaunchedEffect(Unit) {
        if (alreadyAnimated) return@LaunchedEffect

        animatedKeys += key
        val startDelay = if (index < MaxStaggeredItems) index * StaggerDelayMillis else 0L
        delay(startDelay.milliseconds)
        progress.animateTo(targetValue = 1f, animationSpec = tween(ItemAnimationMillis))
    }

    Box(
        modifier =
            Modifier.graphicsLayer {
                alpha = progress.value
                translationY = -ItemFallDistance.toPx() * (1f - progress.value)
            },
    ) {
        content()
    }
}

@Composable
private fun CakeListItem(
    cake: Cake,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
    ) {
        Row(
            modifier = Modifier.padding(ItemPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ImageToTextSpacing),
        ) {
            CakeImage(
                cake = cake,
                modifier = Modifier.size(ImageSize),
            )
            Text(
                text = cake.title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun CakeDescriptionDialog(
    cake: Cake,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(cake.title) },
        text = { Text(cake.description) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
    )
}

@Composable
private fun CakeImage(
    cake: Cake,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center,
    ) {
        // Placeholder initial, visible until the image loads.
        Text(
            text = cake.title.take(1),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        AsyncImage(
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(cake.imageUrl)
                    .crossfade(true)
                    .build(),
            contentDescription = "Image of ${cake.title}",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
