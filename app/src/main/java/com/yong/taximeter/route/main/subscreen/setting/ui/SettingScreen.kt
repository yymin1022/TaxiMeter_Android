package com.yong.taximeter.route.main.subscreen.setting.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yong.taximeter.common.ui.dialog.CustomCostInputDialog
import com.yong.taximeter.common.ui.dialog.RadioSelectDialog
import com.yong.taximeter.common.ui.theme.Typography
import com.yong.taximeter.route.main.subscreen.setting.model.SettingItemGroup
import com.yong.taximeter.route.main.subscreen.setting.viewmodel.SettingViewModel
import com.yong.taximeter.route.main.subscreen.setting.viewmodel.ShowDialog

/**
 * Setting Screen
 */
@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel = hiltViewModel(),
) {
    // UI State
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val settingGroups = uiState.settingGroups
    val showDialog = uiState.showDialog

    // Setting Groups Load Effect
    LaunchedEffect(Unit) {
        viewModel.loadSettingGroups()
    }

    // Show Dialog if enabled
    when(showDialog) {
        // Custom Cost Input Dialog
        is ShowDialog.CustomCostDialog -> {
            CustomCostInputDialog(
                titleRes = showDialog.titleRes,
                onConfirm = showDialog.onComplete,
                onDismiss = viewModel::dismissDialog,
            )
        }

        // Radio Selection Dialog
        is ShowDialog.RadioSelectDialog -> {
            RadioSelectDialog(
                titleRes = showDialog.titleRes,
                radioItemTextResources = showDialog.itemTextResources,
                onComplete = showDialog.onComplete,
                onDismiss = viewModel::dismissDialog,
            )
        }

        // Nothing
        is ShowDialog.Nothing -> {}
    }

    // Setting Groups List UI
    LazyColumn(
        modifier = modifier,
    ) {
        settingGroups?.let {
            items(it.size) { idx ->
                val settingGroup = it[idx]
                SettingGroup(
                    modifier = Modifier,
                    group = settingGroup,
                )
            }
        }
    }
}

/**
 * Setting Group
 */
@Composable
private fun SettingGroup(
    modifier: Modifier = Modifier,
    group: SettingItemGroup,
) {
    val titleText = group.titleRes?.let { stringResource(it) }
    val itemList = group.items ?: emptyList()

    // Setting Group UI
    Column(
        modifier = modifier
            .padding(bottom = 16.dp),
    ) {
        // Group Title Text
        titleText?.let {
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                text = it,
                style = Typography.titleMedium.copy(
                    color = Color.Gray,
                ),
            )
        }

        // Setting Items
        itemList.forEach { item ->
            // Title / Subtitle Text
            val itemTitleText = item.titleRes
                ?.let { stringResource(it) }
            val itemSubtitleText = item.subtitleRes
                ?.let { stringResource(it) }
                ?: item.subtitle
            // Enabled flag
            val isEnabled = item.isEnabled

            // Callback Units
            val onClickItem = item.onClick ?: {}

            // Setting Item UI
            SettingItemInternal(
                modifier = modifier,
                titleText = itemTitleText,
                subtitleText = itemSubtitleText,
                isEnabled = isEnabled,
                onClickItem = onClickItem,
            )
        }
    }
}

/**
 * Setting Item Internal
 */
@Composable
private fun SettingItemInternal(
    modifier: Modifier = Modifier,
    titleText: String?,
    subtitleText: String?,
    isEnabled: Boolean,
    onClickItem: () -> Unit,
) {
    val titleTextStyle = if(isEnabled) {
        Typography.titleMedium
    } else {
        Typography.titleMedium.copy(
            color = Color.LightGray
        )
    }

    val subtitleTextStyle = if(isEnabled) {
        Typography.titleSmall.copy(
            color = Color.DarkGray,
        )
    } else {
        Typography.titleSmall.copy(
            color = Color.LightGray,
        )
    }

    // On Click
    val onClick = { if(isEnabled) onClickItem() }

    // Setting Item UI
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        // Setting Item Title Text
        titleText?.let {
            Text(
                text = it,
                style = titleTextStyle,
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Setting Item Subtitle Text
        subtitleText?.let {
            Text(
                text = it,
                style = subtitleTextStyle,
            )
        }
    }
}