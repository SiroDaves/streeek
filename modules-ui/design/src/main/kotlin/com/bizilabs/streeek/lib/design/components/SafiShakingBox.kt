package com.bizilabs.streeek.lib.design.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun SafiShakingBox(content: @Composable () -> Unit) {
    var enabled by remember { mutableStateOf(true) }
    val infiniteTransition = rememberInfiniteTransition()
    val scaleInfinite by infiniteTransition.animateFloat(
        label = "animated scale",
        initialValue = 1f,
        targetValue = .85f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(60, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
    )

    val rotation by infiniteTransition.animateFloat(
        label = "animated rotation",
        initialValue = -10f,
        targetValue = 10f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(60, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
    )

    Box(
        modifier =
            Modifier
                .graphicsLayer {
                    scaleX = scaleInfinite
                    scaleY = scaleInfinite
                    rotationZ = rotation
                },
    ) {
        content()
    }
}
