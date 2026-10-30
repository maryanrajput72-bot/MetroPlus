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
import androidx.compose.foundation.interaction.collectIsDraggedAsState
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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

private data class Banner(
    val kicker: String, val title: String, val sub: String, val imageRes: Int,
    val stops: Array<Pair<Float, Color>>, val shadow: Color, val isWelcome: Boolean = false
)
private data class MiniCard(
    val kicker: String, val title: String, val sub: String, val label: String, val imageRes: Int,
    val stops: Array<Pair<Float, Color>>, val shadow: Color
)
private data class Hub(
    val tag: String, val tagColor: Color, val title: String, val sub: String,
    val iconRes: Int, val stops: Array<Pair<Float, Color>>
)

// Trial ke gradients (html[data-theme] .ep-b-* overrides).
private val YellowStops = arrayOf(0f to Color(0xFFFFF8D9), 0.6f to Color(0xFFFFE795), 1f to Color(0xFFFFD666))
private val PeachStops = arrayOf(0f to Color(0xFFFFF3E8), 0.6f to Color(0xFFFFDCC0), 1f to Color(0xFFFFC59B))
private val BlueStops = arrayOf(0f to Color(0xFFEAF3FF), 0.6f to Color(0xFFCFE2FF), 1f to Color(0xFFB0CDFF))
private val PurpleStops = arrayOf(0f to Color(0xFFF4EDFF), 0.6f to Color(0xFFE1D1FF), 1f to Color(0xFFCBB2FF))
private val YellowShadow = Color(0xFFE6AA00).copy(alpha = 0.2f)
private val PeachShadow = Color(0xFFEA6E28).copy(alpha = 0.18f)
private val BlueShadow = Color(0xFF3B6EE6).copy(alpha = 0.18f)
private val PurpleShadow = Color(0xFF7C3AED).copy(alpha = 0.18f)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onProfileClick: () -> Unit,
    onChatClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onFeatureClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
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
    val now = Calendar.getInstance()
    val shift = if (now.get(Calendar.HOUR_OF_DAY) < 12) "Morning Shift" else "Evening Shift"
    val dateText = SimpleDateFormat("EEEE, d MMMM", Locale.ENGLISH).format(now.time)

    val banners = remember {
        listOf(
            Banner("", "", "", 0, arrayOf(), Color(0xFF9E1F25).copy(alpha = 0.35f), isWelcome = true),
            Banner("FARES & FINANCE", "Money Calculator", "Tap to calculate", R.drawable.img_money, YellowStops, YellowShadow),
            Banner("DAILY REGISTER", "Tom Data", "Tap to open register", R.drawable.img_tom, PeachStops, PeachShadow),
            Banner("PHOTOS & MAP", "GPS Camera", "Tap to open camera", R.drawable.img_gps, BlueStops, BlueShadow),
            Banner("ONLINE PORTAL", "Online Portal", "Tap to open portal", R.drawable.img_portal, PurpleStops, PurpleShadow)
        )
    }
// More with Metro tiles ke liye thore dark stops (uska order) — upar wale
// banners inhi purane stops pe rahenge, sirf 4 tiles dark hui hain.
val MiniBlueStops = arrayOf(0f to Color(0xFFC9DFFF), 0.6f to Color(0xFFA9C4FF), 1f to Color(0xFF7FA8FF))
val MiniYellowStops = arrayOf(0f to Color(0xFFFFE9A3), 0.6f to Color(0xFFFFD166), 1f to Color(0xFFFFB830))
val MiniPurpleStops = arrayOf(0f to Color(0xFFDDC9FF), 0.6f to Color(0xFFBE9FFF), 1f to Color(0xFF9D7BFF))
val MiniPeachStops = arrayOf(0f to Color(0xFFFFD9B8), 0.6f to Color(0xFFFFB57E), 1f to Color(0xFFFF9A52))
    val minis = remember {
        listOf(
            MiniCard("PHOTOS & MAP", "GPS Camera", "Tap to open camera", "gps camera map location", R.drawable.img_gps, MiniBlueStops, BlueShadow),
            MiniCard("FARES & FINANCE", "Money Calc", "Tap to calculate", "money calc fares finance cash", R.drawable.img_money, MiniYellowStops, YellowShadow),
            MiniCard("REPORTS", "Complaints", "Tap to open complaints", "complaints tom agm tkm reports", R.drawable.img_complaint, MiniPurpleStops, PurpleShadow),
            MiniCard("DAILY REGISTER", "Tom Data", "Tap to open register", "tom data register sales", R.drawable.img_tom, MiniPeachStops, PeachShadow)
        )
    }
    val hubs = remember {
        listOf(
            Hub("START SHIFT", Color(0xFFD9650F), "Opening", "Opening data, staff & GPS", R.drawable.ic_dock_opening,
                arrayOf(0f to Color(0xFFFFF4E3), 0.6f to Color(0xFFFFE2BD), 1f to Color(0xFFFFD29E))),
            Hub("END SHIFT", Color(0xFF5B3FD0), "Closing", "End-of-shift reports", R.drawable.ic_dock_closing,
                arrayOf(0f to Color(0xFFF4F0FF), 0.6f to Color(0xFFE4DBFF), 1f to Color(0xFFD5C8FF))),
            Hub("ONLINE", Color(0xFF1C6FD1), "Online Portal", "Central online portal", R.drawable.ic_dock_portal,
                arrayOf(0f to Color(0xFFEAF6FF), 0.6f to Color(0xFFD3EBFF), 1f to Color(0xFFBFE0FF))),
            Hub("APP", Color(0xFFC1121F), "Settings", "Preferences & profile", R.drawable.ic_dock_settings,
                arrayOf(0f to Color(0xFFFFF0F1), 0.6f to Color(0xFFFFDFE2), 1f to Color(0xFFFFCDD2)))
        )
    }
    val pagerState = rememberPagerState(pageCount = { banners.size })
    // Trial: har 5s agla banner (aakhri ke baad pehla); chhoone ke 8s baad tak ruka rehta hai.
    val dragged by pagerState.interactionSource.collectIsDraggedAsState()
    var lastTouch by remember { mutableLongStateOf(0L) }
    LaunchedEffect(dragged) { if (dragged) lastTouch = System.currentTimeMillis() }
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            if (System.currentTimeMillis() - lastTouch < 8000 || pagerState.isScrollInProgress) continue
            pagerState.animateScrollToPage((pagerState.currentPage + 1) % banners.size)
        }
    }
    // Trial jaisa: search sirf mini cards ko label se filter karta hai.
    val q = searchQuery.trim().lowercase()
    val shownMinis = if (q.isEmpty()) minis else minis.filter { q in it.label }

    LazyColumn(modifier = Modifier.fillMaxSize().background(HomeBackground)) {
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
                            modifier = Modifier.size(46.dp)
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
                    Box(
                        modifier = Modifier.size(46.dp)
                            .cssShadow(6.dp, RoundedCornerShape(16.dp), Color(0xFFA32228).copy(alpha = 0.18f))
                            .background(cssLinear(160f, 0f to Color.White, 1f to Color(0xFFF7ECED)), RoundedCornerShape(16.dp))
                            .border(1.dp, Color(0xFFA32228).copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                            .clickable { onChatClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(painterResource(id = R.drawable.ic_chat_bubble), contentDescription = "Group Chat", contentScale = ContentScale.Fit, modifier = Modifier.size(30.dp))
                    }
                }
            }
        }
        // Search pill (44, white, halki border) + refresh circle (48, laal icon).
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f).height(44.dp)
                        .cssShadow(2.dp, RoundedCornerShape(999.dp), Ink.copy(alpha = 0.07f))
                        .background(Color.White, RoundedCornerShape(999.dp))
                        .border(1.dp, Ink.copy(alpha = 0.05f), RoundedCornerShape(999.dp))
                        .padding(horizontal = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Search, contentDescription = null, tint = Color(0xFF9AA1AB), modifier = Modifier.size(17.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    BasicTextField(
                        value = searchQuery, onValueChange = { searchQuery = it },
                        modifier = Modifier.weight(1f), singleLine = true,
                        textStyle = TextStyle(fontSize = 14.sp, color = Ink),
                        decorationBox = { inner ->
                            if (searchQuery.isEmpty()) Text("Search", color = Color(0xFF9AA1AB), fontSize = 14.sp)
                            inner()
                        }
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier.size(48.dp)
                        .cssShadow(2.dp, CircleShape, Ink.copy(alpha = 0.07f))
                        .background(Color.White, CircleShape)
                        .border(1.dp, Ink.copy(alpha = 0.05f), CircleShape)
                        .clickable { onRefreshClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Refresh, contentDescription = "Refresh", tint = Coral, modifier = Modifier.size(20.dp))
                }
            }
        }
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
    Text(
        text, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Ink, letterSpacing = (-0.2).sp,
        modifier = Modifier.padding(start = 22.dp, end = 20.dp, top = top, bottom = 12.dp)
    )
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
                    b.kicker, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Ink.copy(alpha = 0.7f), letterSpacing = 1.2.sp,
                    modifier = Modifier.background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(20.dp)).padding(horizontal = 9.dp, vertical = 2.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(b.title, fontSize = titleSize, fontWeight = FontWeight.ExtraBold, color = Ink, letterSpacing = (-0.4).sp, lineHeight = (titleSize.value * 1.1f).sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(b.sub, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Ink.copy(alpha = 0.6f))
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.cssShadow(6.dp, RoundedCornerShape(20.dp), Color.Black.copy(alpha = 0.18f))
                        .background(Ink, RoundedCornerShape(20.dp)).padding(horizontal = 13.dp, vertical = 7.dp),
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
        Box(
            modifier = Modifier.align(Alignment.TopEnd).padding(top = 12.dp, end = 12.dp).size(28.dp)
                .cssShadow(3.dp, CircleShape, Color.Black.copy(alpha = 0.08f))
                .background(Color.White.copy(alpha = 0.75f), CircleShape),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Filled.ArrowForward, contentDescription = null, tint = Ink, modifier = Modifier.size(13.dp)) }
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp)) {
            Image(
                painterResource(id = m.imageRes), contentDescription = null, contentScale = ContentScale.Crop,
                modifier = Modifier.size(62.dp).cssShadow(4.dp, CircleShape, Ink.copy(alpha = 0.14f)).background(Color.White, CircleShape).clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(m.kicker, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Ink.copy(alpha = 0.55f), letterSpacing = 1.1.sp)
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
        Box(
            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 14.dp).size(32.dp)
                .cssShadow(4.dp, CircleShape, Color.Black.copy(alpha = 0.08f))
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Filled.ArrowForward, contentDescription = null, tint = h.tagColor, modifier = Modifier.size(14.dp)) }
    }
}
