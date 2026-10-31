package com.metroplus.app.ui

import android.content.Context
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ConfirmationNumber
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.KeyboardType
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
// v1.81: WebView preload + keep-alive (PortalWeb object), header gol
// card, dropdown header se chipka hua, load par neela area (safed nahi),
// Saved Login me Gmail/username device par save.

private val PoRed = Section.Portal // v1.56: Portal section ka neela
// v1.80 BLUE THEME (uska order): Portal ka sara interface sirf neelay
// gradients me — gehra neela text, darmiyani neela grey, aur koi rang nahi.
private val PoInk = Color(0xFF0B2A5B)
private val PoGray = Color(0xFF3D5A80)
private const val PORTAL_URL = "https://shababulmohsinapp.ai.studio/"

// v1.81: Portal ka WebView AB EK HI dafa banta hai aur zinda rehta hai
// (uska order — white screen + 6 second wait khatam). App khulte hi
// peeche se load hona shuru (MainActivity preload), Portal kholo to wahi
// WebView attach hota hai, Portal band par destroy NAHI hota — dobara
// kholo to site pehle se khuli + login (cookies) pehle se laga hua.
// Keyboard bhi isi garam WebView par jaldi khulta hai (thanda WebView hi
// susti + page kheenchne ki asal wajah tha).
object PortalWeb {
    var webView: WebView? = null
        private set
    var lastUrl: String = PORTAL_URL
        private set
    var loading: Boolean = true
        private set
    var everLoaded: Boolean = false
        private set
    var onUrl: ((String) -> Unit)? = null
    var onLoading: ((Boolean) -> Unit)? = null
    // Login save ke liye: app ka context (cookies save/restore ke liye).
    private var appCtx: Context? = null
    private const val COOKIE_PREFS = "portal_cookies"
    private const val COOKIE_KEY = "cookies"

    fun preload(ctx: Context) { getOrCreate(ctx) }

    // v1.82 LOGIN SAVE (uska order): app band hone par phone site ka
    // session cookie mita deta hai — isi liye dobara kholo to logout
    // milta tha (app khula ho to theek rehta tha). Ab har page load par
    // cookies save hoti hain, aur naya WebView banne se pehle wahi
    // cookies wapas lagti hain — login laga rahega.
    private fun saveCookies() {
        val ctx = appCtx ?: return
        val cookies = CookieManager.getInstance().getCookie(PORTAL_URL) ?: return
        ctx.getSharedPreferences(COOKIE_PREFS, Context.MODE_PRIVATE)
            .edit().putString(COOKIE_KEY, cookies).apply()
        CookieManager.getInstance().flush()
    }

    private fun restoreCookies() {
        val ctx = appCtx ?: return
        val saved = ctx.getSharedPreferences(COOKIE_PREFS, Context.MODE_PRIVATE)
            .getString(COOKIE_KEY, null) ?: return
        val cm = CookieManager.getInstance()
        saved.split(";").forEach { part ->
            val cookie = part.trim()
            if (cookie.isNotEmpty()) cm.setCookie(PORTAL_URL, cookie)
        }
        cm.flush()
    }

    fun getOrCreate(ctx: Context): WebView {
        appCtx = ctx.applicationContext
        webView?.let { return it }
        val wv = WebView(ctx.applicationContext)
        // WebView ka apna background bhi neela — load se pehle safed na aaye.
        wv.setBackgroundColor(android.graphics.Color.parseColor("#EAF2FE"))
        // v1.83 SCROLL (uska order — Chrome jaisa smooth): WebView ko GPU
        // wali hardware layer — Compose ke andar scroll ka hang/jank isi
        // se behtar hota hai (window ki HW acceleration pehle se on hai).
        wv.setLayerType(android.view.View.LAYER_TYPE_HARDWARE, null)
        // v1.85 SCROLL (63 comparison — trial shell ke settings, sirf Portal):
        // overscroll glow band (fling par extra draw nahi), horizontal
        // scrollbar off (site sirf vertical hai), nested scrolling on,
        // textZoom 100 saaf saaf, scrollbar content ke andar (chaurai na
        // ghate). Trial pattern: DOWN par parent ko touch intercept se roko.
        wv.overScrollMode = android.view.View.OVER_SCROLL_NEVER
        wv.isHorizontalScrollBarEnabled = false
        wv.scrollBarStyle = android.view.View.SCROLLBARS_INSIDE_OVERLAY
        wv.isNestedScrollingEnabled = true
        wv.settings.textZoom = 100
        wv.setOnTouchListener { v, event ->
            if (event.action == android.view.MotionEvent.ACTION_DOWN) v.parent?.requestDisallowInterceptTouchEvent(true)
            false
        }
        wv.settings.javaScriptEnabled = true
        wv.settings.domStorageEnabled = true
        // v1.82 SPEED (uska order — Portal tez): site ki cache istemal ho
        // (dobara kholo to images/files dobara download nahi), database on,
        // rendering ko priority — pehli dafa ke baad page zyada tez khulega.
        wv.settings.cacheMode = android.webkit.WebSettings.LOAD_DEFAULT
        wv.settings.databaseEnabled = true
        // v1.84 SPEED (net research + uska order — sirf Portal):
        // offscreenPreRaster: screen se bahar wale hisse pehle se banenge,
        // scroll karte waqt ruk-ruk khatam hogi (thodi zyada memory legi).
        wv.settings.offscreenPreRaster = true
        // Renderer priority: default IMPORTANT hi sab se upar hai — BOUND
        // lagane se ulta renderer marne ka zyada chance hota, is liye
        // IMPORTANT saaf saaf rakha hai (API 26+).
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            wv.setRendererPriorityPolicy(WebView.RENDERER_PRIORITY_IMPORTANT, false)
        }
        wv.settings.loadsImagesAutomatically = true
        wv.settings.useWideViewPort = true
        wv.settings.loadWithOverviewMode = true
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(wv, true)
        wv.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean = false
            override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                loading = true
                url?.let { lastUrl = it; onUrl?.invoke(it) }
                onLoading?.invoke(true)
            }
            override fun onPageFinished(view: WebView, url: String?) {
                loading = false
                everLoaded = true
                // Har page load par cookies save — login session mehfooz.
                saveCookies()
                url?.let { lastUrl = it; onUrl?.invoke(it) }
                onLoading?.invoke(false)
            }
        }
        // Pehle save ki hui cookies wapas lagao, phir site load karo.
        restoreCookies()
        wv.loadUrl(PORTAL_URL)
        webView = wv
        return wv
    }

    // Purane parent se hata kar wahi WebView wapas do (dobara banega nahi).
    fun attach(ctx: Context): WebView {
        val wv = getOrCreate(ctx)
        (wv.parent as? ViewGroup)?.removeView(wv)
        return wv
    }
}

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
private fun PoField(label: String, icon: @Composable () -> Unit, value: String, onValue: (String) -> Unit, placeholder: String, password: Boolean = false, keyboard: KeyboardType = KeyboardType.Text) {
    var showPass by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(12.dp)
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp, bottom = 6.dp)) {
        icon()
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = PoGray)
    }
    BasicTextField(
        value = value, onValueChange = onValue, singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboard),
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
// v1.84: chhota size — paanchon ek hi line me aayen (uska order).
@Composable
private fun PoQuickCard(c1: Color, c2: Color, label: String, sub: String, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier.clip(RoundedCornerShape(12.dp)).background(Color(0xFFEAF2FE))
            .border(1.dp, c1.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            .clickable { onClick() }.padding(horizontal = 4.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(28.dp).clip(RoundedCornerShape(9.dp)).background(cssLinear(135f, 0f to c1, 1f to c2)),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Filled.List, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp)) }
        Spacer(modifier = Modifier.height(5.dp))
        Text(label, fontSize = 10.5.sp, fontWeight = FontWeight.ExtraBold, color = PoInk, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(sub, fontSize = 8.5.sp, color = PoGray, maxLines = 1, overflow = TextOverflow.Ellipsis)
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
fun PortalScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit, isActive: Boolean = true) {
    var showLogin by remember { mutableStateOf(false) }
    // v1.86: NAYI TKM GUI (63 ke TKM Auto-Fill dialog se, sky blue) —
    // purani full-screen Fill sheet (showFill) hata di gayi hai.
    var showTkm by remember { mutableStateOf(false) }
    var tkmCardCount by remember { mutableStateOf("") }
    var tkmSaleAmount by remember { mutableStateOf("") }
    // v1.80: dropdown Quick Fill overlay + three-dot card.
    var quickOpen by remember { mutableStateOf(false) }
    var dotMenuOpen by remember { mutableStateOf(false) }
    // v1.81: WebView ab PortalScreen ka nahi — PortalWeb (ek hi, zinda
    // rehne wala) ka hai. Portal khule to attach, band ho to sirf detach
    // (destroy nahi) — dobara kholo to site + login pehle se maujood.
    val ctx = LocalContext.current
    var currentUrl by remember { mutableStateOf(PortalWeb.lastUrl) }
    var pageLoading by remember { mutableStateOf(PortalWeb.loading) }
    var everLoaded by remember { mutableStateOf(PortalWeb.everLoaded) }
    DisposableEffect(Unit) {
        PortalWeb.onUrl = { currentUrl = it }
        PortalWeb.onLoading = { pageLoading = it; if (!it) everLoaded = true }
        currentUrl = PortalWeb.lastUrl
        pageLoading = PortalWeb.loading
        everLoaded = PortalWeb.everLoaded
        onDispose { PortalWeb.onUrl = null; PortalWeb.onLoading = null }
    }
    val density = LocalDensity.current
    var headerH by remember { mutableStateOf(0) }
    // v1.85: overlay hamesha composed rehta hai (63 ki tarah) — BackHandler
    // sirf tab jab Portal waqai khula ho, warna Home ka back kharab hota.
    // Chhupte waqt menus/sheets band (dobara kholo to saaf Portal mile).
    LaunchedEffect(isActive) {
        if (!isActive) { quickOpen = false; dotMenuOpen = false; showLogin = false; showTkm = false }
    }
    // Back: pehle menus/panel/sheets, phir WebView history, phir page.
    BackHandler(enabled = isActive) {
        if (quickOpen) quickOpen = false
        else if (dotMenuOpen) dotMenuOpen = false
        else if (showLogin) showLogin = false
        else if (showTkm) showTkm = false
        else if (PortalWeb.webView?.canGoBack() == true) PortalWeb.webView?.goBack()
        else onBack()
    }
    fun reloadPage() { PortalWeb.webView?.reload() }
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFFEAF2FE))
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // v1.81 header (uska order): gol kinaron wala card — gradient
            // container ab poori chaudai ka rectangle nahi, 22dp radius ke
            // sath kinaron se hata hua card hai. Pill + progress uske andar.
            // Header hamesha upar rehta hai — WebView sirf uske neeche hai.
            Column(
                modifier = Modifier.fillMaxWidth()
                    .statusBarsPadding()
                    .onSizeChanged { headerH = it.height }
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(cssLinear(135f, 0f to Color(0xFF0B5BD3), 1f to Color(0xFF38BDF8)))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp)
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
            }
            // Neeche wali jagah = wahi zinda WebView (preload + keep-alive).
            // Session cookies WebView sambhalta hai — ek dafa login to phir
            // laga rahega (jab tak site logout na kare ya data clear na ho).
            // Pehli load tak safed NAHI — halka neela loading area.
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                AndroidView(
                    factory = { c -> PortalWeb.attach(c) },
                    onRelease = { /* destroy NAHI — WebView zinda rehta hai */ },
                    modifier = Modifier.fillMaxSize()
                )
                if (pageLoading && !everLoaded) {
                    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFEAF2FE)), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = PoRed, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Portal load ho raha hai…", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PoGray)
                        }
                    }
                }
                // v1.86 (uska order): browser frame — left/right BILKUL
                // bareek (1dp) WHITE-BLUE MIX lines (safed se sky blue
                // gradient), upar se neeche tak, taake saaf pata chale site
                // app ke andar khuli hai.
                val sideLine = Brush.verticalGradient(listOf(Color.White, Color(0xFF38BDF8)))
                Box(modifier = Modifier.align(Alignment.CenterStart).fillMaxHeight().width(1.dp).background(sideLine))
                Box(modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight().width(1.dp).background(sideLine))
            }
        }
        // ---- Overlays: dropdown grid + three-dot card. Ye content ko
        // neeche NAHI dhakelte (v1.79 wali shikayat) — header ke neeche
        // float karte hain, baahar tap par band ho jate hain. ----
        if (quickOpen || dotMenuOpen) {
            Box(modifier = Modifier.fillMaxSize().clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { quickOpen = false; dotMenuOpen = false })
        }
        // v1.83 (uska order — v1.81 wala chipka hua overlap khatam):
        // dropdown ab header se thoda NEECHE (6dp gap) khulta hai, gol
        // kinaron (20dp) ke sath, aur uske gird GEHRI NEELI border hai
        // taake saaf pata chale ye neeche wale page se alag cheez hai.
        // headerH naapne se pehle panel nahi khulta (aadha upar/aadha
        // neeche wali pehli-frame ghalti khatam).
        val topPad = if (headerH > 0) with(density) { headerH.toDp() } - 6.dp else 0.dp
        val dropTopPad = if (headerH > 0) with(density) { headerH.toDp() } + 6.dp else 0.dp
        if (quickOpen && headerH > 0) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(start = 12.dp, end = 12.dp, top = dropTopPad)
                    .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { },
            ) {
                val cardShape = RoundedCornerShape(20.dp)
                AnimatedVisibility(
                    visible = true,
                    enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                    exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
                ) {
                Column(modifier = Modifier.fillMaxWidth().cssShadow(10.dp, cardShape, PoInk.copy(alpha = 0.18f)).background(Color.White, cardShape).border(1.5.dp, Color(0xFF0B2A5B), cardShape).padding(12.dp)) {
                    Text("QUICK FILL", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = PoInk.copy(alpha = 0.55f), letterSpacing = 1.2.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    // v1.84: paanchon chhote cards EK hi line me, header se
                    // neeche (gap + dark border wahi). v1.86 (uska order):
                    // ASKARI card DARMIYAN (teesri position) me; TKM card
                    // nayi TKM GUI (dialog) kholta hai.
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        PoQuickCard(Color(0xFF1D6FD8), Color(0xFF38BDF8), "Auto Fill", "Login", Modifier.weight(1f)) { quickOpen = false; onGuiNote("Portal Auto Fill") }
                        PoQuickCard(Color(0xFF0B5BD3), Color(0xFF60A5FA), "TOM A", "Data", Modifier.weight(1f)) { quickOpen = false; onGuiNote("Portal Fill TOM A") }
                        PoQuickCard(Color(0xFF2563EB), Color(0xFF93C5FD), "ASKARI", "Voucher", Modifier.weight(1f)) { quickOpen = false; onGuiNote("Portal Fill ASKARI") }
                        PoQuickCard(Color(0xFF1E40AF), Color(0xFF3B82F6), "TOM B", "Data", Modifier.weight(1f)) { quickOpen = false; onGuiNote("Portal Fill TOM B") }
                        PoQuickCard(Color(0xFF0B2A5B), Color(0xFF1D6FD8), "TKM", "Card sale", Modifier.weight(1f)) { quickOpen = false; tkmCardCount = ""; tkmSaleAmount = ""; showTkm = true }
                    }
                }
                }
            }
        }
        if (dotMenuOpen && headerH > 0) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(end = 12.dp, top = topPad)
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
            // v1.81: Gmail/username device par save (SharedPreferences) —
            // sheet kholo to pehle se bhare milenge. Password save nahi hota.
            val loginPrefs = remember { ctx.getSharedPreferences("portal_login", Context.MODE_PRIVATE) }
            var email by pageState("portal_email", loginPrefs.getString("portal_email", "") ?: "")
            var username by pageState("portal_username", loginPrefs.getString("portal_username", "") ?: "")
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
                                    .clip(RoundedCornerShape(14.dp)).background(PoRed).clickable { loginPrefs.edit().putString("portal_email", email).putString("portal_username", username).apply(); onGuiNote("Save Portal Login") },
                                contentAlignment = Alignment.Center
                            ) { Text("Save Login", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White) }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Gmail aur username ab save rahenge — sheet kholo to pehle se bhare milenge. Site ka login cookies se laga rehta hai; session khatam ho to yahan se copy kar lo.", fontSize = 12.5.sp, color = PoGray, lineHeight = 18.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
        // ---- NAYI TKM GUI (v1.86, 63 ke TKM Auto-Fill dialog se, sky blue) ----
        // Purani full-screen Fill sheet ki jagah bottom-sheet card — 63 ki
        // tarah: drag bar, title, note, 2 number fields, Auto-Fill + Cancel.
        // GUI phase: Auto-Fill abhi note dikhata hai (site me asli fill agla step).
        if (showTkm) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.45f)).clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { showTkm = false }) {
                Column(
                    modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)).background(Color.White)
                        .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { }
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Box(modifier = Modifier.width(44.dp).height(4.dp).clip(RoundedCornerShape(2.dp)).background(Color(0xFFE2E8F0)).align(Alignment.CenterHorizontally))
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(34.dp).clip(CircleShape).background(Color(0xFFE0F2FE)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.ConfirmationNumber, contentDescription = null, tint = Color(0xFF0EA5E9), modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("TKM Auto-Fill", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = PoInk, modifier = Modifier.weight(1f))
                        PoHeaderBtn({ Icon(Icons.Filled.Close, contentDescription = "Close", tint = PoInk, modifier = Modifier.size(18.dp)) }) { showTkm = false }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Enter card count and sale amount — then tap Auto-Fill:", fontSize = 13.sp, color = PoGray, lineHeight = 18.sp)
                    Spacer(modifier = Modifier.height(14.dp))
                    PoField("Master card count (cards used)", { Icon(Icons.Filled.ConfirmationNumber, contentDescription = null, tint = Color(0xFF0EA5E9), modifier = Modifier.size(15.dp)) }, tkmCardCount, { tkmCardCount = it.filter { ch -> ch.isDigit() } }, "0", keyboard = KeyboardType.Number)
                    Spacer(modifier = Modifier.height(12.dp))
                    PoField("Sale Amount (Rs.)", { Icon(Icons.Filled.AttachMoney, contentDescription = null, tint = Color(0xFF0EA5E9), modifier = Modifier.size(15.dp)) }, tkmSaleAmount, { tkmSaleAmount = it.filter { ch -> ch.isDigit() || ch == '.' } }, "0", keyboard = KeyboardType.Decimal)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            modifier = Modifier.weight(1f).height(48.dp).cssShadow(6.dp, RoundedCornerShape(14.dp), Color(0xFF0EA5E9).copy(alpha = 0.25f))
                                .clip(RoundedCornerShape(14.dp)).background(cssLinear(135f, 0f to Color(0xFF38BDF8), 1f to Color(0xFF0EA5E9)))
                                .clickable { showTkm = false; onGuiNote("Portal Fill TKM") },
                            contentAlignment = Alignment.Center
                        ) { Text("Auto-Fill", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White) }
                        Box(
                            modifier = Modifier.height(48.dp).clip(RoundedCornerShape(14.dp)).background(Color(0xFFEAF2FE)).border(1.dp, PoInk.copy(alpha = 0.1f), RoundedCornerShape(14.dp))
                                .clickable { showTkm = false }.padding(horizontal = 18.dp),
                            contentAlignment = Alignment.Center
                        ) { Text("Cancel", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = PoInk) }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }

    }
}
