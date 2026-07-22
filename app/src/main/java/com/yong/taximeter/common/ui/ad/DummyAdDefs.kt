package com.yong.taximeter.common.ui.ad

/**
 * Utility object managing dummy ad definitions and selection logic
 */
object DummyAdDefs {
    const val DUMMY_AD_ROTATION_INTERVAL_MS = 15_000L

    val dummyAdList: List<DummyAd> = emptyList()

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
