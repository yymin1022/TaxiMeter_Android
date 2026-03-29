package com.yong.taximeter.route.main.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.yong.taximeter.route.main.model.TabInfo
import com.yong.taximeter.route.main.subscreen.home.ui.HomeScreen
import com.yong.taximeter.route.main.subscreen.setting.ui.SettingScreen
import com.yong.taximeter.route.main.subscreen.store.ui.StoreScreen
import com.yong.taximeter.route.main.viewmodel.MainViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource

/**
 * Main Screen
 */
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
) {
    // UI State
    val uiState = viewModel.uiState.collectAsState().value
    val selectedTabIdx = uiState.selectedTabIdx
    val tabList = uiState.tabList

    // Initialize Tab Info
    LaunchedEffect(Unit) {
        viewModel.initTabInfo()
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        bottomBar = {
            // Bottom Tab UI
            MainBottomTab(
                modifier = Modifier,
                selectedTabIdx = selectedTabIdx,
                tabList = tabList,
                onClickTab = viewModel::selectTab,
            )
        }
    ) { innerPadding ->
        // Subscreen UI
        MainSubscreen(
            modifier = Modifier
                .padding(innerPadding),
            selectedTabIdx = selectedTabIdx,
        )
    }
}

/**
 * Subscreen UI
 */
@Composable
private fun MainSubscreen(
    modifier: Modifier = Modifier,
    selectedTabIdx: Int?,
) {
    Box(
        modifier = modifier,
    ) {
        // Set subscreen ui for each tab
        when(selectedTabIdx) {
            0 -> SettingScreen()
            1 -> HomeScreen()
            2 -> StoreScreen()
            else -> {}
        }
    }
}

/**
 * Bottom Tab UI
 */
@Composable
private fun MainBottomTab(
    modifier: Modifier = Modifier,
    tabList: List<TabInfo>?,
    selectedTabIdx: Int?,
    onClickTab: (idx: Int) -> Unit,
) {
    ShortNavigationBar(
        modifier = modifier,
    ) {
        tabList?.forEachIndexed { idx, tabInfo ->
            val isSelected = selectedTabIdx == idx
            val tabIcon = tabInfo.icon
            val tabTitle = stringResource(tabInfo.titleRes)

            ShortNavigationBarItem(
                icon = { Icon(imageVector = tabIcon, contentDescription = tabTitle) },
                label = { Text(tabTitle) },
                selected = isSelected,
                onClick = { onClickTab(idx) }
            )
        }
    }
}