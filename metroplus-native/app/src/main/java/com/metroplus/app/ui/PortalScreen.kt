package com.metroplus.app.ui

import android.graphics.Bitmap
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
// 63 ka Portal interface, native Compose me (GUI phase).
// v1.80 (uska wazeh order, 2026-10-31): WebView SIRF Portal ke andar hai
// (link ke neeche wala area) — poori app me nahi, kisi aur screen me
// nahi. Header blue gradient + safed pill; dropdown = Quick Fill grid
// overlay; three-dot = Open Portal Login / Settings (Portal wali, main
// Settings nahi — uska content wo baad me batayega) / Reload. Save/fill
// ki wiring agle step me hogi.

private val PoRed = Section.Portal // v1.56: Portal section ka neela
// v1.80 BLUE THEME (uska order): Portal ka sara interface sirf neelay
// gradients me — gehra neela text, darmiyani neela grey, aur koi rang nahi.
private val PoInk = Color(0xFF0B2A5B)
private val PoGray = Color(0xFF3D5A80)
private const val PORTAL_URL = "https://shababulmohsinapp.ai.studio/"

@Composable
private fun PoHeaderBtn(icon: @Composable () -> Unit, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(38.dp).clip(CircleShape).background(Color.White)
            .border(1.dp, PoInk.copy(alpha = 0.09f), CircleShape).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) { icon() }
}

@Composable
private fun PoCard(title: String?, content: @Composable () -> Unit) {
    val shape = RoundedCornerShape(18.dp)
    Column(
        modifier = Modifier.fillMaxWidth().cssShadow(5.dp, shape, PoInk.copy(alpha = 0.08f))
            .background(Color.White, shape).border(1.dp, PoInk.copy(alpha = 0.06f), shape).padding(16.dp)
    ) {
        if (title != null) {
            Text(title, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = PoInk.copy(alpha = 0.55f), letterSpacing = 1.2.sp)
            Spacer(modifier = Modifier.height(10.dp))
        }
        content()
    }
}

@Composable
private fun PoField(label: String, icon: @Composable () -> Unit, value: String, onValue: (String) -> Unit, placeholder: String, password: Boolean = false) {
    var showPass by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(12.dp)
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp, bottom = 6.dp)) {
        icon()
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = PoGray)
    }
    BasicTextField(
        value = value, onValueChange = onValue, singleLine = true,
        visualTransformation = if (password && !showPass) PasswordVisualTransformation() else VisualTransformation.None,
        textStyle = TextStyle(fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold, color = PoInk),
        modifier = Modifier.fillMaxWidth(),
        decorationBox = { inner ->
            Box(
                modifier = Modifier.fillMaxWidth().clip(shape).background(Color(0xFFEAF2FE)).border(1.dp, PoInk.copy(alpha = 0.1f), shape)
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) Text(placeholder, fontSize = 13.5.sp, color = PoInk.copy(alpha = 0.35f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.weight(1f)) { inner() }
                    if (password) {
                        Icon(
                            if (showPass) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = null, tint = PoGray, modifier = Modifier.size(18.dp).clickable { showPass = !showPass }
                        )
                    }
                }
            }
        }
    )
}

// v1.80: Quick Fill dropdown grid ka card (sirf neelay gradients).
@Composable
private fun PoQuickCard(c1: Color, c2: Color, label: String, sub: String, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier.clip(RoundedCornerShape(14.dp)).background(Color(0xFFEAF2FE))
            .border(1.dp, c1.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
            .clickable { onClick() }.padding(horizontal = 8.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(36.dp).clip(RoundedCornerShape(11.dp)).background(cssLinear(135f, 0f to c1, 1f to c2)),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Filled.List, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp)) }
        Spacer(modifier = Modifier.height(6.dp))
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = PoInk, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(sub, fontSize = 9.5.sp, color = PoGray, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

// v1.80: three-dot menu ki pyari row (icon circle + label).
@Composable
private fun PoMenuRow(icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(horizontal = 14.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(32.dp).clip(CircleShape).background(color.copy(alpha = 0.14f)),
            contentAlignment = Alignment.Center
        ) { Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(17.dp)) }
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PoInk)
    }
}

@Composable
fun PortalScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit) {
    var showLogin by remember { mutableStateOf(false) }
    var showFill by remember { mutableStateOf(false) }
    // v1.80: dropdown Quick Fill overlay + three-dot card.
    var quickOpen by remember { mutableStateOf(false) }
    var dotMenuOpen by remember { mutableStateOf(false) }
    // WebView SIRF Portal ke andar (uska order, 2026-10-31): Portal khule to
    // chale, band ho to destroy — poori app me nahi, aur kisi screen me nahi.
    var currentUrl by remember { mutableStateOf(PORTAL_URL) }
    var pageLoading by remember { mutableStateOf(true) }
    val webHolder = remember { object { var w: WebView? = null } }
    val density = LocalDensity.current
    var headerH by remember { mutableStateOf(0) }
    // Back: pehle menus/panel/sheets, phir WebView history, phir page.
    BackHandler {
        if (quickOpen) quickOpen = false
        else if (dotMenuOpen) dotMenuOpen = false
        else if (showLogin) showLogin = false
        else if (showFill) showFill = false
        else if (webHolder.w?.canGoBack() == true) webHolder.w?.goBack()
        else onBack()
    }
    fun reloadPage() { webHolder.w?.reload() }
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFFEAF2FE))
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // v1.80 header (uska order): blue gradient container, uske andar
            // safed pill — EK hi line: lock + link (thoda lamba, darmiyan se
            // aage tak) + dropdown + gehra neela three-dot. GCS tab nahi hai.
            Column(
                modifier = Modifier.fillMaxWidth()
                    .background(cssLinear(135f, 0f to Color(0xFF0B5BD3), 1f to Color(0xFF38BDF8)))
                    .statusBarsPadding()
                    .onSizeChanged { headerH = it.height }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)
                        .clip(RoundedCornerShape(999.dp)).background(Color.White.copy(alpha = 0.96f))
                        .padding(start = 12.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Lock, contentDescription = null, tint = PoRed, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(currentUrl, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PoInk, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(2f))
                    // Link darmiyan se aage tak (pehle aadha tha, ab 2/3).
                    Spacer(modifier = Modifier.weight(1f))
                    PoHeaderBtn({ Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Quick Fill", tint = PoRed, modifier = Modifier.size(18.dp)) }) { dotMenuOpen = false; quickOpen = !quickOpen }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier.size(38.dp).clip(CircleShape).background(Color(0xFF0B2A5B)).clickable { quickOpen = false; dotMenuOpen = true },
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.Filled.MoreVert, contentDescription = "More", tint = Color.White, modifier = Modifier.size(18.dp)) }
                }
                if (pageLoading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth().height(3.dp), color = Color.White, trackColor = Color.White.copy(alpha = 0.3f))
            }
            // Neeche wali jagah = WebView (link ke neeche ka area, SIRF yahan).
            // Session cookies WebView sambhalta hai — ek dafa login to phir
            // laga rahega (jab tak site logout na kare ya data clear na ho).
            AndroidView(
                factory = { c ->
                    WebView(c).also { wv ->
                        webHolder.w = wv
                        wv.settings.javaScriptEnabled = true
                        wv.settings.domStorageEnabled = true
                        wv.settings.loadsImagesAutomatically = true
                        wv.settings.useWideViewPort = true
                        wv.settings.loadWithOverviewMode = true
                        CookieManager.getInstance().setAcceptCookie(true)
                        CookieManager.getInstance().setAcceptThirdPartyCookies(wv, true)
                        wv.webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean = false
                            override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) { pageLoading = true; url?.let { currentUrl = it } }
                            override fun onPageFinished(view: WebView, url: String?) { pageLoading = false; url?.let { currentUrl = it } }
                        }
                        wv.loadUrl(PORTAL_URL)
                    }
                },
                onRelease = { it.stopLoading(); it.destroy(); webHolder.w = null },
                modifier = Modifier.weight(1f).fillMaxWidth()
            )
        }
        // ---- Overlays: dropdown grid + three-dot card. Ye content ko
        // neeche NAHI dhakelte (v1.79 wali shikayat) — header ke neeche
        // float karte hain, baahar tap par band ho jate hain. ----
        if (quickOpen || dotMenuOpen) {
            Box(modifier = Modifier.fillMaxSize().clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { quickOpen = false; dotMenuOpen = false })
        }
        val topPad = with(density) { headerH.toDp() } + 8.dp
        if (quickOpen) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = topPad)
                    .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { },
            ) {
                val cardShape = RoundedCornerShape(18.dp)
                Column(modifier = Modifier.fillMaxWidth().cssShadow(10.dp, cardShape, PoInk.copy(alpha = 0.18f)).background(Color.White, cardShape).padding(12.dp)) {
                    Text("QUICK FILL", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = PoInk.copy(alpha = 0.55f), letterSpacing = 1.2.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        PoQuickCard(Color(0xFF1D6FD8), Color(0xFF38BDF8), "Auto Fill", "Saved login", Modifier.weight(1f)) { quickOpen = false; onGuiNote("Portal Auto Fill") }
                        PoQuickCard(Color(0xFF0B5BD3), Color(0xFF60A5FA), "TOM A", "Fill data", Modifier.weight(1f)) { quickOpen = false; onGuiNote("Portal Fill TOM A") }
                        PoQuickCard(Color(0xFF1E40AF), Color(0xFF3B82F6), "TOM B", "Fill data", Modifier.weight(1f)) { quickOpen = false; onGuiNote("Portal Fill TOM B") }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row {
                        Spacer(modifier = Modifier.weight(0.5f))
                        PoQuickCard(Color(0xFF0B2A5B), Color(0xFF1D6FD8), "TKM", "Fill card sale", Modifier.weight(1f)) { quickOpen = false; onGuiNote("Portal Fill TKM") }
                        Spacer(modifier = Modifier.width(10.dp))
                        PoQuickCard(Color(0xFF2563EB), Color(0xFF93C5FD), "ASKARI", "Fill voucher", Modifier.weight(1f)) { quickOpen = false; onGuiNote("Portal Fill ASKARI") }
                        Spacer(modifier = Modifier.weight(0.5f))
                    }
                }
            }
        }
        if (dotMenuOpen) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(end = 16.dp, top = topPad)
                    .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { },
            ) {
                val cardShape = RoundedCornerShape(18.dp)
                Column(modifier = Modifier.width(230.dp).align(Alignment.TopEnd).cssShadow(10.dp, cardShape, PoInk.copy(alpha = 0.18f)).background(Color.White, cardShape).padding(vertical = 6.dp)) {
                    PoMenuRow(Icons.Filled.Key, Color(0xFF1D6FD8), "Open Portal Login") { dotMenuOpen = false; showLogin = true }
                    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp).height(1.dp).background(PoInk.copy(alpha = 0.06f)))
                    PoMenuRow(Icons.Filled.Settings, Color(0xFF0B2A5B), "Settings") { dotMenuOpen = false; onGuiNote("Portal Settings") }
                    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp).height(1.dp).background(PoInk.copy(alpha = 0.06f)))
                    PoMenuRow(Icons.Filled.Refresh, Color(0xFF38BDF8), "Reload") { dotMenuOpen = false; reloadPage() }
                }
            }
        }
        // ---- 63 ki Saved Login sheet (nested overlay) ----
        if (showLogin) {
            var email by pageState("portal_email", "")
            var username by pageState("portal_username", "")
            var password by pageState("portal_password", "")
            Box(
                modifier = Modifier.fillMaxSize().background(Color(0xFFEAF2FE))
                    .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { }
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().background(Color(0xFFEAF2FE)).statusBarsPadding().padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Portal Login", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = PoInk, letterSpacing = (-0.3).sp, modifier = Modifier.weight(1f))
                        PoHeaderBtn({ Icon(Icons.Filled.Close, contentDescription = "Close", tint = PoInk, modifier = Modifier.size(18.dp)) }) { showLogin = false }
                    }
                    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        PoCard(null) {
                            PoField("Gmail", { Icon(Icons.Filled.Person, contentDescription = null, tint = PoRed, modifier = Modifier.size(15.dp)) }, email, { email = it }, "Portal Gmail")
                            Spacer(modifier = Modifier.height(10.dp))
                            PoField("Username", { Icon(Icons.Filled.Person, contentDescription = null, tint = PoRed, modifier = Modifier.size(15.dp)) }, username, { username = it }, "User ID")
                            Spacer(modifier = Modifier.height(10.dp))
                            PoField("Password", { Icon(Icons.Filled.Lock, contentDescription = null, tint = PoRed, modifier = Modifier.size(15.dp)) }, password, { password = it }, "Password", password = true)
                            Spacer(modifier = Modifier.height(12.dp))
                            Box(
                                modifier = Modifier.fillMaxWidth().height(48.dp).cssShadow(6.dp, RoundedCornerShape(14.dp), PoRed.copy(alpha = 0.25f))
                                    .clip(RoundedCornerShape(14.dp)).background(PoRed).clickable { onGuiNote("Save Portal Login") },
                                contentAlignment = Alignment.Center
                            ) { Text("Save Login", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White) }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("After saving, the site keeps you logged in inside Portal (cookies) — if the session ever ends, copy the login from here.", fontSize = 12.5.sp, color = PoGray, lineHeight = 18.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
        // ---- 63 ki Data-Fill sheet (nested overlay) ----
        if (showFill) {
            val today = remember { SimpleDateFormat("EEEE, d MMMM", Locale.ENGLISH).format(Calendar.getInstance().time) }
            val fillRows = listOf("TOM A" to "TOM A", "TOM B" to "TOM B", "TKM" to "TKM", "BOP / POS" to "BOP")
            Box(
                modifier = Modifier.fillMaxSize().background(Color(0xFFEAF2FE))
                    .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { }
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().background(Color(0xFFEAF2FE)).statusBarsPadding().padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Fill Today's Data", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = PoInk, letterSpacing = (-0.3).sp, modifier = Modifier.weight(1f))
                        PoHeaderBtn({ Icon(Icons.Filled.Close, contentDescription = "Close", tint = PoInk, modifier = Modifier.size(18.dp)) }) { showFill = false }
                    }
                    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(today, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PoGray, modifier = Modifier.padding(horizontal = 4.dp))
                        PoCard(null) {
                            fillRows.forEachIndexed { i, (label, key) ->
                                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Text("$label: —", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PoInk, modifier = Modifier.weight(1f))
                                    Box(
                                        modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(PoRed).clickable { onGuiNote("Portal Fill $key") }.padding(horizontal = 12.dp, vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) { Text("Auto-Fill", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.White) }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(Color(0xFFEAF2FE)).border(1.dp, PoInk.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
                                            .clickable { onGuiNote("Copy Portal $key") }.padding(horizontal = 10.dp, vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) { Icon(Icons.Filled.ContentCopy, contentDescription = null, tint = PoGray, modifier = Modifier.size(16.dp)) }
                                }
                                if (i < fillRows.size - 1) Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(PoInk.copy(alpha = 0.06f)))
                            }
                        }
                        Text("Values will appear here once today's closing is saved in Tom Data — save wiring comes in a later step.", fontSize = 12.5.sp, color = PoGray, lineHeight = 18.sp, modifier = Modifier.padding(horizontal = 4.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}
