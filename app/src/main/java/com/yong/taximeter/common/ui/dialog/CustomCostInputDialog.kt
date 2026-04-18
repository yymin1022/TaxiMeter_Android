package com.yong.taximeter.common.ui.dialog

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.yong.taximeter.domain.model.CostInfo
import com.yong.taximeter.R
import com.yong.taximeter.domain.model.RegionSetting

/**
 * A dialog, which shows custom cost input form
 */
@Composable
fun CustomCostInputDialog(
    @StringRes
    titleRes: Int,
    onConfirm: (costInfo: CostInfo) -> Unit,
    onDismiss: () -> Unit,
) {
    // Initial Cost Info
    val initial = CostInfo()

    // Cost States
    var costBase by remember { mutableStateOf(initial.costBase.toFieldValue()) }
    var distBase by remember { mutableStateOf(initial.distBase.toFieldValue()) }
    var costRunPer by remember { mutableStateOf(initial.costRunPer.toFieldValue()) }
    var costTimePer by remember { mutableStateOf(initial.costTimePer.toFieldValue()) }
    var extraRateCity by remember { mutableStateOf(initial.extraRateCity.toFieldValue()) }
    var extraRateNight1 by remember { mutableStateOf(initial.extraRateNight1.toFieldValue()) }
    var extraRateNight2 by remember { mutableStateOf(initial.extraRateNight2.toFieldValue()) }
    var nightStartHour1 by remember { mutableStateOf(initial.nightStartHour1.toFieldValue()) }
    var nightStartHour2 by remember { mutableStateOf(initial.nightStartHour2.toFieldValue()) }
    var nightEndHour1 by remember { mutableStateOf(initial.nightEndHour1.toFieldValue()) }
    var nightEndHour2 by remember { mutableStateOf(initial.nightEndHour2.toFieldValue()) }

    // Input Cost Validation
    var valueErrors by remember { mutableStateOf(mapOf<String, String>()) }
    fun validate(context: Context): Boolean {
        val errorMessages = mutableMapOf<String, String>()

        // Validate each cost values
        listOf(
            "costBase" to costBase,
            "distBase" to distBase,
            "costRunPer" to costRunPer,
            "costTimePer" to costTimePer,
            "extraRateCity" to extraRateCity,
            "extraRateNight1" to extraRateNight1,
            "extraRateNight2" to extraRateNight2,
        ).forEach { (key, value) ->
            if(value.toIntOrNull() == null) {
                errorMessages[key] = context.getString(R.string.dialog_cost_input_error_cost)
            }
        }

        // Validate each time values
        listOf(
            "nightStartHour1" to nightStartHour1,
            "nightStartHour2" to nightStartHour2,
            "nightEndHour1" to nightEndHour1,
            "nightEndHour2" to nightEndHour2,
        ).forEach { (key, value) ->
            val hour = value.toIntOrNull()
            if(hour == null || hour !in 0..23) {
                errorMessages[key] = context.getString(R.string.dialog_cost_input_error_time)
            }
        }

        valueErrors = errorMessages
        return errorMessages.isEmpty()
    }

    // Alert Dialog UI
    AlertDialog(
        modifier = Modifier
            .fillMaxWidth(0.92f)
            .fillMaxHeight(0.85f),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss,
        title = { Text(stringResource(titleRes)) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // Base Cost
                SectionHeader(
                    text = stringResource(R.string.dialog_cost_input_section_base_cost),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    // Cost Base
                    CostTextField(
                        modifier = Modifier
                            .weight(1f),
                        error = valueErrors["costBase"],
                        label = stringResource(R.string.dialog_cost_input_label_cost_base),
                        placeholder = costBase,
                        value = costBase,
                        onValueChange = { costBase = it.filterDigits(); valueErrors = valueErrors - "costBase" },
                    )

                    // Dist Base
                    CostTextField(
                        modifier = Modifier
                            .weight(1f),
                        error = valueErrors["distBase"],
                        label = stringResource(R.string.dialog_cost_input_label_dist_base),
                        placeholder = distBase,
                        value = distBase,
                        onValueChange = { distBase = it.filterDigits(); valueErrors = valueErrors - "distBase" },
                    )
                }

                // Cost Run / Time Per
                SectionHeader(
                    text = stringResource(R.string.dialog_cost_input_section_cost_per),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    // Cost run per
                    CostTextField(
                        modifier = Modifier
                            .weight(1f),
                        error = valueErrors["costRunPer"],
                        label = stringResource(R.string.dialog_cost_input_label_run_per),
                        placeholder = costRunPer,
                        value = costRunPer,
                        onValueChange = { costRunPer = it.filterDigits(); valueErrors = valueErrors - "costRunPer" },
                    )

                    // Cost time per
                    CostTextField(
                        modifier = Modifier
                            .weight(1f),
                        error = valueErrors["costTimePer"],
                        label = stringResource(R.string.dialog_cost_input_label_time_per),
                        placeholder = costTimePer,
                        value = costTimePer,
                        onValueChange = { costTimePer = it.filterDigits(); valueErrors = valueErrors - "costTimePer" },
                    )
                }

                // Extra Rate
                SectionHeader(
                    text = stringResource(R.string.dialog_cost_input_section_extra_rate),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    // Out city
                    CostTextField(
                        modifier = Modifier
                            .weight(1f),
                        error = valueErrors["extraRateCity"],
                        label = stringResource(R.string.dialog_cost_input_label_out_city),
                        placeholder = extraRateCity,
                        value = extraRateCity,
                        onValueChange = { extraRateCity = it.filterDigits(); valueErrors = valueErrors - "extraRateCity" },
                    )

                    // Night step 1
                    CostTextField(
                        modifier = Modifier
                            .weight(1f),
                        error = valueErrors["extraRateNight1"],
                        label = stringResource(R.string.dialog_cost_input_label_night_1),
                        placeholder = extraRateNight1,
                        value = extraRateNight1,
                        onValueChange = { extraRateNight1 = it.filterDigits(); valueErrors = valueErrors - "extraRateNight1" },
                    )

                    // Night step 2
                    CostTextField(
                        modifier = Modifier
                            .weight(1f),
                        error = valueErrors["extraRateNight2"],
                        label = stringResource(R.string.dialog_cost_input_label_night_2),
                        placeholder = extraRateNight2,
                        value = extraRateNight2,
                        onValueChange = { extraRateNight2 = it.filterDigits(); valueErrors = valueErrors - "extraRateNight2" },
                    )
                }

                // Night Extra Rate Times - Step 1
                SectionHeader(
                    text = stringResource(R.string.dialog_cost_input_section_night_time_1),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    // Step 1 Start
                    CostTextField(
                        modifier = Modifier
                            .weight(1f),
                        error = valueErrors["nightStartHour1"],
                        label = stringResource(R.string.dialog_cost_input_label_night_time_start),
                        placeholder = nightStartHour1,
                        value = nightStartHour1,
                        onValueChange = { nightStartHour1 = it.filterDigits(); valueErrors = valueErrors - "nightStartHour1" },
                    )

                    // Step 1 End
                    CostTextField(
                        modifier = Modifier
                            .weight(1f),
                        error = valueErrors["nightEndHour1"],
                        label = stringResource(R.string.dialog_cost_input_label_night_time_end),
                        placeholder = nightEndHour1,
                        value = nightEndHour1,
                        onValueChange = { nightEndHour1 = it.filterDigits(); valueErrors = valueErrors - "nightEndHour1" },
                    )
                }

                // Night Extra Rate Times - Step 2
                SectionHeader(
                    text = stringResource(R.string.dialog_cost_input_section_night_time_2),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    // Step 2 Start
                    CostTextField(
                        modifier = Modifier
                            .weight(1f),
                        error = valueErrors["nightStartHour2"],
                        label = stringResource(R.string.dialog_cost_input_label_night_time_start),
                        placeholder = nightStartHour2,
                        value = nightStartHour2,
                        onValueChange = { nightStartHour2 = it.filterDigits(); valueErrors = valueErrors - "nightStartHour2" },
                    )

                    // Step 2 End
                    CostTextField(
                        modifier = Modifier
                            .weight(1f),
                        error = valueErrors["nightEndHour2"],
                        label = stringResource(R.string.dialog_cost_input_label_night_time_end),
                        placeholder = nightEndHour2,
                        value = nightEndHour2,
                        onValueChange = { nightEndHour2 = it.filterDigits(); valueErrors = valueErrors - "nightEndHour2" },
                    )
                }

                Spacer(Modifier.height(4.dp))
            }
        },
        confirmButton = {
            val context = LocalContext.current
            TextButton(
                onClick = {
                    if(validate(context)) {
                        onConfirm(
                            CostInfo(
                                region = RegionSetting.CUSTOM.key,
                                costBase = costBase.toIntOrNull() ?: 0,
                                distBase = distBase.toIntOrNull() ?: 0,
                                costRunPer = costRunPer.toIntOrNull() ?: 0,
                                costTimePer = costTimePer.toIntOrNull() ?: 0,
                                extraRateCity = extraRateCity.toIntOrNull() ?: 0,
                                extraRateNight1 = extraRateNight1.toIntOrNull() ?: 0,
                                extraRateNight2 = extraRateNight2.toIntOrNull() ?: 0,
                                nightStartHour1 = nightStartHour1.toIntOrNull() ?: 0,
                                nightStartHour2 = nightStartHour2.toIntOrNull() ?: 0,
                                nightEndHour1 = nightEndHour1.toIntOrNull() ?: 0,
                                nightEndHour2 = nightEndHour2.toIntOrNull() ?: 0,
                            )
                        )
                    }
                },
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

/**
 * Cost Section Header
 */
@Composable
private fun SectionHeader(
    text: String,
) {
    Text(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 2.dp),
        text = text,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleSmall,
    )
}

/**
 * Cost Input Text Field
 */
@Composable
private fun CostTextField(
    modifier: Modifier = Modifier,
    error: String?,
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
        ),
        isError = error != null,
        label = { Text(label, maxLines = 1) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        supportingText = error?.let { { Text(it) } },
        value = value,
        onValueChange = onValueChange,
    )
}

/** If value is 0, it is empty string */
private fun Int.toFieldValue(): String
    = if(this == 0) "" else this.toString()

/** Filter only numbers from String */
private fun String.filterDigits(): String
    = this.filter { it.isDigit() }