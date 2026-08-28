package com.example.gram.ui.lessoncontentscreen.leftbar

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.gram.ui.theme.ScrollBackgroundColor
import com.example.gram.ui.theme.ScrollThumbColor
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun VerticalScrollbar(
    modifier: Modifier = Modifier,
    scrollState: ScrollState
) {
    val metrics = rememberScrollbarMetrics(scrollState) ?: return

    BoxWithConstraints(
        modifier = modifier
            .width(8.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(ScrollBackgroundColor)
    ) {
        val availableHeightPx = constraints.maxHeight.toFloat()
        val thumbHeightPx = availableHeightPx * metrics.thumbHeightFraction
        val maxTravelPx = availableHeightPx - thumbHeightPx

        Box(
            modifier = Modifier
                .fillMaxSize()
                .scrollbarGestures(
                    scrollState = scrollState,
                    maxTravelPx = maxTravelPx,
                    thumbHeightPx = thumbHeightPx
                )
        ) {
            ScrollbarThumb(
                scrollState = scrollState,
                thumbHeightFraction = metrics.thumbHeightFraction,
                maxTravelPx = maxTravelPx
            )
        }
    }
}

@Composable
private fun rememberScrollbarMetrics(scrollState: ScrollState): ScrollbarMetrics? {
    return remember(scrollState) {
        derivedStateOf {
            val viewportHeight = scrollState.viewportSize.toFloat()
            val scrollRange = scrollState.maxValue.toFloat()
            val totalContentHeight = scrollRange + viewportHeight

            if (scrollRange <= 0f || totalContentHeight <= 0f) {
                null
            } else {
                ScrollbarMetrics(
                    thumbHeightFraction = (viewportHeight / totalContentHeight).coerceIn(0.1f, 1f)
                )
            }
        }
    }.value
}

private fun Modifier.scrollbarGestures(
    scrollState: ScrollState,
    maxTravelPx: Float,
    thumbHeightPx: Float
): Modifier = this.pointerInput(scrollState, maxTravelPx, thumbHeightPx) {
    coroutineScope {
        detectTapGestures { offset ->
            if (maxTravelPx <= 0f) return@detectTapGestures

            val fraction = (offset.y - thumbHeightPx / 2f).coerceIn(0f, maxTravelPx) / maxTravelPx
            val target = (fraction * scrollState.maxValue).toInt().coerceIn(0, scrollState.maxValue)

            launch {
                scrollState.scrollTo(target)
            }
        }
    }
}.pointerInput(scrollState, maxTravelPx) {
    coroutineScope {
        detectDragGestures { change, dragAmount ->
            change.consume()

            if (maxTravelPx <= 0f) return@detectDragGestures

            val currentThumbPosition = if (scrollState.maxValue > 0) {
                scrollState.value.toFloat() / scrollState.maxValue * maxTravelPx
            } else {
                0f
            }

            val newThumbPosition = (currentThumbPosition + dragAmount.y).coerceIn(0f, maxTravelPx)
            val fraction = newThumbPosition / maxTravelPx
            val target = (fraction * scrollState.maxValue).toInt().coerceIn(0, scrollState.maxValue)

            launch {
                scrollState.scrollTo(target)
            }
        }
    }
}

@Composable
private fun ScrollbarThumb(
    scrollState: ScrollState,
    thumbHeightFraction: Float,
    maxTravelPx: Float
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(thumbHeightFraction)
            .offset {
                val scrollFraction = if (scrollState.maxValue > 0) {
                    scrollState.value.toFloat() / scrollState.maxValue
                } else {
                    0f
                }

                val thumbOffsetPx = scrollFraction * maxTravelPx

                IntOffset(
                    x = 0,
                    y = thumbOffsetPx.toInt()
                )
            }
            .padding(horizontal = 1.dp)
            .background(
                ScrollThumbColor,
                RoundedCornerShape(3.dp)
            )
    )
}

private data class ScrollbarMetrics(
    val thumbHeightFraction: Float
)
