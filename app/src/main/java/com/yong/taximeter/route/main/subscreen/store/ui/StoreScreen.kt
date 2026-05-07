package com.yong.taximeter.route.main.subscreen.store.ui

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

    Box(
        modifier = modifier,
    ) {
        Text("Store Screen")
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
    when(effect) {
        // Launch Purchase flow
        is StoreUiEffect.LaunchPurchase -> {
            launchPurchase(activity, effect.productID)
        }
    }
}