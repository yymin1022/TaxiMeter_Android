package com.yong.taximeter.common.ui.ad

import com.yong.taximeter.R

/**
 * Utility object managing fallback ad definitions and selection logic
 */
object FallbackAdDefs {
    const val FALLBACK_AD_ROTATION_INTERVAL_MS = 15_000L

    val fallbackAdList: List<FallbackAd> = listOf(
        FallbackAd(
            iconRes = R.drawable.ic_blog_icon,
            titleRes = R.string.fallback_ad_blog_title,
            subtitleRes = R.string.fallback_ad_blog_desc,
            targetUrl = "https://dev-lr.com",
            ctaRes = R.string.fallback_ad_cta_default
        )
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
