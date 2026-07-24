package com.yong.taximeter.common.ui.ad

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
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
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.yong.taximeter.R
import androidx.core.net.toUri
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
        com.google.android.gms.ads.AdView(context).apply {
            setAdSize(com.google.android.gms.ads.AdSize.BANNER)
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
        modifier = modifier
            .height(50.dp) // Fixed height to prevent layout shifts
    ) {
        if (!isLoading && !isFailed) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { adView }
            )
        } else {
            val iconRes = fallbackAd?.iconRes ?: fallbackImageRes
            val titleText = fallbackAd?.titleRes?.let { stringResource(it) } ?: stringResource(R.string.ad_fallback_headline)
            val subtitleText = fallbackAd?.subtitleRes?.let { stringResource(it) } ?: stringResource(R.string.ad_fallback_body)
            val ctaText = fallbackAd?.ctaRes?.let { stringResource(it) } ?: stringResource(R.string.ad_fallback_cta)
            val targetUrl = fallbackAd?.targetUrl ?: fallbackUrl

            Row(
                modifier = Modifier
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
                    contentDescription = "Useful Blog Icon",
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
    }
}

@Composable
fun NativeAdViewCompose(
    modifier: Modifier = Modifier,
    adUnitId: String = "ca-app-pub-3940256099942544/2247696110", // Test Native Ad ID
    @DrawableRes fallbackImageRes: Int = R.drawable.ic_blog_icon,
    fallbackHeadline: String = stringResource(R.string.ad_fallback_headline),
    fallbackBody: String = stringResource(R.string.ad_fallback_body),
    fallbackCtaText: String = stringResource(R.string.ad_fallback_cta),
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
        modifier = modifier
            .height(90.dp) // Fixed height to prevent layout shifts
    ) {
        if (nativeAd != null && !isLoading && !isFailed) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    val inflater = LayoutInflater.from(ctx)
                    val adView = inflater.inflate(R.layout.layout_native_ad, null) as NativeAdView
                    val headlineView = adView.findViewById<TextView>(R.id.ad_headline)
                    val bodyView = adView.findViewById<TextView>(R.id.ad_body)
                    val iconView = adView.findViewById<ImageView>(R.id.ad_icon)

                    headlineView.text = nativeAd?.headline
                    adView.headlineView = headlineView

                    bodyView.text = nativeAd?.body
                    adView.bodyView = bodyView

                    nativeAd?.icon?.let { icon ->
                        iconView.setImageDrawable(icon.drawable)
                        adView.iconView = iconView
                    }

                    adView.setNativeAd(nativeAd!!)
                    adView
                }
            )
        } else {
            val iconRes = fallbackAd?.iconRes ?: fallbackImageRes
            val titleText = fallbackAd?.titleRes?.let { stringResource(it) } ?: fallbackHeadline
            val bodyText = fallbackAd?.subtitleRes?.let { stringResource(it) } ?: fallbackBody
            val ctaText = fallbackAd?.ctaRes?.let { stringResource(it) } ?: fallbackCtaText
            val targetUrl = fallbackAd?.targetUrl ?: fallbackUrl

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x1A888888))
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, targetUrl.toUri())
                        context.startActivity(intent)
                    }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = "Default Ad Icon",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(end = 12.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFF9800))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "AD",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = titleText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        text = bodyText,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, targetUrl.toUri())
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .height(36.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text(
                        text = ctaText,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
