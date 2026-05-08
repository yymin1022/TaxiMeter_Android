package com.yong.taximeter.route.main.subscreen.store.ui

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yong.taximeter.R
import com.yong.taximeter.common.ui.theme.Typography
import com.yong.taximeter.route.main.subscreen.store.model.ProductItem
import com.yong.taximeter.route.main.subscreen.store.viewmodel.StoreUiEffect
import com.yong.taximeter.route.main.subscreen.store.viewmodel.StoreViewModel

/**
 * Store Screen
 */
@Composable
fun StoreScreen(
    modifier: Modifier = Modifier,
    viewModel: StoreViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState,
) {
    val context = LocalContext.current
    val activity = context as ComponentActivity

    // UI Effect Handling
    val uiEffect = viewModel.uiEffect
    LaunchedEffect(Unit) {
        uiEffect.collect { effect ->
            handleUiEffect(
                effect = effect,
                activity = activity,
                launchPurchase = viewModel::launchBillingFlow,
            )
        }
    }

    // UI State
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // SnackBar Effect
    val snackBarMessageRes = uiState.snackBarMessageRes
    snackBarMessageRes?.let {
        val message = stringResource(it)
        LaunchedEffect(message) {
            // Show Snack Bar
            snackBarHostState.showSnackbar(message)
            // Clear Snack Bar Message
            viewModel.clearSnackBar()
        }
    }

    // Store Products Load Effect
    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    // Store Screen Content
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        when {
            // Loading State
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center),
                )
            }

            // Empty State
            uiState.productItems.isEmpty() -> {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = stringResource(R.string.store_coming_soon),
                    style = Typography.bodyLarge,
                )
            }

            // Product List State
            else -> {
                StoreContent(
                    modifier = Modifier
                        .fillMaxSize(),
                    productItems = uiState.productItems,
                    selectedProductIdx = uiState.selectedProductIdx,
                    isPurchasing = uiState.isPurchasing,
                    onProductClick = viewModel::onClickProduct,
                    onPurchaseClick = viewModel::onClickPurchase,
                )
            }
        }
    }
}

/**
 * Store Content
 * - Product list with purchase button
 */
@Composable
private fun StoreContent(
    modifier: Modifier = Modifier,
    productItems: List<ProductItem>,
    selectedProductIdx: Int?,
    isPurchasing: Boolean,
    onProductClick: (Int) -> Unit,
    onPurchaseClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(16.dp),
    ) {
        // Product List
        LazyColumn(
            modifier = Modifier
                .weight(1f),
        ) {
            itemsIndexed(
                items = productItems,
                key = { idx, item -> "${idx}_${item.title}" },
            ) { index, item ->
                ProductItem(
                    item = item,
                    isSelected = selectedProductIdx == index,
                    onClick = { onProductClick(index) },
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Purchase Button
        val isPurchaseEnabled =
            (selectedProductIdx != null)
                && isPurchasing.not()
                && productItems[selectedProductIdx].isPurchased.not()
        PurchaseButton(
            isPurchasing = isPurchasing,
            isEnabled = isPurchaseEnabled,
            onClick = onPurchaseClick,
        )
    }
}

/**
 * Product Tile
 * - Radio button style selectable tile
 */
@Composable
private fun ProductItem(
    modifier: Modifier = Modifier,
    item: ProductItem,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    // Border Color
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline
    }

    // Container Color
    val containerColor = if (item.isPurchased) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.surface
    }

    // Product Info Card
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = !item.isPurchased) { onClick() },
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(
            width = if(isSelected) 2.dp else 1.dp,
            color = borderColor,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Product Info
            Column(
                modifier = Modifier
                    .weight(1f),
            ) {
                // Product Title
                Text(
                    text = item.title,
                    style = Typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Product Description
                Text(
                    text = item.desc,
                    style = Typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Price Label
            val priceLabelColor =
                if(item.isPurchased) MaterialTheme.colorScheme.onSurfaceVariant
                else MaterialTheme.colorScheme.primary
            val priceLabelText =
                if(item.isPurchased) stringResource(R.string.store_already_purchased)
                else item.formattedPrice
            Text(
                text = priceLabelText,
                color = priceLabelColor,
                style = Typography.titleSmall,
            )
        }
    }
}

/**
 * Purchase Button
 */
@Composable
private fun PurchaseButton(
    modifier: Modifier = Modifier,
    isPurchasing: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(64.dp),
        onClick = onClick,
        enabled = isEnabled,
    ) {
        if(isPurchasing) {
            // Loading Indicator
            CircularProgressIndicator(
                modifier = Modifier
                    .height(20.dp)
                    .width(20.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        } else {
            // Button Text
            Text(
                text = stringResource(R.string.store_purchase),
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}

/**
 * Handle each [StoreUiEffect]
 */
private fun handleUiEffect(
    effect: StoreUiEffect,
    activity: Activity,
    launchPurchase: (activity: Activity, productID: String) -> Unit,
) {
    when (effect) {
        // Launch Purchase flow
        is StoreUiEffect.LaunchPurchase -> {
            launchPurchase(activity, effect.productID)
        }
    }
}