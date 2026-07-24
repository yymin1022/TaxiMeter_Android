package com.yong.taximeter.common.ui.ad

import androidx.compose.ui.graphics.Color
import com.yong.taximeter.R

/**
 * Utility object managing fallback ad definitions and selection logic
 */
object FallbackAdDefs {
    const val FALLBACK_AD_ROTATION_INTERVAL_MS = 15_000L

    val FALLBACK_AD_DEFAULT_COLOR = Color.DarkGray
    val FALLBACK_AD_DEFAULT_TEXT_COLOR = Color.White
    val FALLBACK_AD_DEFAULT_ICON_RES = R.drawable.ic_noti_taxi
    val FALLBACK_AD_DEFAULT_TITLE_RES = R.string.fallback_ad_title_default
    val FALLBACK_AD_DEFAULT_DESC_RES = R.string.fallback_ad_desc_default


    val fallbackAdList: List<FallbackAd> = listOf(
        // Default - Remove Advertisement
        FallbackAd(
            bgColor = FALLBACK_AD_DEFAULT_COLOR,
            textColor = FALLBACK_AD_DEFAULT_TEXT_COLOR,
            iconRes = FALLBACK_AD_DEFAULT_ICON_RES,
            titleRes = FALLBACK_AD_DEFAULT_TITLE_RES,
            descRes = FALLBACK_AD_DEFAULT_DESC_RES,
        ),

        // Blog
        FallbackAd(
            bgColor = Color(0xFF103D88),
            iconRes = R.drawable.ic_blog_icon,
            titleRes = R.string.fallback_ad_blog_title,
            descRes = R.string.fallback_ad_blog_desc,
            targetUrlRes = R.string.fallback_ad_blog_url,
        ),

        // Typer by NewMeans
        FallbackAd(
            bgColor = Color(0xFFFCF2D9),
            textColor = Color.Black,
            iconRes = R.mipmap.ic_typer,
            titleRes = R.string.fallback_ad_typer_title,
            descRes = R.string.fallback_ad_typer_desc,
            targetUrlRes = R.string.fallback_ad_typer_url,
        ),
    )

    /**
     * Get a random ad from fallbackAdList, optionally excluding the currently displayed ad
     */
    fun getRandomAd(except: FallbackAd? = null): FallbackAd? {
        if (fallbackAdList.isEmpty()) return null
        val candidateList = if (fallbackAdList.size > 1 && except != null) {
            fallbackAdList.filter { it != except }
        } else {
            fallbackAdList
        }
        return candidateList.randomOrNull() ?: fallbackAdList.firstOrNull()
    }
}
