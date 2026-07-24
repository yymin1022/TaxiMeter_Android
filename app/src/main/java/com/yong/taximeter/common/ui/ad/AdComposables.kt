package com.yong.taximeter.common.ui.ad

import android.content.Intent
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.yong.taximeter.R
import kotlinx.coroutines.delay

@Composable
fun BannerAdView(
    modifier: Modifier = Modifier,
    adUnitId: String = "ca-app-pub-3940256099942544/6300978111", // GMS Ads Banner Test ID
    @DrawableRes fallbackImageRes: Int = R.drawable.ic_blog_icon,
    fallbackUrl: String = "https://dev-lr.com"
) {
    val context = LocalContext.current
    var isFailed by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var fallbackAd by remember { mutableStateOf(FallbackAdDefs.getRandomAd()) }

    val adView = remember(context, adUnitId) {
        AdView(context).apply {
            setAdSize(AdSize.BANNER)
            setAdUnitId(adUnitId)
            adListener = object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    isFailed = true
                    isLoading = false
                }
                override fun onAdLoaded() {
                    isLoading = false
                    isFailed = false
                }
            }
        }
    }

    DisposableEffect(adView) {
        adView.loadAd(AdRequest.Builder().build())
        onDispose {
            adView.destroy()
        }
    }

    LaunchedEffect(isLoading, isFailed) {
        if (isLoading || isFailed) {
            while (true) {
                delay(FallbackAdDefs.FALLBACK_AD_ROTATION_INTERVAL_MS)
                fallbackAd = FallbackAdDefs.getRandomAd(except = fallbackAd)
            }
        }
    }

    Box(
        modifier = modifier.height(50.dp) // Fixed height to prevent layout shifts
    ) {
        if (!isLoading && !isFailed) {
            BannerAdContent(adView = adView)
        } else {
            FallbackAdContent(
                fallbackAd = fallbackAd,
                fallbackImageRes = fallbackImageRes,
            )
        }
    }
}

@Composable
fun NativeAdViewCompose(
    modifier: Modifier = Modifier,
    adUnitId: String = "ca-app-pub-3940256099942544/2247696110", // Test Native Ad ID
    @DrawableRes fallbackImageRes: Int = R.drawable.ic_blog_icon,
    fallbackHeadline: String = stringResource(R.string.fallback_ad_blog_title),
    fallbackBody: String = stringResource(R.string.fallback_ad_blog_desc),
    fallbackCtaText: String = stringResource(R.string.fallback_ad_cta_default),
    fallbackUrl: String = "https://dev-lr.com"
) {
    val context = LocalContext.current
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
    var isFailed by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var fallbackAd by remember { mutableStateOf(FallbackAdDefs.getRandomAd()) }

    LaunchedEffect(adUnitId) {
        val adLoader = AdLoader.Builder(context, adUnitId)
            .forNativeAd { ad ->
                nativeAd = ad
                isLoading = false
                isFailed = false
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    isFailed = true
                    isLoading = false
                }
            })
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    DisposableEffect(nativeAd) {
        onDispose {
            nativeAd?.destroy()
        }
    }

    LaunchedEffect(isLoading, isFailed) {
        if (isLoading || isFailed) {
            while (true) {
                delay(FallbackAdDefs.FALLBACK_AD_ROTATION_INTERVAL_MS)
                fallbackAd = FallbackAdDefs.getRandomAd(except = fallbackAd)
            }
        }
    }

    Box(
        modifier = modifier.height(90.dp) // Fixed height to prevent layout shifts
    ) {
        if (nativeAd != null && !isLoading && !isFailed) {
            NativeAdContent(nativeAd = nativeAd!!)
        } else {
            FallbackAdContent(
                modifier = Modifier.padding(vertical = 16.dp),
                fallbackAd = fallbackAd,
                fallbackImageRes = fallbackImageRes,
            )
        }
    }
}

@Composable
fun BannerAdContent(
    adView: AdView,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { adView }
    )
}

@Composable
fun NativeAdContent(
    nativeAd: NativeAd,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { ctx ->
            val inflater = LayoutInflater.from(ctx)
            val adView = inflater.inflate(R.layout.layout_native_ad, null) as NativeAdView
            val headlineView = adView.findViewById<TextView>(R.id.ad_headline)
            val bodyView = adView.findViewById<TextView>(R.id.ad_body)
            val iconView = adView.findViewById<ImageView>(R.id.ad_icon)

            headlineView.text = nativeAd.headline
            adView.headlineView = headlineView

            bodyView.text = nativeAd.body
            adView.bodyView = bodyView

            nativeAd.icon?.let { icon ->
                iconView.setImageDrawable(icon.drawable)
                adView.iconView = iconView
            }

            adView.setNativeAd(nativeAd)
            adView
        }
    )
}

@Composable
fun FallbackAdContent(
    fallbackAd: FallbackAd?,
    @DrawableRes fallbackImageRes: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val iconRes = fallbackAd?.iconRes ?: fallbackImageRes
    val titleText = fallbackAd?.titleRes?.let { stringResource(it) } ?: stringResource(R.string.fallback_ad_blog_title)
    val subtitleText = fallbackAd?.subtitleRes?.let { stringResource(it) } ?: stringResource(R.string.fallback_ad_blog_desc)
    val ctaText = fallbackAd?.ctaRes?.let { stringResource(it) } ?: stringResource(R.string.fallback_ad_cta_default)
    val targetUrl = stringResource(fallbackAd?.targetUrl ?: R.string.fallback_ad_blog_url)

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF103D88))
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, targetUrl.toUri())
                context.startActivity(intent)
            }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = "Fallback Ad Icon",
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = titleText,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitleText,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = ctaText,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
