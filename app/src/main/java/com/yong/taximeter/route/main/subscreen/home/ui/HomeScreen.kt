package com.yong.taximeter.route.main.subscreen.home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yong.taximeter.R
import com.yong.taximeter.common.ui.theme.Typography
import com.yong.taximeter.route.main.subscreen.home.viewmodel.HomeViewModel

// Constant values
private val HOME_APP_LOGO_ICON_SIZE = 96.dp

/**
 * Home Screen
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToMeter: () -> Unit,
) {
    // UI State
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // SnackBar State / Effect
    val snackBarHostState = remember { SnackbarHostState() }
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

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .clickable(onClick = navigateToMeter)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // App Logo
            AppLogo(
                modifier = Modifier,
            )

            // Description Text
            DescriptionText(
                modifier = Modifier,
            )
        }
    }
}

/**
 * TaxiMeter App Logo
 */
@Composable
private fun AppLogo(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    )  {
        // Icon
        AppLogoIcon(
            modifier = Modifier,
        )

        // Text
        AppLogoText(
            modifier = Modifier,
        )
    }
}

/**
 * TaxiMeter App Logo - Icon
 */
@Composable
private fun AppLogoIcon(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier
                .size(HOME_APP_LOGO_ICON_SIZE),
            imageVector = Icons.Filled.LocalTaxi,
            contentDescription = null,
        )
    }

}

/**
 * TaxiMeter App Logo - Text
 */
@Composable
private fun AppLogoText(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        // Taxi
        Text(
            text = stringResource(R.string.home_logo_text_taxi),
            style = Typography.displayMedium
                .copy(fontWeight = FontWeight.Bold),
        )

        // Meter
        Text(
            text = stringResource(R.string.home_logo_text_meter),
            style = Typography.displayMedium,
        )
    }
}

/**
 * Description Text
 */
@Composable
private fun DescriptionText(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            stringResource(R.string.home_desc_text),
            style = Typography.headlineSmall,
        )
    }
}