package com.yong.taximeter.common.ui.dialog

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.yong.taximeter.R
import com.yong.taximeter.common.ui.theme.Typography

/**
 * A dialog, which shows list of radio buttons
 */
@Composable
fun RadioSelectDialog(
    @StringRes
    titleRes: Int?,
    radioItemTextResources: List<Int>,
    onComplete: (idx: Int) -> Unit,
    onDismiss: () -> Unit,
) {
    // Selected Index State
    var selectedIndex by remember { mutableIntStateOf(-1) }
    // Title Text
    val titleText = titleRes?.let { stringResource(it) } ?: ""

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(titleText) },
        text = {
            LazyColumn {
                itemsIndexed(radioItemTextResources) { idx, itemTextRes ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selectedIndex == idx,
                                onClick = { selectedIndex = idx },
                                role = Role.RadioButton,
                            )
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = selectedIndex == idx,
                            onClick = null,
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = stringResource(itemTextRes),
                            style = Typography.bodyLarge,
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = (selectedIndex in radioItemTextResources.indices),
                onClick = { onComplete(selectedIndex) },
            ) {
                Text(
                    text = stringResource(R.string.dialog_complete),
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text(
                    text = stringResource(R.string.dialog_cancel),
                )
            }
        },
    )
}