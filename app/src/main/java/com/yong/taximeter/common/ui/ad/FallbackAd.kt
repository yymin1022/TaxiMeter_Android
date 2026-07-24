package com.yong.taximeter.common.ui.ad

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.yong.taximeter.R

/**
 * Data class representing a fallback ad
 */
data class FallbackAd(
    @DrawableRes val iconRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val subtitleRes: Int,
    @StringRes val targetUrl: Int,
    @StringRes val ctaRes: Int = R.string.fallback_ad_cta_default
)
