package com.yong.taximeter.common.ui.ad

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.yong.taximeter.R

/**
 * Data class representing a fallback ad
 */
data class FallbackAd(
    val bgColor: Color = Color.DarkGray,
    val textColor: Color = Color.White,
    @DrawableRes val iconRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val descRes: Int,
    @StringRes val targetUrlRes: Int? = null,
    @StringRes val ctaRes: Int = R.string.fallback_ad_cta_default,
)