package com.yong.taximeter.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yong.taximeter.route.main.ui.MainScreen
import com.yong.taximeter.route.meter.ui.MeterScreen

/**
 * TaxiMeter Nav Host
 * - Manage navigation for each screen
 * - Each route is defined at [TaxiMeterNavRoute] as data class
 */
@Composable
fun TaxiMeterNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = TaxiMeterNavRoute.Main,
    ) {
        // Main
        composable<TaxiMeterNavRoute.Main> {
            val navigateToMeter = {
                navController.navigate(TaxiMeterNavRoute.Meter) {
                    launchSingleTop = true
                }
            }

            MainScreenNav(
                modifier = Modifier
                    .fillMaxSize(),
                navigateToMeter = navigateToMeter,
            )
        }

        // Meter
        composable<TaxiMeterNavRoute.Meter>(
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
        ) {
            MeterScreenNav(
                modifier = Modifier
                    .fillMaxSize(),
            )
        }
    }
}

// Main Screen Nav
@Composable
private fun MainScreenNav(
    modifier: Modifier = Modifier,
    navigateToMeter: () -> Unit,
) {
    MainScreen(
        modifier = modifier,
        navigateToMeter = navigateToMeter,
    )
}

// Meter Screen Nav
@Composable
private fun MeterScreenNav(
    modifier: Modifier = Modifier,
) {
    MeterScreen(
        modifier = modifier,
    )
}