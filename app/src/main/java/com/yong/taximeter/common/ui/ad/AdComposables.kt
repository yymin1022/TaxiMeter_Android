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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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

@Composable
fun BannerAdView(
    modifier: Modifier = Modifier,
    adUnitId: String = "ca-app-pub-3940256099942544/6300978111", // GMS Ads Banner Test ID
    @DrawableRes fallbackImageRes: Int = R.drawable.ic_launcher_foreground,
    fallbackUrl: String = "https://dev-lr.com"
) {
    val context = LocalContext.current
    var isFailed by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .height(50.dp) // Fixed height to prevent layout shifts
    ) {
        if (isFailed) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = listOf(Color(0xFF1E5FC1), Color(0xFF103D88))
                        )
                    )
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, fallbackUrl.toUri())
                        context.startActivity(intent)
                    }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = fallbackImageRes),
                    contentDescription = "Useful Blog Icon",
                    modifier = Modifier
                        .size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Useful Blog",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "1인개발자 Useful의 IT블로그 방문하기",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = "방문하기",
                    color = Color(0xFFFF9800),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            val adView = remember {
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

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { adView }
            )

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray.copy(alpha = 0.05f))
                )
            }
        }
    }
}

@Composable
fun NativeAdViewCompose(
    modifier: Modifier = Modifier,
    adUnitId: String = "ca-app-pub-3940256099942544/2247696110", // Test Native Ad ID
    @DrawableRes fallbackImageRes: Int = R.drawable.ic_launcher_foreground,
    fallbackHeadline: String = "Useful Blog",
    fallbackBody: String = "1인개발자 Useful의 IT블로그",
    fallbackCtaText: String = "방문하기",
    fallbackUrl: String = "https://dev-lr.com"
) {
    val context = LocalContext.current
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
    var isFailed by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .height(90.dp) // Fixed height to prevent layout shifts
    ) {
        if (isFailed) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x1A888888))
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, fallbackUrl.toUri())
                        context.startActivity(intent)
                    }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = fallbackImageRes),
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
                            text = fallbackHeadline,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        text = fallbackBody,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl))
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .height(36.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text(
                        text = fallbackCtaText,
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            LaunchedEffect(adUnitId) {
                val adLoader = AdLoader.Builder(context, adUnitId)
                    .forNativeAd { ad ->
                        nativeAd = ad
                        isLoading = false
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

            val currentNativeAd = nativeAd
            if (currentNativeAd != null) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        val view = LayoutInflater.from(ctx).inflate(R.layout.layout_native_ad, null) as NativeAdView
                        
                        // Bind views
                        val headlineView = view.findViewById<TextView>(R.id.ad_headline)
                        headlineView.text = currentNativeAd.headline
                        view.headlineView = headlineView

                        val bodyView = view.findViewById<TextView>(R.id.ad_body)
                        if (currentNativeAd.body == null) {
                            bodyView.visibility = View.GONE
                        } else {
                            bodyView.visibility = View.VISIBLE
                            bodyView.text = currentNativeAd.body
                        }
                        view.bodyView = bodyView

                        val ctaView = view.findViewById<android.widget.Button>(R.id.ad_call_to_action)
                        if (currentNativeAd.callToAction == null) {
                            ctaView.visibility = View.GONE
                        } else {
                            ctaView.visibility = View.VISIBLE
                            ctaView.text = currentNativeAd.callToAction
                        }
                        view.callToActionView = ctaView

                        val iconView = view.findViewById<ImageView>(R.id.ad_icon)
                        if (currentNativeAd.icon == null) {
                            iconView.visibility = View.GONE
                        } else {
                            iconView.visibility = View.VISIBLE
                            iconView.setImageDrawable(currentNativeAd.icon?.drawable)
                        }
                        view.iconView = iconView

                        // Register native ad object
                        view.setNativeAd(currentNativeAd)
                        view
                    }
                )
            } else if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray.copy(alpha = 0.05f))
                )
            }
        }
    }
}
