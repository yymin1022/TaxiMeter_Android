package com.yong.taximeter.route.main.subscreen.history.ui

import android.content.ClipData
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yong.taximeter.R
import com.yong.taximeter.common.ui.dialog.SimpleDialog
import com.yong.taximeter.common.ui.theme.Typography
import com.yong.taximeter.domain.model.MeterHistory
import com.yong.taximeter.route.main.subscreen.history.viewmodel.HistoryViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.launch

/**
 * History Screen
 */
@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()
    val copyTemplate = stringResource(R.string.history_clipboard_copy_format)
    val copiedMessage = stringResource(R.string.history_snack_bar_copied)

    val onCopyHistory: (MeterHistory) -> Unit = { history ->
        val formattedCost = NumberFormat.getNumberInstance().format(history.cost)
        val totalSeconds = history.elapsedSeconds.toInt()
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val formattedDate = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault()).format(Date(history.timestamp))

        val copyText = String.format(
            Locale.getDefault(),
            copyTemplate,
            history.distanceMeters / 1000f,
            formattedCost,
            minutes,
            seconds,
            formattedDate,
        )

        coroutineScope.launch {
            val clipEntry = ClipData.newPlainText("history", copyText).toClipEntry()
            clipboard.setClipEntry(clipEntry)
            snackBarHostState.showSnackbar(copiedMessage)
        }
    }

    if (uiState.showClearAllConfirm) {
        SimpleDialog(
            titleRes = R.string.history_dialog_clear_all_title,
            descRes = R.string.history_dialog_clear_all_desc,
            onConfirm = viewModel::deleteAllHistories,
            onDismiss = viewModel::hideClearAllConfirm,
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            uiState.histories.isEmpty() -> {
                HistoryEmptyView(
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            else -> {
                HistoryContent(
                    modifier = Modifier.fillMaxSize(),
                    histories = uiState.histories,
                    totalCount = uiState.totalCount,
                    totalCost = uiState.totalCost,
                    totalDistanceMeters = uiState.totalDistanceMeters,
                    onDeleteHistory = viewModel::deleteHistory,
                    onClearAllClick = viewModel::showClearAllConfirm,
                    onCopyHistory = onCopyHistory,
                )
            }
        }
    }
}

/**
 * Empty View when no histories recorded
 */
@Composable
private fun HistoryEmptyView(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            modifier = Modifier.size(64.dp),
            imageVector = Icons.Outlined.History,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.history_empty),
            style = Typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
        )
    }
}

/**
 * Content displaying summary card and list of histories (matching StoreContent layout and spacing)
 */
@Composable
private fun HistoryContent(
    modifier: Modifier = Modifier,
    histories: List<MeterHistory>,
    totalCount: Int,
    totalCost: Long,
    totalDistanceMeters: Double,
    onDeleteHistory: (Long) -> Unit,
    onClearAllClick: () -> Unit,
    onCopyHistory: (MeterHistory) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(16.dp),
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            // Summary Header Card (without border)
            item(key = "history_summary") {
                HistorySummaryCard(
                    totalCount = totalCount,
                    totalCost = totalCost,
                    totalDistanceMeters = totalDistanceMeters,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // History Items with Swipe-to-Dismiss
            items(
                items = histories,
                key = { it.id },
            ) { history ->
                HistorySwipeItem(
                    history = history,
                    onDelete = { onDeleteHistory(history.id) },
                    onCopy = { onCopyHistory(history) },
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Clear All Button at bottom
            item(key = "history_clear_all") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    TextButton(
                        onClick = onClearAllClick,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error,
                        ),
                    ) {
                        Text(
                            text = stringResource(R.string.history_btn_clear_all),
                            style = Typography.labelLarge,
                        )
                    }
                }
            }
        }
    }
}

/**
 * Summary Card displaying total statistics (without border to differentiate hierarchy)
 */
@Composable
private fun HistorySummaryCard(
    modifier: Modifier = Modifier,
    totalCount: Int,
    totalCost: Long,
    totalDistanceMeters: Double,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SummaryItem(
                label = stringResource(R.string.history_summary_trips),
                value = "${totalCount}",
            )
            SummaryItem(
                label = stringResource(R.string.history_summary_cost),
                value = stringResource(
                    R.string.meter_cost,
                    NumberFormat.getNumberInstance().format(totalCost),
                ),
            )
            SummaryItem(
                label = stringResource(R.string.history_summary_distance),
                value = String.format(Locale.getDefault(), "%.1f km", totalDistanceMeters / 1000.0),
            )
        }
    }
}

/**
 * Single item in summary card
 */
@Composable
private fun SummaryItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            style = Typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = Typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

/**
 * Swipe to dismiss wrapper for history item
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistorySwipeItem(
    modifier: Modifier = Modifier,
    history: MeterHistory,
    onDelete: () -> Unit,
    onCopy: () -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState()

    SwipeToDismissBox(
        modifier = modifier,
        state = dismissState,
        enableDismissFromStartToEnd = false,
        onDismiss = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
            }
        },
        backgroundContent = {
            val isDismissing = dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart
            val backgroundColor = if (isDismissing) {
                MaterialTheme.colorScheme.errorContainer
            } else {
                Color.Transparent
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = backgroundColor,
                        shape = CardDefaults.shape,
                    )
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                if (isDismissing) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                    )
                }
            }
        },
    ) {
        HistoryItemCard(
            modifier = Modifier.fillMaxWidth(),
            history = history,
            onClick = onCopy,
        )
    }
}

/**
 * Card representing single driving record (matching StoreScreen ProductItem tone & manner)
 */
@Composable
private fun HistoryItemCard(
    modifier: Modifier = Modifier,
    history: MeterHistory,
    onClick: () -> Unit,
) {
    val dateText = remember(history.timestamp) {
        SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault()).format(Date(history.timestamp))
    }
    val costText = stringResource(
        R.string.meter_cost,
        NumberFormat.getNumberInstance().format(history.cost),
    )
    val distanceText = remember(history.distanceMeters) {
        String.format(Locale.getDefault(), "%.1f km", history.distanceMeters / 1000.0)
    }
    val durationText = remember(history.elapsedSeconds) {
        val totalSeconds = history.elapsedSeconds.toInt()
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        Pair(minutes, seconds)
    }
    val detailText = "$distanceText · " + stringResource(
        R.string.history_item_duration,
        durationText.first,
        durationText.second,
    )

    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Driving Info (Left Column matching Store ProductItem)
            Column(
                modifier = Modifier.weight(1f),
            ) {
                // Date & Time (as Title)
                Text(
                    text = dateText,
                    style = Typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Distance & Duration (as Desc)
                Text(
                    text = detailText,
                    style = Typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Cost (Right label matching Store price)
            Text(
                text = costText,
                color = MaterialTheme.colorScheme.primary,
                style = Typography.titleSmall,
            )
        }
    }
}
