package com.metroplus.app.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// Update pe confetti — 63 wali (60-updater.js triggerBigConfetti) jaisi:
// neeche dono kono se blast, upar ja ke phail ke neeche girti hai.
// Colors: 63 ki CONFETTI_PARTY list. Shapes: square + circle (+ ribbon ki
// jagah lamba rotated rect). Sirf GUI hai — koi touch nahi rokta.

// 63 ki party colors.
private val PartyColors = listOf(
    Color(0xFFFF2A2A), Color(0xFFFF6B6B), Color(0xFFFFD93D), Color(0xFFFFB300),
    Color(0xFFFF6B9D), Color(0xFF6BCBFF), Color(0xFFFFFFFF), Color(0xFFB0060F)
)

private class Piece(
    val x0: Float, val y0: Float, val vx: Float, val vy: Float,
    val size: Float, val color: Color, val circle: Boolean, val rot: Float, val rotSpeed: Float
)

@Composable
fun ConfettiOverlay(onDone: () -> Unit) {
    var t by remember { mutableStateOf(0f) }
    val pieces = remember {
        val rnd = Random(31)
        List(90) { i ->
            val left = i % 2 == 0
            val angle = if (left) rnd.nextFloat() * 0.9f + 0.15f else 3.14159f - rnd.nextFloat() * 0.9f - 0.15f
            val speed = 1.1f + rnd.nextFloat() * 1.3f
            Piece(
                x0 = if (left) 0.04f else 0.96f, y0 = 1.02f,
                vx = cos(angle) * speed, vy = -sin(angle) * speed * 1.6f,
                size = 6f + rnd.nextFloat() * 9f,
                color = PartyColors[rnd.nextInt(PartyColors.size)],
                circle = rnd.nextFloat() < 0.4f,
                rot = rnd.nextFloat() * 360f,
                rotSpeed = (rnd.nextFloat() - 0.5f) * 720f
            )
        }
    }
    LaunchedEffect(Unit) {
        val start = withFrameNanos { it }
        while (true) {
            val now = withFrameNanos { it }
            val elapsed = (now - start) / 1_000_000_000f
            t = elapsed
            if (elapsed > 2.6f) break
        }
        onDone()
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val g = 1.5f // gravity (screen heights / s^2)
            for (p in pieces) {
                val x = (p.x0 + p.vx * t * 0.45f) * w
                val y = (p.y0 + p.vy * t * 0.45f + 0.5f * g * t * t * 0.45f) * h
                if (y > h + 40 || x < -40 || x > w + 40) continue
                val alpha = when {
                    t < 0.2f -> t / 0.2f
                    t > 2.1f -> ((2.6f - t) / 0.5f).coerceIn(0f, 1f)
                    else -> 1f
                }
                val s = p.size * (if (p.circle) 1f else 1.6f)
                if (p.circle) {
                    drawCircle(color = p.color.copy(alpha = alpha), radius = s / 2, center = Offset(x, y))
                } else {
                    rotate(degrees = p.rot + p.rotSpeed * t, pivot = Offset(x, y)) {
                        drawRect(color = p.color.copy(alpha = alpha), topLeft = Offset(x - s / 2, y - s / 4), size = androidx.compose.ui.geometry.Size(s, s / 2))
                    }
                }
            }
        }
    }
}
