package com.yong.taximeter.common.ui.ad

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.yong.taximeter.R

@Composable
fun BannerAdView(
    modifier: Modifier = Modifier,
    adUnitId: String = "ca-app-pub-3940256099942544/6300978111" // GMS Ads Banner Test ID
) {
    val context = LocalContext.current
    val adView = remember {
        com.google.android.gms.ads.AdView(context).apply {
            setAdSize(com.google.android.gms.ads.AdSize.BANNER)
            setAdUnitId(adUnitId)
        }
    }

    DisposableEffect(adView) {
        adView.loadAd(AdRequest.Builder().build())
        onDispose {
            adView.destroy()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { adView }
    )
}

@Composable
fun NativeAdViewCompose(
    modifier: Modifier = Modifier,
    adUnitId: String = "ca-app-pub-3940256099942544/2247696110" // Test Native Ad ID
) {
    val context = LocalContext.current
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
    var isFailed by remember { mutableStateOf(false) }

    LaunchedEffect(adUnitId) {
        val adLoader = AdLoader.Builder(context, adUnitId)
            .forNativeAd { ad ->
                nativeAd = ad
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    isFailed = true
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

    if (isFailed) {
        return
    }

    val currentNativeAd = nativeAd
    if (currentNativeAd != null) {
        AndroidView(
            modifier = modifier,
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

                val ctaView = view.findViewById<Button>(R.id.ad_call_to_action)
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
    } else {
        Spacer(modifier = modifier.height(80.dp))
    }
}
