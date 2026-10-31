package com.metroplus.app.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.metroplus.app.ui.theme.AppFontFamily
import androidx.compose.ui.unit.sp
import com.metroplus.app.R
import com.metroplus.app.ui.theme.HomeBackground
import com.metroplus.app.ui.theme.Ink
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

// Home GUI — trial 2.1.63 (Easypaisa theme + v2.1.x overrides) ki EXACT copy, native Compose me.
// Har size/rang trial ke computed CSS se naapa gaya hai (375px viewport = dp):
// header 74, search 44 + refresh 48, banner 192 (kona 26, padding 24), minis 160 (kona 20),
// hubs 94 (kona 22), section titles 17sp. Sirf design hai, koi feature/Firebase nahi.

private val Coral = Color(0xFF9E1F25)
private val HubInk = Color(0xFF1D1416)

// CSS `linear-gradient(<angle>deg, ...)` bilkul usi hisaab se (kisi bhi box size pe).
internal fun cssLinear(angleDeg: Float, vararg stops: Pair<Float, Color>): Brush = object : ShaderBrush() {
    override fun createShader(size: Size): Shader {
        val rad = Math.toRadians(angleDeg.toDouble())
        val dx = sin(rad).toFloat()
        val dy = -cos(rad).toFloat()
        val half = (abs(size.width * dx) + abs(size.height * dy)) / 2f
        val c = Offset(size.width / 2f, size.height / 2f)
        return LinearGradientShader(
            from = c - Offset(dx * half, dy * half), to = c + Offset(dx * half, dy * half),
            colors = stops.map { it.second }, colorStops = stops.map { it.first }
        )
    }
}

// Trial ka ::before glow (radial white → transparent 70%) aur ::after halka circle.
@Composable
internal fun BoxScope.Glow(size: Dp, x: Dp, y: Dp, alpha: Float, solid: Boolean = false, align: Alignment = Alignment.TopStart) {
    val brush = if (solid) Brush.radialGradient(listOf(Color.White.copy(alpha = alpha), Color.White.copy(alpha = alpha)))
    else Brush.radialGradient(0f to Color.White.copy(alpha = alpha), 0.7f to Color.White.copy(alpha = 0f))
    Box(modifier = Modifier.align(align).offset(x = x, y = y).size(size).background(brush, CircleShape))
}

// Trial ka `.mp-wm` watermark: logo_wm, -14° ghooma hua, halka.
@Composable
internal fun BoxScope.Watermark(size: Dp, x: Dp, y: Dp, alpha: Float) {
    Image(
        painterResource(id = R.drawable.logo_wm), contentDescription = null, contentScale = ContentScale.Fit,
        modifier = Modifier.align(Alignment.TopStart).offset(x = x, y = y).size(size).rotate(-14f).alpha(alpha)
    )
}

// CSS box-shadow jaisa rangeen saya (Android 9+ pe rang, purane pe grey).
internal fun Modifier.cssShadow(elevation: Dp, shape: Shape, color: Color): Modifier =
    shadow(elevation = elevation, shape = shape, ambientColor = color, spotColor = color)

// accent = us section ka rang (v1.56 rang-nizaam) — kicker, Open button, arrow tile.
private data class Banner(
    val kicker: String, val title: String, val sub: String, val imageRes: Int,
    val stops: Array<Pair<Float, Color>>, val shadow: Color, val isWelcome: Boolean = false,
    val accent: Color = Color.Unspecified
)
private data class MiniCard(
    val kicker: String, val title: String, val sub: String, val label: String, val imageRes: Int,
    val stops: Array<Pair<Float, Color>>, val shadow: Color, val accent: Color
)
// v1.58: GPS teal (Portal pehle se neela hai — do neele ek jaise lagte the).
private val GpsBlue = Color(0xFF0891B2)

// v1.60 (uska order "colors pyare karo"): v1.58 ke halke ek-rang dabbe phike lagte
// the. Ab har section ka 3-rang (hue badalta) gradient — halka → beech → gehra:
// Money peela→narangi, Tom aadoo→coral→gulabi, GPS aqua→neela, Complaints
// lavender→jamni→gulabi, Portal neela→indigo, Opening sunehra→narangi, Closing
// lavender→jamni. Banners sab se halke, minis sab se rangeen, hubs beech me.
private fun vivid(c: Color): Triple<Color, Color, Color> = when (c) {
    Section.Money -> Triple(Color(0xFFFFF1A8), Color(0xFFFFC94D), Color(0xFFFF8F3D))
    Section.Tom -> Triple(Color(0xFFFFE0CF), Color(0xFFFF9A76), Color(0xFFFF5F86))
    GpsBlue -> Triple(Color(0xFFD2F6FF), Color(0xFF63D2F0), Color(0xFF3A7BEF))
    Section.Complaints -> Triple(Color(0xFFEDE3FF), Color(0xFFB794F6), Color(0xFFE56FCF))
    Section.Portal -> Triple(Color(0xFFDCEBFF), Color(0xFF8DB7FF), Color(0xFF7A6CF0))
    Section.Opening -> Triple(Color(0xFFFFF0C2), Color(0xFFFFC266), Color(0xFFFF8457))
    Section.Closing -> Triple(Color(0xFFEFE6FF), Color(0xFFBBA4FF), Color(0xFFC07CF5))
    else -> Triple(c.lighter(0.85f), c.lighter(0.5f), c)
}
private fun bannerStops(c: Color) = vivid(c).let { (l, m, d) -> arrayOf(0f to l.lighter(0.55f), 0.6f to l, 1f to m.lighter(0.25f)) }
private fun miniStops(c: Color) = vivid(c).let { (l, m, d) -> arrayOf(0f to l, 0.55f to m, 1f to d) }
private fun hubStops(c: Color) = vivid(c).let { (l, m, d) -> arrayOf(0f to l.lighter(0.4f), 0.6f to l, 1f to m.lighter(0.1f)) }
private data class Hub(
    val tag: String, val tagColor: Color, val title: String, val sub: String,
    val iconRes: Int, val stops: Array<Pair<Float, Color>>
)


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onProfileClick: () -> Unit,
    onChatClick: () -> Unit,
    onFeatureClick: (String) -> Unit
) {
    // LIVE/OFFLINE badge (purani trial app jaisa): internet ho to green LIVE,
    // na ho to red OFFLINE. NetworkCallback se live update hota hai.
    val appContext = LocalContext.current.applicationContext
    // Kabhi crash nahi: permission ya system ki koi bhi problem ho to sirf OFFLINE samjho.
    fun currentOnline(): Boolean {
        return try {
            val cm = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val net = cm.activeNetwork ?: return false
            val caps = cm.getNetworkCapabilities(net) ?: return false
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } catch (_: Exception) { false }
    }
    var isOnline by remember { mutableStateOf(currentOnline()) }
    DisposableEffect(Unit) {
        val cm = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val cb = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) { isOnline = true }
            override fun onLost(network: Network) { isOnline = currentOnline() }
            override fun onUnavailable() { isOnline = false }
        }
        try { cm.registerDefaultNetworkCallback(cb) } catch (_: Exception) { }
        onDispose { try { cm.unregisterNetworkCallback(cb) } catch (_: Exception) { } }
    }
    // App wapas foreground me aaye to shift/date dobara (12 baje ke paar purana shift na rahe).
    @Suppress("UNUSED_VARIABLE") val resumeTick = com.metroplus.app.ForegroundTicker.resumeCount.value
    val now = Calendar.getInstance()
    val shift = if (now.get(Calendar.HOUR_OF_DAY) < 12) "Morning Shift" else "Evening Shift"
    val dateText = SimpleDateFormat("EEEE, d MMMM", Locale.ENGLISH).format(now.time)

    val banners = remember {
        listOf(
            Banner("", "", "", 0, arrayOf(), Color(0xFF9E1F25).copy(alpha = 0.35f), isWelcome = true),
            Banner("FARES & FINANCE", "Money Calculator", "Tap to calculate", R.drawable.img_money, bannerStops(Section.Money), Section.Money.copy(alpha = 0.22f), accent = Section.Money),
            Banner("DAILY REGISTER", "Tom Data", "Tap to open register", R.drawable.img_tom, bannerStops(Section.Tom), Section.Tom.copy(alpha = 0.22f), accent = Section.Tom),
            Banner("PHOTOS & MAP", "GPS Camera", "Tap to open camera", R.drawable.img_gps, bannerStops(GpsBlue), GpsBlue.copy(alpha = 0.22f), accent = GpsBlue),
            Banner("ONLINE PORTAL", "Online Portal", "Tap to open portal", R.drawable.img_portal, bannerStops(Section.Portal), Section.Portal.copy(alpha = 0.22f), accent = Section.Portal)
        )
    }
    // More with Metro: banners se thore gehre (uska purana "dark" order wahi), lekin ab
    // har tile apne section rang se — chaaron ek parivar jaise.
    val minis = remember {
        listOf(
            MiniCard("PHOTOS & MAP", "GPS Camera", "Tap to open camera", "gps camera map location", R.drawable.img_gps, miniStops(GpsBlue), GpsBlue.copy(alpha = 0.25f), GpsBlue),
            MiniCard("FARES & FINANCE", "Money Calc", "Tap to calculate", "money calc fares finance cash", R.drawable.img_money, miniStops(Section.Money), Section.Money.copy(alpha = 0.25f), Section.Money),
            MiniCard("REPORTS", "Complaints", "Tap to open complaints", "complaints tom agm tkm reports", R.drawable.img_complaint, miniStops(Section.Complaints), Section.Complaints.copy(alpha = 0.25f), Section.Complaints),
            MiniCard("DAILY REGISTER", "Tom Data", "Tap to open register", "tom data register sales", R.drawable.img_tom, miniStops(Section.Tom), Section.Tom.copy(alpha = 0.25f), Section.Tom)
        )
    }
    val hubs = remember {
        listOf(
            // Settings hub khatam (uska order) — sirf pehle 3 hubs rahenge.
            // Baaki 3 ke colors waazeh dark (halka step nazar nahi aata tha).
            // v1.57: tag + arrow tile us section ke rang me.
            Hub("START SHIFT", Section.Opening, "Opening", "Opening data, staff & GPS", R.drawable.ic_dock_opening, hubStops(Section.Opening)),
            Hub("END SHIFT", Section.Closing, "Closing", "End-of-shift reports", R.drawable.ic_dock_closing, hubStops(Section.Closing)),
            Hub("ONLINE", Section.Portal, "Online Portal", "Central online portal", R.drawable.ic_dock_portal, hubStops(Section.Portal))
        )
    }
    val pagerState = rememberPagerState(pageCount = { banners.size })
    // Batch item 2 (31 Oct 2026, uska order): banner auto-slide FILHAL BAND —
    // manual swipe hi chalegi. Jab usko chahiye hogi tab ye loop wapas lagega.
    // (Pehle: har 5s agla banner, aakhri ke baad pehla — wahi backward slide thi.)
    // Trial jaisa search hata diya gaya hai (batch item 3) — saare mini cards dikhte hain.
    val shownMinis = minis

    // v1.57: plain #F3EDED ki jagah upar safed → neeche halka gulabi gradient.
    LazyColumn(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFFBF8F8), HomeBackground, Color(0xFFF1E3E4))))) {
        // Header: white, padding 14/20, 46dp logo, "Hey There" 17sp, LIVE badge + chat button (46, kona 16).
        item {
            Surface(modifier = Modifier.fillMaxWidth(), color = Color.White, shadowElevation = 3.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = Modifier.weight(1f).clickable { onProfileClick() }, verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_hd),
                            contentDescription = "Metro M logo", contentScale = ContentScale.Fit,
                            // v1.59: asal logo wahi, sirf rang/contrast enhance + thora bara (46 → 50).
                            colorFilter = LogoEnhance,
                            modifier = Modifier.size(50.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Hi Aryan 👋", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Ink, letterSpacing = (-0.2).sp)
                    }
                    // Trial: LIVE #00E065 / OFFLINE #D43D2A — bg = rang 11%, border = rang 28%.
                    val live = if (isOnline) Color(0xFF00E065) else Color(0xFFD43D2A)
                    Row(
                        modifier = Modifier.background(live.copy(alpha = 0.11f), RoundedCornerShape(999.dp))
                            .border(1.dp, live.copy(alpha = 0.28f), RoundedCornerShape(999.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(8.dp).background(live, CircleShape))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isOnline) "LIVE" else "OFFLINE", fontSize = 10.5.sp, fontWeight = FontWeight.ExtraBold, color = live, letterSpacing = 0.8.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    // v1.65 (uska order "message icon aur pyara"): sabz → teal 3D dabba,
                    // upar shishe ki chamak, andar safed chat bubbles, aur kone pe laal
                    // "3" badge (GUI demo — asli unread count chat wiring ke sath).
                    val chatShape = RoundedCornerShape(16.dp)
                    Box(modifier = Modifier.size(50.dp)) {
                        Box(
                            modifier = Modifier.align(Alignment.BottomStart).size(46.dp)
                                .cssShadow(10.dp, chatShape, Color(0xFF059669).copy(alpha = 0.45f)).clip(chatShape)
                                .background(cssLinear(145f, 0f to Color(0xFF6EE7B7), 0.5f to Color(0xFF10B981), 1f to Color(0xFF0F766E)))
                                .drawBehind { drawRect(Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.45f), Color.White.copy(alpha = 0f)), endY = size.height * 0.55f)) }
                                .border(1.dp, Color.White.copy(alpha = 0.45f), chatShape)
                                .clickable { onChatClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Forum, contentDescription = "Group Chat", tint = Color.White, modifier = Modifier.size(24.dp))
                        }
                        Box(
                            modifier = Modifier.align(Alignment.TopEnd).size(19.dp)
                                .cssShadow(3.dp, CircleShape, Color(0xFFE11D48).copy(alpha = 0.5f))
                                .background(Brush.linearGradient(listOf(Color(0xFFFF6B6B), Color(0xFFE11D48))), CircleShape)
                                .border(2.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) { Text("3", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Color.White) }
                    }
                }
            }
        }
        // Batch item 3 (31 Oct 2026, uska order): search pill + refresh circle
        // dono hata diye gaye hain — taake cards upar aa jayen. Manual update
        // check ab bhi har app open pe 5 second baad khud hota hai.
        // Banner carousel (trial CSS): card = (screen - 40) ka 88%, gap 12; pehla card trial
        // jaisa left se 20dp (deewar ke saath) — centered padding se pehla card 39dp andar
        // ho jata tha, is liye contentPadding 20dp hai; side cards 0.92 + 75% opacity.
        // Unchai trial jaisi: sab cards sab se lambe card jitne (2-line "Money Calculator"), kam se kam 182 —
        // fixed 192 se "Open" button kat jata tha.
        item {
            val measurer = rememberTextMeasurer()
            val density = LocalDensity.current
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val pageWidth = (maxWidth - 40.dp) * 0.88f
                // Title ki asal chaurai naap ke: koi lafz column se bada ho to font chhota (25 → 17),
                // phir sab se zyada lines wale title se sab cards ki unchai — "Open" kabhi na kate
                // (chhote phone / bada system font pe bhi).
                val titleCol = pageWidth - 48.dp - 108.dp - 4.dp
                val fits = remember(titleCol, density) { banners.map { fitBannerTitle(measurer, it.title, titleCol, density) } }
                val bannerH = with(density) {
                    val titleH = fits.maxOf { (size, lines) -> size.toPx() * 1.1f * lines }.toDp()
                    maxOf(182.dp, 88.dp + 54.sp.toDp() + titleH + 6.dp)
                }
                HorizontalPager(
                    state = pagerState,
                    pageSize = PageSize.Fixed(pageWidth),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 0.dp),
                    pageSpacing = 12.dp,
                    modifier = Modifier.padding(top = 6.dp, bottom = 18.dp)
                ) { page ->
                    val b = banners[page]
                    val dist = abs((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).coerceIn(0f, 1f)
                    val side = if (dist > 0.35f) 1f else 0f
                    Box(
                        modifier = Modifier.graphicsLayer {
                            val s = 1f - 0.08f * side
                            scaleX = s; scaleY = s; alpha = 1f - 0.25f * side
                        }
                    ) {
                        if (b.isWelcome) WelcomeBanner(shift, dateText, b.shadow, bannerH)
                        else ColorBanner(b, bannerH, fits[page].first) { onFeatureClick(b.title) }
                    }
                }
            }
            // Dots: 7dp, active 22dp laal #B32B32.
            Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 18.dp), horizontalArrangement = Arrangement.Center) {
                banners.indices.forEach { i ->
                    val on = pagerState.currentPage == i
                    Box(
                        modifier = Modifier.padding(horizontal = 3.dp).size(width = if (on) 22.dp else 7.dp, height = 7.dp)
                            .clip(CircleShape).background(if (on) Color(0xFFB32B32) else Ink.copy(alpha = 0.15f))
                    )
                }
            }
        }
        // More with Metro — 2 column minis (gap 12), min 160 unche.
        item {
            SectionTitle("More with Metro", top = 0.dp)
            Column(modifier = Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                for (row in shownMinis.chunked(2)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        row.forEach { m -> MiniTile(m, Modifier.weight(1f)) { onFeatureClick(m.title) } }
                        if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        // Hubs — 4 wide cards (94 unche, gap 12).
        item {
            SectionTitle("Hubs", top = 15.dp)
            Column(modifier = Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                hubs.forEach { h -> HubCard(h) { onFeatureClick(h.title) } }
            }
            // Dock (70 + 12 neeche) ke liye jagah.
            Spacer(modifier = Modifier.navigationBarsPadding().height(110.dp))
        }
    }
}

// Banner title ka font (25sp se shuru) aur lines: har lafz column me poora aaye.
private fun fitBannerTitle(m: TextMeasurer, title: String, width: Dp, d: Density): Pair<TextUnit, Int> {
    if (title.isEmpty()) return 25.sp to 1
    val maxPx = with(d) { width.roundToPx() }
    fun style(size: Float) = TextStyle(fontFamily = AppFontFamily, fontSize = size.sp, fontWeight = FontWeight.ExtraBold,
        letterSpacing = (-0.4).sp, lineHeight = (size * 1.1f).sp)
    var size = 25f
    while (size > 17f && title.split(" ").maxOf { m.measure(it, style(size), maxLines = 1, softWrap = false).size.width } > maxPx) size -= 1f
    val lines = m.measure(title, style(size), constraints = Constraints(maxWidth = maxPx)).lineCount
    return size.sp to lines
}

@Composable
private fun SectionTitle(text: String, top: Dp) {
    // v1.57: title se pehle chhoti laal gradient patti.
    Row(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = top, bottom = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(width = 5.dp, height = 18.dp).background(Brush.verticalGradient(listOf(BrandRedLight, BrandRedDark)), RoundedCornerShape(3.dp)))
        Spacer(modifier = Modifier.width(9.dp))
        Text(text, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Ink, letterSpacing = (-0.2).sp)
    }
}

// Welcome banner: laal 4-stop gradient + 2 radial chamak, "DEAR" + naam, station, chips, role badge.
@Composable
private fun WelcomeBanner(shift: String, dateText: String, shadowColor: Color, height: Dp) {
    val shape = RoundedCornerShape(26.dp)
    Box(
        modifier = Modifier.fillMaxWidth().height(height)
            .cssShadow(16.dp, shape, shadowColor).clip(shape)
            .background(cssLinear(135f, 0f to Color(0xFFFF5A5F), 0.35f to Color(0xFFE5383B), 0.62f to Color(0xFFC1121F), 1f to Color(0xFF8A0F3A)))
            .drawBehind {
                // radial-gradient(120% 90% at 0% 0%, rgba(255,138,101,.55) → 0 at 45%)
                val ry = size.height * 0.9f
                scale(scaleX = size.width * 1.2f / ry, scaleY = 1f, pivot = Offset.Zero) {
                    drawRect(Brush.radialGradient(0f to Color(0xFFFF8A65).copy(alpha = 0.55f), 0.45f to Color(0x00FF8A65), center = Offset.Zero, radius = ry))
                }
                // radial-gradient(90% 80% at 100% 100%, rgba(120,10,70,.55) → 0 at 55%)
                val ry2 = size.height * 0.8f
                scale(scaleX = size.width * 0.9f / ry2, scaleY = 1f, pivot = Offset(size.width, size.height)) {
                    drawRect(Brush.radialGradient(0f to Color(0xFF780A46).copy(alpha = 0.55f), 0.55f to Color(0x00780A46), center = Offset(size.width, size.height), radius = ry2))
                }
            }
    ) {
        Glow(190.dp, x = 60.dp, y = (-70).dp, alpha = 0.35f, align = Alignment.TopEnd)
        Glow(120.dp, x = (-40).dp, y = 60.dp, alpha = 0.08f, solid = true, align = Alignment.BottomStart)
        // User ka mascot (watermark ki jagah): daein taraf, role badge ke neeche se card ke neeche tak.
        Image(
            painterResource(id = R.drawable.metro_plus_welcome), contentDescription = "Metro + mascot", contentScale = ContentScale.Fit,
            modifier = Modifier.align(Alignment.BottomEnd).padding(end = 6.dp).height(height - 34.dp).width((height - 34.dp) * 0.667f)
        )
        // Role badge (upar-daein 12/12).
        Text(
            "Operator", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF7A171C), letterSpacing = 1.sp,
            modifier = Modifier.align(Alignment.TopEnd).padding(top = 12.dp, end = 12.dp)
                .cssShadow(2.dp, RoundedCornerShape(20.dp), Color(0xFFAA1F26).copy(alpha = 0.15f))
                .background(Color.White.copy(alpha = 0.95f), RoundedCornerShape(20.dp))
                .padding(horizontal = 13.dp, vertical = 3.dp)
        )
        // Likhai mascot ke baein taraf (mascot ki chaurai jitna daein padding).
        Column(modifier = Modifier.align(Alignment.CenterStart).padding(start = 24.dp, end = (height - 34.dp) * 0.667f)) {
            Text("DEAR", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFECEE).copy(alpha = 0.9f), letterSpacing = 2.sp, lineHeight = 15.sp)
            // GUI demo naam; Login step me asli profile se aayega.
            Text("Aryan", fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, letterSpacing = (-0.4).sp, lineHeight = 27.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.LocationOn, contentDescription = null, tint = Color(0xFFFFBFC2), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Shaheed-e-Millat", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, maxLines = 1, softWrap = false, letterSpacing = (-0.3).sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Item 1 (30 Oct 2026): shift chip upar, date chip uske NEECHE apni row me
            // (pehle dono ek hi row me barabar me the).
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                WelcomeChip(shift)
                WelcomeChip(dateText)
            }
            Spacer(modifier = Modifier.height(9.dp))
            Text("Welcome to the Metro + app", fontSize = 12.5.sp, fontWeight = FontWeight.Medium, color = Color.White.copy(alpha = 0.78f), letterSpacing = 0.3.sp)
        }
    }
}

@Composable
private fun WelcomeChip(text: String) {
    Text(
        text, fontSize = 11.5.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, maxLines = 1,
        modifier = Modifier.background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
            .border(1.dp, Color.White.copy(alpha = 0.28f), RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 3.dp)
    )
}

// Rangeen banner: kicker pill, 25sp title, sub, kala "Open" button, daein 106dp art circle.
@Composable
private fun ColorBanner(b: Banner, height: Dp, titleSize: TextUnit, onClick: () -> Unit) {
    val shape = RoundedCornerShape(26.dp)
    Box(
        modifier = Modifier.fillMaxWidth().height(height)
            .cssShadow(12.dp, shape, b.shadow).clip(shape)
            .background(cssLinear(135f, *b.stops))
            .clickable { onClick() }
    ) {
        Glow(190.dp, x = 60.dp, y = (-70).dp, alpha = 0.55f, align = Alignment.TopEnd)
        Glow(120.dp, x = (-40).dp, y = 60.dp, alpha = 0.22f, solid = true, align = Alignment.BottomStart)
        Watermark(165.dp, x = 151.dp, y = 14.dp, alpha = 0.2f)
        Row(modifier = Modifier.fillMaxSize().padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    b.kicker, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = b.accent.darker(0.3f), letterSpacing = 1.2.sp,
                    modifier = Modifier.background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(20.dp)).padding(horizontal = 9.dp, vertical = 2.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(b.title, fontSize = titleSize, fontWeight = FontWeight.ExtraBold, color = Ink, letterSpacing = (-0.4).sp, lineHeight = (titleSize.value * 1.1f).sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(b.sub, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Ink.copy(alpha = 0.6f))
                Spacer(modifier = Modifier.height(12.dp))
                // v1.57: kala "Open" ab section ke rang ka 3D pill (upar chamak).
                val openShape = RoundedCornerShape(20.dp)
                Row(
                    modifier = Modifier.cssShadow(6.dp, openShape, b.accent.copy(alpha = 0.45f)).clip(openShape)
                        .background(Brush.verticalGradient(listOf(b.accent.lighter(0.12f), b.accent.darker(0.18f))))
                        .drawBehind { drawRect(Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.3f), Color.Transparent), endY = size.height / 2)) }
                        .padding(horizontal = 13.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Open", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                }
            }
            Spacer(modifier = Modifier.width(4.dp))
            // Trial: `.ep-b-art { width/height: 108px !important }`.
            Box(
                modifier = Modifier.size(108.dp)
                    .cssShadow(10.dp, CircleShape, Color.Black.copy(alpha = 0.12f))
                    .background(Color.White.copy(alpha = 0.55f), CircleShape)
                    .border(2.7.dp, Color.White.copy(alpha = 0.7f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(painterResource(id = b.imageRes), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.size(96.dp).clip(CircleShape))
            }
        }
    }
}

// Mini card: gradient, glow, upar-daein 28dp arrow circle, 62dp gol image, kicker/title/sub.
@Composable
private fun MiniTile(m: MiniCard, modifier: Modifier, onClick: () -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Box(
        // Item 3: chaaron dabbe barabar — fixed height (pehle sirf minHeight
        // 160dp tha, sub-text 2 line hone par tile lambi ho jati thi).
        modifier = modifier.height(184.dp)
            .cssShadow(12.dp, shape, m.shadow).clip(shape)
            .background(cssLinear(135f, *m.stops))
            .clickable { onClick() }
    ) {
        Glow(130.dp, x = 50.dp, y = (-50).dp, alpha = 0.6f, align = Alignment.TopEnd)
        Watermark(98.dp, x = 90.dp, y = 32.dp, alpha = 0.075f)
        // v1.57: safed arrow gola ab section rang ka 3D tile.
        Box(modifier = Modifier.align(Alignment.TopEnd).padding(top = 12.dp, end = 12.dp)) {
            Icon3DTile(Icons.Filled.ArrowForward, m.accent, size = 30.dp, iconSize = 14.dp, corner = 10.dp)
        }
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp)) {
            Image(
                painterResource(id = m.imageRes), contentDescription = null, contentScale = ContentScale.Crop,
                modifier = Modifier.size(62.dp).cssShadow(4.dp, CircleShape, Ink.copy(alpha = 0.14f)).background(Color.White, CircleShape).clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(m.kicker, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = m.accent.darker(0.35f), letterSpacing = 1.1.sp)
            Spacer(modifier = Modifier.height(3.dp))
            Text(m.title, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Ink, letterSpacing = (-0.3).sp)
            Text(m.sub, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Ink.copy(alpha = 0.6f), lineHeight = 18.sp)
        }
    }
}

// Hub card: 94 unche, 60dp 3D icon (bina dabbe), tag pill, 18sp title, sub, 32dp white arrow circle.
@Composable
private fun HubCard(h: Hub, onClick: () -> Unit) {
    val shape = RoundedCornerShape(22.dp)
    Box(
        modifier = Modifier.fillMaxWidth().height(94.dp)
            .cssShadow(8.dp, shape, Color(0xFF3C1419).copy(alpha = 0.08f)).clip(shape)
            .background(cssLinear(120f, *h.stops))
            .border(1.dp, Color.White.copy(alpha = 0.7f), shape)
            .clickable { onClick() }
    ) {
        Glow(170.dp, x = (-30).dp, y = (-60).dp, alpha = 0.85f)
        Glow(150.dp, x = 224.dp, y = 13.dp, alpha = 0.35f, solid = true)
        Watermark(143.dp, x = 230.dp, y = (-24).dp, alpha = 0.075f)
        Image(
            painterResource(id = h.iconRes), contentDescription = null, contentScale = ContentScale.Fit,
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 14.dp).size(60.dp)
        )
        Column(modifier = Modifier.align(Alignment.CenterStart).padding(start = 90.dp, end = 58.dp)) {
            Text(
                h.tag, fontSize = 9.5.sp, fontWeight = FontWeight.ExtraBold, color = h.tagColor, letterSpacing = 1.sp,
                modifier = Modifier.background(Color.White.copy(alpha = 0.75f), RoundedCornerShape(20.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(h.title, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = HubInk, letterSpacing = (-0.3).sp, lineHeight = 22.sp)
            Text(h.sub, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = HubInk.copy(alpha = 0.62f))
        }
        // v1.57: arrow ab section rang ka 3D tile.
        Box(modifier = Modifier.align(Alignment.CenterEnd).padding(end = 14.dp)) {
            Icon3DTile(Icons.Filled.ArrowForward, h.tagColor, size = 34.dp, iconSize = 16.dp, corner = 11.dp)
        }
    }
}
