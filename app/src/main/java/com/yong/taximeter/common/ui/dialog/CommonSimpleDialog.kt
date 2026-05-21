package com.yong.taximeter.common.ui.dialog

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.yong.taximeter.R

/**
 * A simple dialog, with title and description
 * - And also, [onConfirm], [onDismiss] actions
 */

@Composable
fun SimpleDialog(
    @StringRes
    titleRes: Int? = null,
    title: String? = null,
    @StringRes
    descRes: Int? = null,
    desc: String? = null,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val titleText = titleRes?.let { stringResource(it) }
        ?: title ?: ""
    val descText = descRes?.let { stringResource(it) }
        ?: desc ?: ""

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(titleText)
        },
        text = {
            Text(
                text = descText,
                fontSize = 17.sp,
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.dialog_ok),
                    fontSize = 17.sp,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.dialog_cancel),
                    fontSize = 17.sp,
                )
            }
        }
    )
}