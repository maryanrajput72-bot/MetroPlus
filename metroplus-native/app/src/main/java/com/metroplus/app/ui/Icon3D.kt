package com.metroplus.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// App ka rang-nizaam (uska order, v1.56):
// - Brand laal sirf do shades: BrandRed #B02A31 (asal) + BrandRedDark #9E1F25
//   (gehra); gradient ke upar ke liye BrandRedLight, neeche ke liye BrandRedDeep.
// - Har section ka apna pakka rang — us page ke accents (buttons, chips,
//   toggles) usi rang me, taake rang dekh ke pata chale kis page pe ho.
val BrandRedLight = Color(0xFFE0454D)
val BrandRed = Color(0xFFB02A31)
val BrandRedDark = Color(0xFF9E1F25)
val BrandRedDeep = Color(0xFF7A1A1F)

object Section {
    val Opening = Color(0xFFEA580C)   // narangi / amber
    val Closing = Color(0xFF6D28D9)   // purple
    val Money = Color(0xFFE29A0C)     // sunehra (B7791F bhoora lagta tha)
    val Tom = Color(0xFFE8590C)       // aadoo (peach) ka gehra
    val Portal = Color(0xFF1D6FD8)    // neela
    val Complaints = Color(0xFF7C3AED) // violet
    val Settings = Color(0xFF16A34A)  // sabz
}

// Asal M logo (logo_hd) wahi rehta hai, sirf enhance (uska order v1.59): rang
// ~25% zyada gehre (saturation), thora zyada contrast aur halki si roshni — laal
// zyada chamakdaar aur kala zyada gehra lagta hai. Design me koi badlaav nahi.
val LogoEnhance: ColorFilter = run {
    val sat = ColorMatrix().apply { setToSaturation(1.28f) }
    val c = 1.12f
    val t = (1f - c) * 128f + 10f
    sat.timesAssign(ColorMatrix(floatArrayOf(c, 0f, 0f, 0f, t, 0f, c, 0f, 0f, t, 0f, 0f, c, 0f, t, 0f, 0f, 0f, 1f, 0f)))
    ColorFilter.colorMatrix(sat)
}

// v1.59: logos pe chalti chamak (Modifier.shine) uske kehne pe hata di —
// naye vector logos me chamak unke andar hi bani hui hai (bina animation).

// Rang ka halka (upar) aur gehra (neeche) roop — 3D tile ke gradient ke liye.
fun Color.lighter(f: Float = 0.32f): Color = lerp(this, Color.White, f)
fun Color.darker(f: Float = 0.22f): Color = lerp(this, Color.Black, f)

// 3D icon tile (dock ke icons jaisa ehsaas): squircle me upar halka → neeche
// gehra gradient, upar aadhe me safed chamak, andar patli safed kinari, aur
// tile ke apne rang ka saya. Icon safed.
@Composable
fun Icon3DTile(
    icon: ImageVector,
    color: Color,
    size: Dp = 38.dp,
    iconSize: Dp = 20.dp,
    corner: Dp = 12.dp,
    colors: List<Color>? = null
) {
    val shape = RoundedCornerShape(corner)
    val stops = colors ?: listOf(color.lighter(), color.darker())
    Box(
        modifier = Modifier.size(size)
            .shadow(5.dp, shape, ambientColor = stops.last(), spotColor = stops.last())
            .clip(shape)
            .background(Brush.linearGradient(stops))
            .border(1.dp, Color.White.copy(alpha = 0.28f), shape),
        contentAlignment = Alignment.Center
    ) {
        // Upar wali chamak (shisha jaisi).
        Box(
            modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth().fillMaxHeight(0.5f)
                .background(Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.38f), Color.White.copy(alpha = 0f))))
        )
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(iconSize))
    }
}
