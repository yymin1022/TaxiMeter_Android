package com.yong.taximeter.route.main.subscreen.store.ui

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.yong.taximeter.route.main.subscreen.store.viewmodel.StoreUiEffect
import com.yong.taximeter.route.main.subscreen.store.viewmodel.StoreViewModel

/**
 * Store Screen
 */
@Composable
fun StoreScreen(
    modifier: Modifier = Modifier,
    viewModel: StoreViewModel = hiltViewModel(),
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