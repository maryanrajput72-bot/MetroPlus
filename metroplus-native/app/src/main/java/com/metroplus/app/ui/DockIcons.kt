package com.metroplus.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.unit.dp

// Dock ke apne 3D icons (rangeen, chamakdaar). Har icon: neeche halka zameeni saya, shakal ka
// gehra "depth" saya (2-2.5 neeche), radial/linear gradient wali asal shakal, aur safed chamak.
// Layers ek hi spec se bane hain jis se browser preview bhi banaya gaya tha (preview = app).
enum class DockIcon { SETTINGS, OPENING, HOME, CLOSING, PORTAL }

@Composable
fun rememberDockIcon(icon: DockIcon): ImageVector = remember(icon) { buildDockIcon(icon) }

private fun nodes(d: String) = PathParser().parsePathString(d).toNodes()

private fun ImageVector.Builder.fill(d: String, brush: Brush, alpha: Float) =
    addPath(pathData = nodes(d), fill = brush, fillAlpha = alpha)

private fun ImageVector.Builder.line(d: String, color: Color, width: Float, alpha: Float) =
    addPath(pathData = nodes(d), stroke = SolidColor(color), strokeAlpha = alpha, strokeLineWidth = width,
        strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round)

private fun buildDockIcon(icon: DockIcon): ImageVector =
    ImageVector.Builder(defaultWidth = 64.dp, defaultHeight = 64.dp, viewportWidth = 64f, viewportHeight = 64f).apply {
        when (icon) {
            DockIcon.SETTINGS -> {
                fill("M15,59a17,3 0 1,0 34,0a17,3 0 1,0 -34,0z", SolidColor(Color(0xFF000000)), 0.22f)
                group(translationY = 2.5f) { fill("M51.6 25.0 L56.6 26.6 L56.6 35.4 L51.6 37.0 L50.1 40.7 L52.5 45.3 L46.3 51.5 L41.7 49.1 L38.0 50.6 L36.4 55.6 L27.6 55.6 L26.0 50.6 L22.3 49.1 L17.7 51.5 L11.5 45.3 L13.9 40.7 L12.4 37.0 L7.4 35.4 L7.4 26.6 L12.4 25.0 L13.9 21.3 L11.5 16.7 L17.7 10.5 L22.3 12.9 L26.0 11.4 L27.6 6.4 L36.4 6.4 L38.0 11.4 L41.7 12.9 L46.3 10.5 L52.5 16.7 L50.1 21.3Z", SolidColor(Color(0xFF2B2F8F)), 1.0f) }
                fill("M51.6 25.0 L56.6 26.6 L56.6 35.4 L51.6 37.0 L50.1 40.7 L52.5 45.3 L46.3 51.5 L41.7 49.1 L38.0 50.6 L36.4 55.6 L27.6 55.6 L26.0 50.6 L22.3 49.1 L17.7 51.5 L11.5 45.3 L13.9 40.7 L12.4 37.0 L7.4 35.4 L7.4 26.6 L12.4 25.0 L13.9 21.3 L11.5 16.7 L17.7 10.5 L22.3 12.9 L26.0 11.4 L27.6 6.4 L36.4 6.4 L38.0 11.4 L41.7 12.9 L46.3 10.5 L52.5 16.7 L50.1 21.3Z", Brush.radialGradient(0.0f to Color(0xFFD6DCFF), 0.5f to Color(0xFF7C8BF5), 1.0f to Color(0xFF3F46C2), center = Offset(25.0f, 16.0f), radius = 44.0f), 1.0f)
                fill("M21,31a11,11 0 1,0 22,0a11,11 0 1,0 -22,0z", Brush.linearGradient(0.0f to Color(0xFF2E3496), 1.0f to Color(0xFF5560E0), start = Offset(0.0f, 20.0f), end = Offset(0.0f, 42.0f)), 1.0f)
                fill("M24.5,30.5a7.5,7.5 0 1,0 15.0,0a7.5,7.5 0 1,0 -15.0,0z", Brush.radialGradient(0.0f to Color(0xFFFFFFFF), 0.6f to Color(0xFFE3E7FF), 1.0f to Color(0xFFB7BFF5), center = Offset(29.5f, 27.5f), radius = 10.0f), 1.0f)
                fill("M27.7,27.5a1.8,1.8 0 1,0 3.6,0a1.8,1.8 0 1,0 -3.6,0z", SolidColor(Color(0xFFFFFFFF)), 1.0f)
                line("M15 21 Q32 7 49 21", Color(0xFFFFFFFF), 3.0f, 0.55f)
            }
            DockIcon.OPENING -> {
                fill("M14,59a18,3 0 1,0 36,0a18,3 0 1,0 -36,0z", SolidColor(Color(0xFF000000)), 0.22f)
                line("M32 5v5", Color(0xFFFFB020), 4.2f, 1.0f)
                line("M13 13.5l3.6 3.6", Color(0xFFFFB020), 4.2f, 1.0f)
                line("M51 13.5l-3.6 3.6", Color(0xFFFFB020), 4.2f, 1.0f)
                line("M5.5 30.5h5", Color(0xFFFFB020), 4.2f, 1.0f)
                line("M58.5 30.5h-5", Color(0xFFFFB020), 4.2f, 1.0f)
                group(translationY = 2.0f) { fill("M14,36a18,18 0 0 1 36,0z", SolidColor(Color(0xFFE0701A)), 1.0f) }
                fill("M14,36a18,18 0 0 1 36,0z", Brush.radialGradient(0.0f to Color(0xFFFFF8B8), 0.5f to Color(0xFFFFD23F), 1.0f to Color(0xFFFF8A1E), center = Offset(27.0f, 22.0f), radius = 22.0f), 1.0f)
                line("M21 28a12 12 0 0 1 9-8", Color(0xFFFFFFFF), 3.0f, 0.85f)
                group(translationY = 2.5f) { fill("M13.5,38h37a7.5,7.5 0 0 1 7.5,7.5a7.5,7.5 0 0 1 -7.5,7.5h-37a7.5,7.5 0 0 1 -7.5,-7.5a7.5,7.5 0 0 1 7.5,-7.5z", SolidColor(Color(0xFF137A58)), 1.0f) }
                fill("M13.5,38h37a7.5,7.5 0 0 1 7.5,7.5a7.5,7.5 0 0 1 -7.5,7.5h-37a7.5,7.5 0 0 1 -7.5,-7.5a7.5,7.5 0 0 1 7.5,-7.5z", Brush.linearGradient(0.0f to Color(0xFF7DEBC0), 1.0f to Color(0xFF1B9F74), start = Offset(0.0f, 38.0f), end = Offset(0.0f, 53.0f)), 1.0f)
                line("M13 42h18", Color(0xFFFFFFFF), 2.4f, 0.55f)
                line("M27 48l5-5 5 5", Color(0xFFFFFFFF), 3.0f, 1.0f)
            }
            DockIcon.HOME -> {
                fill("M14,59a18,3 0 1,0 36,0a18,3 0 1,0 -36,0z", SolidColor(Color(0xFF000000)), 0.22f)
                fill("M43.5,12h4.0a1.5,1.5 0 0 1 1.5,1.5v12.0a1.5,1.5 0 0 1 -1.5,1.5h-4.0a1.5,1.5 0 0 1 -1.5,-1.5v-12.0a1.5,1.5 0 0 1 1.5,-1.5z", Brush.linearGradient(0.0f to Color(0xFFE5474E), 1.0f to Color(0xFF8E1820), start = Offset(0.0f, 12.0f), end = Offset(0.0f, 27.0f)), 1.0f)
                group(translationY = 2.0f) { fill("M15,30h34v20a6,6 0 0 1 -6,6h-22a6,6 0 0 1 -6,-6z", SolidColor(Color(0xFFD9BFA6)), 1.0f) }
                fill("M15,30h34v20a6,6 0 0 1 -6,6h-22a6,6 0 0 1 -6,-6z", Brush.linearGradient(0.0f to Color(0xFFFFFFFF), 1.0f to Color(0xFFF3DCC6), start = Offset(0.0f, 30.0f), end = Offset(0.0f, 56.0f)), 1.0f)
                fill("M19.1,37h4.8a1.6,1.6 0 0 1 1.6,1.6v4.8a1.6,1.6 0 0 1 -1.6,1.6h-4.8a1.6,1.6 0 0 1 -1.6,-1.6v-4.8a1.6,1.6 0 0 1 1.6,-1.6z", Brush.linearGradient(0.0f to Color(0xFFCFEFFF), 1.0f to Color(0xFF5AB3F0), start = Offset(0.0f, 37.0f), end = Offset(0.0f, 45.0f)), 1.0f)
                fill("M40.1,37h4.8a1.6,1.6 0 0 1 1.6,1.6v4.8a1.6,1.6 0 0 1 -1.6,1.6h-4.8a1.6,1.6 0 0 1 -1.6,-1.6v-4.8a1.6,1.6 0 0 1 1.6,-1.6z", Brush.linearGradient(0.0f to Color(0xFFCFEFFF), 1.0f to Color(0xFF5AB3F0), start = Offset(0.0f, 37.0f), end = Offset(0.0f, 45.0f)), 1.0f)
                line("M19.5,38.5h2.5", Color(0xFFFFFFFF), 1.4f, 0.9f)
                line("M40.5,38.5h2.5", Color(0xFFFFFFFF), 1.4f, 0.9f)
                fill("M27,56v-11a5,5 0 0 1 10,0v11z", Brush.linearGradient(0.0f to Color(0xFFFFC37A), 1.0f to Color(0xFFD9731F), start = Offset(0.0f, 40.0f), end = Offset(0.0f, 56.0f)), 1.0f)
                fill("M33.3,50a1.2,1.2 0 1,0 2.4,0a1.2,1.2 0 1,0 -2.4,0z", SolidColor(Color(0xFF7A3A10)), 1.0f)
                group(translationY = 2.5f) { fill("M7 31.5 L29 11.5 Q32 9 35 11.5 L57 31.5 Q59.5 34.5 55.5 35 Q53.5 35 52 33.6 L32 16 L12 33.6 Q10.5 35 8.5 35 Q4.5 34.5 7 31.5Z", SolidColor(Color(0xFF7A0F16)), 1.0f) }
                fill("M7 31.5 L29 11.5 Q32 9 35 11.5 L57 31.5 Q59.5 34.5 55.5 35 Q53.5 35 52 33.6 L32 16 L12 33.6 Q10.5 35 8.5 35 Q4.5 34.5 7 31.5Z", Brush.radialGradient(0.0f to Color(0xFFFFA4A8), 0.45f to Color(0xFFE5383B), 1.0f to Color(0xFFA3161F), center = Offset(26.0f, 13.0f), radius = 32.0f), 1.0f)
                line("M14 28 L29.5 14", Color(0xFFFFFFFF), 2.6f, 0.75f)
            }
            DockIcon.CLOSING -> {
                fill("M14,59a16,3 0 1,0 32,0a16,3 0 1,0 -32,0z", SolidColor(Color(0xFF000000)), 0.22f)
                group(translationY = 2.5f) { fill("M38 8 A24 24 0 1 0 56 40 A19 19 0 1 1 38 8Z", SolidColor(Color(0xFF3F2A99)), 1.0f) }
                fill("M38 8 A24 24 0 1 0 56 40 A19 19 0 1 1 38 8Z", Brush.radialGradient(0.0f to Color(0xFFE8E0FF), 0.45f to Color(0xFF9B7EF7), 1.0f to Color(0xFF5233C2), center = Offset(20.0f, 16.0f), radius = 42.0f), 1.0f)
                fill("M16,38a3,3 0 1,0 6,0a3,3 0 1,0 -6,0z", SolidColor(Color(0xFF4B30B8)), 0.3f)
                fill("M24.8,47a2.2,2.2 0 1,0 4.4,0a2.2,2.2 0 1,0 -4.4,0z", SolidColor(Color(0xFF4B30B8)), 0.28f)
                fill("M12.9,28a1.6,1.6 0 1,0 3.2,0a1.6,1.6 0 1,0 -3.2,0z", SolidColor(Color(0xFF4B30B8)), 0.25f)
                line("M22 15 Q13 24 14 36", Color(0xFFFFFFFF), 3.0f, 0.65f)
                fill("M50 6 l2.2 5 5 2.2 -5 2.2 -2.2 5 -2.2 -5 -5 -2.2 5 -2.2z", Brush.linearGradient(0.0f to Color(0xFFFFF4A8), 1.0f to Color(0xFFFFB21E), start = Offset(0.0f, 6.0f), end = Offset(0.0f, 20.0f)), 1.0f)
                fill("M54.8,28a2.2,2.2 0 1,0 4.4,0a2.2,2.2 0 1,0 -4.4,0z", SolidColor(Color(0xFFFFD34D)), 1.0f)
                fill("M42.2,22a1.3,1.3 0 1,0 2.6,0a1.3,1.3 0 1,0 -2.6,0z", SolidColor(Color(0xFFFFE680)), 1.0f)
            }
            DockIcon.PORTAL -> {
                fill("M14,59a18,3 0 1,0 36,0a18,3 0 1,0 -36,0z", SolidColor(Color(0xFF000000)), 0.22f)
                group(translationY = 2.5f) { fill("M8,31a24,24 0 1,0 48,0a24,24 0 1,0 -48,0z", SolidColor(Color(0xFF164F9E)), 1.0f) }
                fill("M8,31a24,24 0 1,0 48,0a24,24 0 1,0 -48,0z", Brush.radialGradient(0.0f to Color(0xFFC4ECFF), 0.55f to Color(0xFF3C9CF0), 1.0f to Color(0xFF1A5FC0), center = Offset(24.0f, 20.0f), radius = 34.0f), 1.0f)
                group(clipPathData = nodes("M8,31a24,24 0 1,0 48,0a24,24 0 1,0 -48,0z")) {
                    fill("M14 17 q8-4 13 1 q3 5-2 8 q-5 1-6 6 q-1 5-7 4 q-6-6 2-19z", Brush.linearGradient(0.0f to Color(0xFFA6F796), 1.0f to Color(0xFF2FAE3C), start = Offset(0.0f, 15.0f), end = Offset(0.0f, 37.0f)), 1.0f)
                    fill("M36 22 q7-3 12 2 q5 4 1 9 q-4 2-6 7 q-2 6-7 4 q-3-5 0-10 q-5-6 0-12z", Brush.linearGradient(0.0f to Color(0xFFA6F796), 1.0f to Color(0xFF2FAE3C), start = Offset(0.0f, 20.0f), end = Offset(0.0f, 45.0f)), 1.0f)
                    fill("M22 46 q5-2 9 2 q-1 6-8 6 q-4-3-1-8z", Brush.linearGradient(0.0f to Color(0xFFA6F796), 1.0f to Color(0xFF2FAE3C), start = Offset(0.0f, 45.0f), end = Offset(0.0f, 55.0f)), 1.0f)
                    line("M21,31a11,24 0 1,0 22,0a11,24 0 1,0 -22,0z", Color(0xFFFFFFFF), 1.6f, 0.35f)
                    line("M8 31h48M11 19h42M11 43h42", Color(0xFFFFFFFF), 1.6f, 0.35f)
                }
                group(rotate = -30.0f, pivotX = 23.0f, pivotY = 19.0f) { fill("M15,19a8,4.5 0 1,0 16,0a8,4.5 0 1,0 -16,0z", SolidColor(Color(0xFFFFFFFF)), 0.5f) }
            }
        }
    }.build()
