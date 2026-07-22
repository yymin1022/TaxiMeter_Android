package com.yong.taximeter.common.ui.ad

import com.yong.taximeter.R

/**
 * Utility object managing dummy ad definitions and selection logic
 */
object DummyAdDefs {
    const val DUMMY_AD_ROTATION_INTERVAL_MS = 15_000L

    val dummyAdList: List<DummyAd> = listOf(
        DummyAd(
            iconRes = R.drawable.ic_blog_icon,
            titleRes = R.string.ad_fallback_headline,
            subtitleRes = R.string.ad_fallback_body,
            targetUrl = "https://dev-lr.com",
            ctaRes = R.string.ad_fallback_cta
        )
    )

    /**
     * Get a random ad from dummyAdList, optionally excluding the currently displayed ad
     */
    fun getRandomAd(except: DummyAd? = null): DummyAd? {
        if (dummyAdList.isEmpty()) return null
        val candidateList = if (dummyAdList.size > 1 && except != null) {
            dummyAdList.filter { it != except }
        } else {
            dummyAdList
        }
        return candidateList.randomOrNull() ?: dummyAdList.firstOrNull()
    }
}
