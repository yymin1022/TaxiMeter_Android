package com.yong.taximeter.common.window

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Rect
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker

/**
 * 폴더블 단말의 힌지 자세 및 상태 모델
 */
sealed interface FoldablePosture {
    data object Unavailable : FoldablePosture

    data class Available(
        val state: FoldingFeature.State,
        val orientation: FoldingFeature.Orientation,
        val isSeparating: Boolean,
        val bounds: Rect,
    ) : FoldablePosture {
        val isVerticalHinge: Boolean
            get() = orientation == FoldingFeature.Orientation.VERTICAL

        val isHorizontalHinge: Boolean
            get() = orientation == FoldingFeature.Orientation.HORIZONTAL

        val isHalfOpened: Boolean
            get() = state == FoldingFeature.State.HALF_OPENED

        val isFlat: Boolean
            get() = state == FoldingFeature.State.FLAT
    }
}

/**
 * Activity의 [WindowInfoTracker]를 통해 폴더블 힌지 상태([FoldablePosture])를 관측하는 Composable
 */
@Composable
fun rememberFoldablePosture(): FoldablePosture {
    val context = LocalContext.current
    val activity = LocalActivity.current ?: remember(context) { context.findActivity() }
        ?: return FoldablePosture.Unavailable

    val windowLayoutInfo by remember(activity) {
        WindowInfoTracker.getOrCreate(activity).windowLayoutInfo(activity)
    }.collectAsStateWithLifecycle(initialValue = null)

    return remember(windowLayoutInfo) {
        val foldingFeature = windowLayoutInfo?.displayFeatures
            ?.filterIsInstance<FoldingFeature>()
            ?.firstOrNull()

        if (foldingFeature != null) {
            FoldablePosture.Available(
                state = foldingFeature.state,
                orientation = foldingFeature.orientation,
                isSeparating = foldingFeature.isSeparating,
                bounds = foldingFeature.bounds,
            )
        } else {
            FoldablePosture.Unavailable
        }
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
