package com.metroplus.app

import android.os.Bundle
import java.util.Locale
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Refresh
import com.metroplus.app.ui.DockIcon
import com.metroplus.app.ui.SettingsScreen
import com.metroplus.app.ui.TomDataScreen
import com.metroplus.app.ui.TlLockScreen
import com.metroplus.app.ui.ChatSettingsScreen
import com.metroplus.app.ui.AppInfoScreen
import com.metroplus.app.ui.SystemStorageScreen
import com.metroplus.app.ui.WaFormatScreen
import com.metroplus.app.ui.TomCountScreen
import com.metroplus.app.ui.PhotoViewerScreen
import com.metroplus.app.ui.SplashScreen
import com.metroplus.app.ui.rememberDockIcon
import com.metroplus.app.ui.LoginScreen
import com.metroplus.app.ui.SignUpScreen
import com.metroplus.app.ui.SPLASH_MS
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animate
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import kotlinx.coroutines.Job
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.rememberDrawerState
import com.metroplus.app.ui.ClosingScreen
import com.metroplus.app.ui.ComplaintScreen
import com.metroplus.app.ui.ConfettiOverlay
import com.metroplus.app.ui.GroupChatScreen
import com.metroplus.app.ui.CalculatorSettingsScreen
import com.metroplus.app.ui.EditProfileScreen
import com.metroplus.app.ui.GpsCameraScreen
import com.metroplus.app.ui.GpsSettingsScreen
import com.metroplus.app.ui.TlRpSettingsScreen
import com.metroplus.app.ui.MoneyCalculatorScreen
import com.metroplus.app.ui.OpeningScreen
import com.metroplus.app.ui.PortalScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metroplus.app.ui.HomeScreen
import com.metroplus.app.ui.theme.HomeBackground
import com.metroplus.app.ui.theme.MetroGreen
import com.metroplus.app.ui.theme.MetroGreenLight
import com.metroplus.app.ui.theme.MetroPlusTheme
import com.metroplus.app.ui.theme.Muted
import kotlinx.coroutines.launch

// GUI PHASE ONLY: Home screen ka design. Baqi screens abhi nahi bani,
// is liye un par tap karne se sirf ye bataya jata hai ke wo agle step me banegi.

enum class NavTab { SETTINGS, OPENING, HOME, CLOSING, PORTAL }

// Sub-screens: HOME se aage jo bhi page khulega, right se left slide hoke aayega.
enum class AppScreen { HOME, MONEY, OPENING, CLOSING, CAMERA, SETTINGS, TOM, COMPLAINTS, EDIT_PROFILE, CALC_SETTINGS, GPS_SETTINGS, TLRP_SETTINGS, PORTAL, GROUP_CHAT, TL_LOCK, CHAT_SETTINGS, APP_INFO, SYSTEM_STORAGE, WA_FORMAT, TOM_COUNT, PHOTO_VIEWER, LOGIN, SIGNUP }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // 63 jaisa: upar status bar + neeche nav bar 63 ke theme-color
        // #B02A31 (gehra laal) me — edge-to-edge pe warna dono strips
        // be-rang nazar aati thin (user ne red se mark ki thin). Icons
        // safed rakhe hain taake gehre laal pe nazar aayen.
        window.statusBarColor = android.graphics.Color.parseColor("#B02A31")
        window.navigationBarColor = android.graphics.Color.parseColor("#B02A31")
        androidx.core.view.WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }
        setContent { MetroPlusTheme { MetroPlusApp() } }
    }

    override fun onResume() {
        super.onResume()
        // Har foreground pe 5s check ka trigger (cold open + background se wapas, dono).
        ForegroundTicker.resumeCount.value++
        // Unknown sources on karke wapas aaye to pending update foran install ho.
        UpdateManager.pendingInstallFile?.let { file ->
            if (UpdateManager.canInstall(this)) {
                UpdateManager.pendingInstallFile = null
                UpdateManager.installUpdate(this, file)
            }
        }
    }
}

@Composable
fun MetroPlusApp() {
    val context = LocalContext.current
    // Sticky rule: har app open pe 5 second baad update check.
    // Update mile to khud download shuru, progress upar pill me, 100% pe khud install
    // screen — app ke andar koi dialog, koi button nahi (trial 63 ka silent system).
    var downloadProgress by remember { mutableStateOf<Int?>(null) }
    // v1.63: 63 jaisa nazar aane wala progress — % na mile to downloaded MB.
    var downloadBytes by remember { mutableStateOf(0L) }
    // Batch item 13: update pe confetti (63 wali) — 100% hote hi dikhegi.
    var showConfetti by remember { mutableStateOf(false) }
    // v1.62: splash GUI — cold open pe 1.5s, phir Home (sirf GUI, koi login nahi).
    var showSplash by remember { mutableStateOf(true) }
    // v1.66: intro ab 5 second (uska order) — progress bar + badalti lines SplashScreen me.
    LaunchedEffect(Unit) { kotlinx.coroutines.delay(SPLASH_MS.toLong()); showSplash = false }
    fun startDownload(apkUrl: String) {
        // Double download se bacho (do check ek sath mile to bhi ek hi download).
        if (downloadProgress != null) return
        downloadProgress = 0
        downloadBytes = 0L
        UpdateManager.downloadUpdate(
            context.applicationContext, apkUrl,
            onProgress = { pct, down, _ -> downloadProgress = pct; downloadBytes = down },
            onComplete = { file ->
                // Pehle 100% dikhao + confetti (63 wali), phir silent install —
                // koi button, koi dialog nahi. Install 2.7s baad khulta hai taake
                // confetti nazar aa jaye (confetti 2.6s chalti hai).
                downloadProgress = 100
                showConfetti = true
                // 63 ki tarah: install se pehle "Installing update…" nazar aaye.
                // (toast() yahan se upar hai is liye direct Toast use kiya.)
                Toast.makeText(context, "Installing update…", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    downloadProgress = null
                    showConfetti = false
                    if (UpdateManager.canInstall(context.applicationContext)) {
                        UpdateManager.installUpdate(context.applicationContext, file)
                    } else {
                        // Nayi app ke liye "Install unknown apps" ijazat EK dafa darkar hai
                        // (purani app ki ijazat nayi app par nahi chalti) — settings seedha
                        // khol do; wapas aate hi onResume install khud khol dega.
                        UpdateManager.pendingInstallFile = file
                        UpdateManager.openUnknownSourcesSettings(context.applicationContext)
                    }
                }, 2700)
            },
            onError = {
                // 63 ki tarah fail bhi NAZAR aaye (63 me "Download Fail" popup
                // tha) — bar chhupegi aur toast batayega; dobara check agle
                // foreground par ya drawer ke Check Update se ho jayega.
                downloadProgress = null
                Toast.makeText(context, "Download failed — please try again", Toast.LENGTH_SHORT).show()
            }
        )
    }
    // Har foreground (cold open + background se wapas) ke 5 second baad check.
    // Resume ke dauran dobara foreground aaye to pichhla timer cancel hoke naya
    // 5s timer shuru hota hai — check hamesha aakhri foreground ke 5s baad hota hai.
    val resumeTick = ForegroundTicker.resumeCount.value
    LaunchedEffect(resumeTick) {
        if (resumeTick == 0) return@LaunchedEffect
        kotlinx.coroutines.delay(5000)
        if (downloadProgress == null) {
            UpdateManager.checkForUpdate(
                context.applicationContext,
                // Update mile to BINA button dabaye khud download shuru (purana silent system).
                onUpdate = { versionName, apkUrl -> startDownload(apkUrl) }
            )
        }
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var activeNav by remember { mutableStateOf(NavTab.HOME) }
    var screen by remember { mutableStateOf(AppScreen.HOME) }
    // v1.64 swipe-back state — yahan upar taake neeche LaunchedEffect bhi use kar sake.
    var swipeOffsetX by remember { mutableStateOf(0f) }
    var swipeClosing by remember { mutableStateOf(false) }
    // Back pe slide-out ke dauran bhi wahi page dikhe (warna khali box slide hota tha).
    var lastSubScreen by remember { mutableStateOf(AppScreen.MONEY) }
    LaunchedEffect(screen) { if (screen != AppScreen.HOME) lastSubScreen = screen }
    // v1.64: swipe-close ke reset window me naya page khule to offset
    // spring-back se reset — warna naya page aadha khiska hua aayega.
    LaunchedEffect(screen) {
        if (screen != AppScreen.HOME && swipeClosing) {
            animate(swipeOffsetX, 0f, animationSpec = tween(200, easing = FastOutSlowInEasing)) { v, _ -> swipeOffsetX = v }
            swipeClosing = false
        }
    }
    // Camera jahan se khula, back pe wahin wapas (Opening → GPS → Camera → back = Opening).
    var cameraFrom by remember { mutableStateOf(AppScreen.HOME) }
    // v1.66 (uska order "jis tarteeb se gaya usi tarteeb se wapas"): page ka back
    // stack. Ek khule page se doosra khule (Settings → App Info, Settings →
    // Calculator, Opening → Camera ...) to pichhla page stack me jata hai; back
    // pe wahi wapas aata hai. Dock (Home/Opening/Closing/Portal) aur Home se
    // seedha khulne wale pages stack saaf karte hain — unka back seedha Home.
    val backStack = remember { mutableStateListOf<AppScreen>() }
    var navBack by remember { mutableStateOf(false) }
    fun navFor(s: AppScreen) = when (s) {
        AppScreen.OPENING -> NavTab.OPENING; AppScreen.CLOSING -> NavTab.CLOSING
        AppScreen.PORTAL -> NavTab.PORTAL; AppScreen.SETTINGS -> NavTab.SETTINGS
        else -> activeNav
    }
    fun backHome() { backStack.clear(); navBack = true; screen = AppScreen.HOME; activeNav = NavTab.HOME }
    fun go(to: AppScreen) {
        if (screen != AppScreen.HOME && screen != to) backStack.add(screen)
        navBack = false
        screen = to
    }
    fun goBack() {
        if (backStack.isEmpty()) { backHome(); return }
        navBack = true
        screen = backStack.removeAt(backStack.size - 1)
        activeNav = navFor(screen)
    }
    var dialog by remember { mutableStateOf<String?>(null) }
    fun toast(msg: String) { Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() }
    fun notYet(name: String) { dialog = "$name\n\nGUI phase: this screen is design-only for now, its feature comes in a later step." }
    fun manualCheck() {
        UpdateManager.checkForUpdate(
            context.applicationContext,
            onUpdate = { versionName, apkUrl -> startDownload(apkUrl) },
            onNoUpdate = { toast("App is already on the latest version") },
            onError = { toast("Update check failed, please check your internet") }
        )
    }
    fun closeDrawer() { scope.launch { drawerState.close() } }

    BackHandler(enabled = drawerState.isOpen) { closeDrawer() }

    val density = LocalDensity.current
    val imeBottom = with(density) { WindowInsets.ime.getBottom(density).toDp() }
    val navBottom = with(density) { WindowInsets.navigationBars.getBottom(density).toDp() }
    // v1.64: swipe-back — khula page finger ke sath left khisakta hai.
    // Aadhi screen tak kheenchne ya tez flick par back; wapas (right) kheenchne
    // par page apni jagah wapas (cancel). Sirf page ke RIGHT half se shuru
    // hone wala swipe — taake left side ke content aur drawer-edge se takrao na ho.
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val screenWidthPx = with(density) { LocalConfiguration.current.screenWidthDp.dp.toPx() }

    Box(modifier = Modifier.fillMaxSize()) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        scrimColor = Color(0xFF0A0E0A).copy(alpha = 0.45f),
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width((LocalConfiguration.current.screenWidthDp * 0.8f).dp),
                drawerContainerColor = Color(0xFFF6F0F0),
                drawerShape = RoundedCornerShape(0.dp)
            ) {
                ProfileDrawer(
                    onClose = { closeDrawer() },
                    onEdit = { closeDrawer(); screen = AppScreen.EDIT_PROFILE },
                    onMenu = { name ->
                        closeDrawer()
                        if (name == "app Setting") { activeNav = NavTab.SETTINGS; screen = AppScreen.SETTINGS }
                        else if (name == "Calculator setting") screen = AppScreen.CALC_SETTINGS
                        else if (name == "Gps setting") screen = AppScreen.GPS_SETTINGS
                        else if (name == "Tl/Rp access setting") screen = AppScreen.TLRP_SETTINGS
                        else notYet(name)
                    },
                    onCheckUpdate = { closeDrawer(); manualCheck() },
                    // v1.66: Log Out se Login page (sirf GUI).
                    onLogout = { closeDrawer(); screen = AppScreen.LOGIN }
                )
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize().background(HomeBackground)) {
            HomeScreen(
                onProfileClick = { scope.launch { drawerState.open() } },
                onChatClick = { screen = AppScreen.GROUP_CHAT },
                onFeatureClick = {
                    if (it == "Money Calc" || it == "Money Calculator") screen = AppScreen.MONEY
                    else if (it == "Opening") { activeNav = NavTab.OPENING; screen = AppScreen.OPENING }
                    else if (it == "Closing") { activeNav = NavTab.CLOSING; screen = AppScreen.CLOSING }
                    else if (it == "GPS Camera") { cameraFrom = AppScreen.HOME; screen = AppScreen.CAMERA }
                    else if (it == "Settings") { activeNav = NavTab.SETTINGS; screen = AppScreen.SETTINGS }
                    else if (it == "Tom Data") screen = AppScreen.TOM
                    else if (it == "Complaints") screen = AppScreen.COMPLAINTS
                    else if (it == "Online Portal") { activeNav = NavTab.PORTAL; screen = AppScreen.PORTAL }
                    else notYet(it)
                }
            )
        }
    }
        // Sub-screen overlay: normal pages right se left slide hoke aate hain
        // (350ms), back pe wapas right ki taraf slide hoke jate hain.
        // Batch item 8: DRAWER pages ULTE hain — khulna left→right, band hona
        // right→left. Exit ke waqt screen HOME hota hai, is liye direction
        // lastSubScreen se tay hoti hai (warna drawer page bhi right ko nikalta).
        val animScreen = if (screen != AppScreen.HOME) screen else lastSubScreen
        val isDrawerPage = animScreen == AppScreen.EDIT_PROFILE || animScreen == AppScreen.CALC_SETTINGS ||
            animScreen == AppScreen.GPS_SETTINGS || animScreen == AppScreen.TLRP_SETTINGS
        AnimatedVisibility(
            visible = screen != AppScreen.HOME,
            enter = slideInHorizontally(animationSpec = tween(350, easing = FastOutSlowInEasing)) { if (isDrawerPage) -it else it } + fadeIn(animationSpec = tween(350)),
            // Swipe-close par exit bhi LEFT ko (finger ki taraf) — warna page
            // wapas right ko slide karke nikalta, jo swipe se ulta lagta.
            exit = if (swipeClosing) slideOutHorizontally(animationSpec = tween(250, easing = FastOutSlowInEasing)) { -it } + fadeOut(animationSpec = tween(250))
                else slideOutHorizontally(animationSpec = tween(350, easing = FastOutSlowInEasing)) { if (isDrawerPage) -it else it } + fadeOut(animationSpec = tween(350)),
            modifier = Modifier.fillMaxSize()
                .offset { IntOffset(swipeOffsetX.toInt(), 0) }
                .pointerInput(Unit) {
                    // Har gesture ke liye dobara set hote hain.
                    var dragStartedRight = false
                    var tracker = VelocityTracker()
                    var offsetAnimJob: Job? = null
                    fun springBack(duration: Int) {
                        offsetAnimJob?.cancel()
                        offsetAnimJob = scope.launch {
                            animate(swipeOffsetX, 0f, animationSpec = tween(duration, easing = FastOutSlowInEasing)) { v, _ -> swipeOffsetX = v }
                        }
                    }
                    detectHorizontalDragGestures(
                        onDragStart = { offset ->
                            // Page khula ho AUR swipe page ke RIGHT half se shuru ho.
                            dragStartedRight = screen != AppScreen.HOME && offset.x > size.width * 0.5f
                            if (dragStartedRight) offsetAnimJob?.cancel()
                            tracker = VelocityTracker()
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            if (!dragStartedRight) return@detectHorizontalDragGestures
                            change.consume()
                            tracker.addPosition(change.uptimeMillis, change.position)
                            swipeOffsetX = (swipeOffsetX + dragAmount).coerceIn(-screenWidthPx, 0f)
                        },
                        onDragEnd = {
                            if (!dragStartedRight) return@detectHorizontalDragGestures
                            val velocityX = tracker.calculateVelocity().x
                            val offsetX = swipeOffsetX
                            // CLOSE: aadhi screen tak ya tez flick — back jaisa.
                            if (offsetX < -screenWidthPx * 0.5f || velocityX < -1000f) {
                                offsetAnimJob = scope.launch {
                                    swipeClosing = true
                                    if (screen != AppScreen.HOME) backDispatcher?.onBackPressed()
                                    if (screen == AppScreen.HOME) {
                                        // Page exit ho raha hai — exit ke dauran
                                        // offset waisa hi rahe, phir reset.
                                        kotlinx.coroutines.delay(300)
                                        swipeOffsetX = 0f
                                        swipeClosing = false
                                    } else {
                                        // Sheet band hui ya doosra sub-page khula —
                                        // page apni jagah wapas.
                                        animate(swipeOffsetX, 0f, animationSpec = tween(300, easing = FastOutSlowInEasing)) { v, _ -> swipeOffsetX = v }
                                        swipeClosing = false
                                    }
                                }
                            } else {
                                // CANCEL: page wapas apni jagah.
                                springBack(250)
                            }
                        },
                        onDragCancel = {
                            if (dragStartedRight) springBack(250)
                        }
                    )
                }
        ) {
            val shown = if (screen != AppScreen.HOME) screen else lastSubScreen
            // Dock har page pe samne rehta hai — is liye page ka content dock ke upar khatam hota hai
            // (camera full-screen hai, wahan dock nahi). Keyboard khula ho to content keyboard ke
            // upar khatam hota hai (edge-to-edge me warna chat/form ka neeche wala field keyboard
            // ke peeche chhup jata tha).
            val fullScreen = shown == AppScreen.CAMERA || shown == AppScreen.LOGIN || shown == AppScreen.SIGNUP
            val bottomSpace = if (fullScreen) 0.dp else maxOf(imeBottom, navBottom + 76.dp)
            // FIX (v1.42): overlay ke khaali area ke tap Home tak na pahunchein —
            // pehle overlay ki background tap nahi rokti thi, is liye khula page
            // ke neeche wale home tiles bhi dab jate the.
            Box(modifier = Modifier.fillMaxSize().background(HomeBackground).padding(bottom = bottomSpace)
                .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { }) {
                // Ek khule page se doosre pe (dock se, ya camera se wapas) bhi slide: naya page
                // right se aata hai, purana left ko jata hai; camera se wapas aate hue ulta.
                AnimatedContent(
                    targetState = shown,
                    transitionSpec = {
                        // v1.66: back stack se wapas aaye to ulti slide (purana page left se aata hai).
                        val back = navBack || initialState == AppScreen.CAMERA
                        val spec = tween<IntOffset>(350, easing = FastOutSlowInEasing)
                        (slideInHorizontally(spec) { if (back) -it else it } + fadeIn(tween(350))) togetherWith
                            (slideOutHorizontally(spec) { if (back) it else -it } + fadeOut(tween(350)))
                    },
                    label = "pageSwitch",
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                Box(modifier = Modifier.fillMaxSize().background(HomeBackground)) {
                when (page) {
                    AppScreen.MONEY -> MoneyCalculatorScreen(onBack = { goBack() }, onGuiNote = { notYet(it) })
                    AppScreen.OPENING -> OpeningScreen(onBack = { goBack() }, onGuiNote = { notYet(it) }, onOpenCamera = { cameraFrom = AppScreen.OPENING; go(AppScreen.CAMERA) }, onOpenViewer = { go(AppScreen.PHOTO_VIEWER) })
                    AppScreen.CLOSING -> ClosingScreen(onBack = { goBack() }, onGuiNote = { notYet(it) })
                    AppScreen.CAMERA -> GpsCameraScreen(onBack = { goBack() }, onGuiNote = { notYet(it) })
                    AppScreen.SETTINGS -> SettingsScreen(onBack = { goBack() }, onGuiNote = { notYet(it) }, onOpenPage = {
                        when (it) {
                            "TL / RP Login" -> go(AppScreen.TL_LOCK)
                            "Chat Settings" -> go(AppScreen.CHAT_SETTINGS)
                            "System & Storage" -> go(AppScreen.SYSTEM_STORAGE)
                            "WhatsApp Report Format" -> go(AppScreen.WA_FORMAT)
                            "App Info" -> go(AppScreen.APP_INFO)
                            // v1.65: Settings ke Quick tiles + profile card se drawer wale pages.
                            "Edit Profile" -> go(AppScreen.EDIT_PROFILE)
                            "Calculator setting" -> go(AppScreen.CALC_SETTINGS)
                            "Gps setting" -> go(AppScreen.GPS_SETTINGS)
                            "Tl/Rp access setting" -> go(AppScreen.TLRP_SETTINGS)
                            "Logout" -> go(AppScreen.LOGIN)
                            else -> notYet(it)
                        }
                    })
                    AppScreen.TOM -> TomDataScreen(onBack = { goBack() }, onGuiNote = { notYet(it) }, onOpenTomCount = { go(AppScreen.TOM_COUNT) })
                    AppScreen.COMPLAINTS -> ComplaintScreen(onBack = { goBack() }, onGuiNote = { notYet(it) })
                    AppScreen.EDIT_PROFILE -> EditProfileScreen(onBack = { goBack() }, onGuiNote = { notYet(it) })
                    AppScreen.CALC_SETTINGS -> CalculatorSettingsScreen(onBack = { goBack() })
                    AppScreen.GPS_SETTINGS -> GpsSettingsScreen(onBack = { goBack() }, onGuiNote = { notYet(it) })
                    AppScreen.TLRP_SETTINGS -> TlRpSettingsScreen(onBack = { goBack() }, onGuiNote = { notYet(it) })
                    AppScreen.PORTAL -> PortalScreen(onBack = { goBack() }, onGuiNote = { notYet(it) })
                    AppScreen.GROUP_CHAT -> GroupChatScreen(onBack = { goBack() })
                    AppScreen.TL_LOCK -> TlLockScreen(onBack = { goBack() }, onGuiNote = { notYet(it) })
                    AppScreen.CHAT_SETTINGS -> ChatSettingsScreen(onBack = { goBack() }, onGuiNote = { notYet(it) }, onOpenWaFormat = { go(AppScreen.WA_FORMAT) })
                    AppScreen.APP_INFO -> AppInfoScreen(onBack = { goBack() }, onGuiNote = { notYet(it) }, onCheckUpdate = { manualCheck() }, onOpenSystemStorage = { go(AppScreen.SYSTEM_STORAGE) })
                    AppScreen.SYSTEM_STORAGE -> SystemStorageScreen(onBack = { goBack() }, onGuiNote = { notYet(it) })
                    AppScreen.WA_FORMAT -> WaFormatScreen(onBack = { goBack() }, onGuiNote = { notYet(it) })
                    AppScreen.TOM_COUNT -> TomCountScreen(onBack = { goBack() }, onGuiNote = { notYet(it) })
                    AppScreen.PHOTO_VIEWER -> PhotoViewerScreen(onBack = { goBack() }, onGuiNote = { notYet(it) })
                    AppScreen.LOGIN -> LoginScreen(onBack = { goBack() }, onSignUp = { go(AppScreen.SIGNUP) }, onGuiNote = { notYet(it) })
                    AppScreen.SIGNUP -> SignUpScreen(onBack = { goBack() }, onGuiNote = { notYet(it) })
                    AppScreen.HOME -> Unit
                }
                }
                }
            }
        }
        // Dock sab pages ke UPAR (naya page khulne pe bhi samne rehta hai). Sirf drawer khula ho
        // ya full-screen camera ho to neeche khisak jata hai.
        AnimatedVisibility(
            // Keyboard khula ho to bhi dock chhup jata hai (wo keyboard ke peeche hota).
            visible = drawerState.targetValue == DrawerValue.Closed && screen != AppScreen.CAMERA &&
                screen != AppScreen.LOGIN && screen != AppScreen.SIGNUP && imeBottom == 0.dp,
            enter = slideInVertically(tween(250)) { it } + fadeIn(tween(250)),
            exit = slideOutVertically(tween(250)) { it } + fadeOut(tween(250)),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomNavBar(
                activeNav = activeNav, onNavClick = { tab ->
                    activeNav = tab
                    // v1.66: dock se khula page "seedha" hai — purana back stack saaf,
                    // is ka back seedha Home (uska order).
                    backStack.clear()
                    navBack = false
                    when (tab) {
                        NavTab.HOME -> screen = AppScreen.HOME
                        NavTab.OPENING -> screen = AppScreen.OPENING
                        NavTab.CLOSING -> screen = AppScreen.CLOSING
                        NavTab.PORTAL -> screen = AppScreen.PORTAL
                        // SETTINGS dock se hat gaya hai — drawer/Home se aaye to
                        // bhi yahan kabhi nahi pahunchega; lambda ka when
                        // exhaustive hona lazmi hai is liye else rakha hai.
                        else -> {}
                    }
                }
            )
        }
        // Batch item 7: 63 jaisa silent update bar — 3px gradient line +
        // poori chaurai wali "Updating… X%" strip (safed 94%, laal bold text).
        // 100% par install screen khud khul jati hai, app ka koi button nahi.
        downloadProgress?.let { pct ->
            Column(modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth().statusBarsPadding()) {
                Box(modifier = Modifier.fillMaxWidth().height(3.dp).background(Color(0xFFB02A31).copy(alpha = 0.25f))) {
                    if (pct >= 0) Box(
                        modifier = Modifier.fillMaxWidth(pct / 100f).height(3.dp)
                            .background(Brush.horizontalGradient(listOf(Color(0xFFB02A31), Color(0xFF9E1F25))))
                    ) else Box(modifier = Modifier.fillMaxWidth().height(3.dp).background(Color(0xFF9E1F25)))
                }
                Surface(color = Color.White.copy(alpha = 0.94f), shadowElevation = 4.dp) {
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp), contentAlignment = Alignment.Center) {
                        Text(
                            // v1.63: % na mile (server size na bheje) to sirf
                            // "Updating…" ki bajaye downloaded MB dikhao — 63 ki
                            // tarah progress hamesha NAZAR aaye.
                            text = if (pct >= 0) "Updating\u2026 $pct%"
                                else "Updating\u2026 " + "%.1f".format(Locale.US, downloadBytes / 1048576.0) + " MB",
                            fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9E1F25)
                        )
                    }
                }
            }
        }
        // Batch item 13: update pe confetti — sab se upar, touch nahi rokti.
        if (showConfetti) ConfettiOverlay { showConfetti = false }
        // v1.62: splash sab se upar (cold open pe 1.5s).
        if (showSplash) SplashScreen()
    }

    dialog?.let { body ->
        AlertDialog(
            onDismissRequest = { dialog = null },
            title = { Text("Metro +", fontWeight = FontWeight.Bold) },
            text = { Text(body) },
            confirmButton = { TextButton(onClick = { dialog = null }) { Text("OK") } }
        )
    }
}

@Composable
private fun BottomNavBar(modifier: Modifier = Modifier, activeNav: NavTab, onNavClick: (NavTab) -> Unit) {
    // Dock (user ka diya design): SAFED pill bar, baaki items sirf rangeen 3D icon; jo select ho
    // wo LAAL pill ban jata hai — safed gol me 3D icon + poora naam — pill smooth phail/simat jati hai.
    // v1.63: dock se Settings hata diya (uska order) — sirf 4 rahenge.
    // Settings ab bhi drawer ki "app Setting" row aur Home ki Settings entry se khulti hai.
    // v1.64 (uska order): Home pehle, phir Opening, phir Closing, aakhir me Portal.
    val tabs = listOf(
        Triple(NavTab.HOME, DockIcon.HOME, "Home"),
        Triple(NavTab.OPENING, DockIcon.OPENING, "Opening"),
        Triple(NavTab.CLOSING, DockIcon.CLOSING, "Closing"),
        Triple(NavTab.PORTAL, DockIcon.PORTAL, "Portal")
    )
    val shape = RoundedCornerShape(999.dp)
    Row(
        modifier = modifier.fillMaxWidth().navigationBarsPadding().padding(start = 14.dp, end = 14.dp, bottom = 12.dp)
            .height(64.dp)
            .shadow(14.dp, shape, ambientColor = Color(0xFF3C1416).copy(alpha = 0.22f), spotColor = Color(0xFF3C1416).copy(alpha = 0.22f))
            .background(Brush.verticalGradient(listOf(Color.White, Color(0xFFFBF5F5))), shape)
            .border(1.dp, Color(0xFF14181C).copy(alpha = 0.07f), shape)
            .padding(horizontal = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        tabs.forEach { (tab, icon, label) ->
            BottomNavItem(icon = icon, label = label, selected = activeNav == tab, onClick = { onNavClick(tab) })
        }
    }
}

@Composable
private fun BottomNavItem(icon: DockIcon, label: String, selected: Boolean, onClick: () -> Unit) {
    val pillAlpha by animateFloatAsState(if (selected) 1f else 0f, tween(300), label = "dockPill")
    val elevation by animateDpAsState(if (selected) 6.dp else 0.dp, tween(300), label = "dockElev")
    Row(
        modifier = Modifier.height(46.dp)
            .shadow(elevation, CircleShape, ambientColor = Color(0xFFB02A31).copy(alpha = 0.45f), spotColor = Color(0xFFB02A31).copy(alpha = 0.45f))
            .background(Brush.verticalGradient(listOf(Color(0xFFE0454D).copy(alpha = pillAlpha), Color(0xFFA81E26).copy(alpha = pillAlpha))), CircleShape)
            .clip(CircleShape)
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onClick() }
            .animateContentSize(tween(300, easing = FastOutSlowInEasing))
            .padding(start = if (selected) 5.dp else 7.dp, end = if (selected) 15.dp else 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (selected) {
            // Laal pill me icon safed gol plate pe (ghar ki laal chhat laal pe na chhupe).
            Box(
                modifier = Modifier.size(36.dp).shadow(3.dp, CircleShape)
                    .background(Brush.radialGradient(0f to Color.White, 0.6f to Color(0xFFFFF4F4), 1f to Color(0xFFF1DADB)), CircleShape),
                contentAlignment = Alignment.Center
            ) { Image(rememberDockIcon(icon), contentDescription = label, modifier = Modifier.size(28.dp)) }
            Spacer(modifier = Modifier.width(7.dp))
            Text(label, fontSize = 13.5.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, maxLines = 1, softWrap = false)
        } else {
            Image(rememberDockIcon(icon), contentDescription = label, modifier = Modifier.size(32.dp))
        }
    }
}

// JazzCash-style slider — user ki screenshot ke mutabiq: kala→laal→gulabi gradient header,
// gol photo, naam + ID + station, My Account dropdown, 4 settings rows.
// Width 80% (na half, na full); ModalNavigationDrawer ka swipe finger ke sath chalta hai.
@Composable
private fun ProfileDrawer(
    onClose: () -> Unit,
    onEdit: () -> Unit,
    onMenu: (String) -> Unit,
    onCheckUpdate: () -> Unit,
    onLogout: () -> Unit
) {
    var myAccountExpanded by remember { mutableStateOf(false) }
    val ink = Color(0xFF14181C)
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF6F0F0)).verticalScroll(rememberScrollState())) {
        // Header: kala \u2192 gehra laal \u2192 laal \u2192 gulabi diagonal gradient, chamak ke gole + do lakeer-gole.
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier.fillMaxWidth().height(172.dp)
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    .background(Brush.linearGradient(0f to Color(0xFF1A0B0D), 0.45f to Color(0xFF8E1820), 0.75f to Color(0xFFD3343C), 1f to Color(0xFFFF8A8F)))
            ) {
                Box(modifier = Modifier.size(200.dp).align(Alignment.TopEnd).offset(x = 70.dp, y = (-80).dp)
                    .background(Brush.radialGradient(0f to Color.White.copy(alpha = 0.28f), 0.7f to Color.Transparent), CircleShape))
                Box(modifier = Modifier.size(220.dp).offset(x = 110.dp, y = (-110).dp).border(1.dp, Color.White.copy(alpha = 0.18f), CircleShape))
                Box(modifier = Modifier.size(160.dp).offset(x = 150.dp, y = (-60).dp).border(1.dp, Color.White.copy(alpha = 0.14f), CircleShape))
                Row(modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(start = 12.dp, end = 14.dp, top = 10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(38.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.16f)).clickable { onClose() },
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(20.dp)) }
                    Row(
                        modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(Color.White.copy(alpha = 0.16f))
                            .border(1.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(999.dp))
                            .clickable { onEdit() }.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Edit, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Edit Profile", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
            // Gol photo: safed ring + saya, neeche-daein laal camera badge.
            Box(modifier = Modifier.align(Alignment.BottomStart).offset(x = 20.dp, y = 54.dp)) {
                Box(
                    modifier = Modifier.size(108.dp).shadow(10.dp, CircleShape)
                        .background(Color.White, CircleShape).padding(4.dp).clip(CircleShape).background(Color.Black),
                    contentAlignment = Alignment.Center
                ) { Image(painterResource(id = R.drawable.logo_hd), contentDescription = "Profile", contentScale = ContentScale.Crop, colorFilter = com.metroplus.app.ui.LogoEnhance, modifier = Modifier.fillMaxSize()) }
                Box(
                    modifier = Modifier.align(Alignment.BottomEnd).size(32.dp).shadow(4.dp, CircleShape)
                        .background(Brush.linearGradient(listOf(Color(0xFFE0454D), Color(0xFF9E1F25))), CircleShape)
                        .border(2.5.dp, Color.White, CircleShape).clickable { onEdit() },
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.PhotoCamera, contentDescription = "Change photo", tint = Color.White, modifier = Modifier.size(15.dp)) }
            }
        }
        Spacer(modifier = Modifier.height(64.dp))
        // Naam + role chip.
        Row(modifier = Modifier.padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("ARYAN", fontSize = 23.sp, fontWeight = FontWeight.ExtraBold, color = ink, letterSpacing = (-0.3).sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "OPERATOR", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF9E1F25), letterSpacing = 1.sp,
                modifier = Modifier.background(Color(0xFFB02A31).copy(alpha = 0.10f), RoundedCornerShape(999.dp))
                    .border(1.dp, Color(0xFFB02A31).copy(alpha = 0.22f), RoundedCornerShape(999.dp)).padding(horizontal = 9.dp, vertical = 3.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        // ID + station ek safed card me.
        DrawerCard {
            DrawerInfoRow(Icons.Filled.Badge, listOf(Color(0xFFFFB547), Color(0xFFF08C00)), "Employee ID", "\u2014")
            DrawerDivider()
            DrawerInfoRow(Icons.Filled.LocationOn, listOf(Color(0xFFFF6B70), Color(0xFFC1121F)), "Station", "Shaheed-e-Millat")
        }
        DrawerSection("ACCOUNT")
        DrawerCard {
            DrawerMenuRow(
                icon = Icons.Filled.Person, label = "My Account", colors = listOf(Color(0xFFFF6B70), Color(0xFFC1121F)),
                trailing = if (myAccountExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                onClick = { myAccountExpanded = !myAccountExpanded }
            )
            AnimatedVisibility(visible = myAccountExpanded) {
                Column {
                    DrawerDivider()
                    DrawerMenuRow(icon = Icons.Filled.Edit, label = "Edit Profile", colors = listOf(Color(0xFFFF8FA3), Color(0xFFD6336C)), onClick = onEdit)
                    DrawerDivider()
                    DrawerMenuRow(icon = Icons.Filled.Logout, label = "Log Out", colors = listOf(Color(0xFFFF7A6B), Color(0xFFE53935)), labelColor = Color(0xFFE53935), onClick = onLogout)
                }
            }
        }
        DrawerSection("SETTINGS")
        DrawerCard {
            // Dark step 2 (v1.47): pichhla step bhi uski nazar me halka tha —
            // is dafa waazeh gehra (icons pe safed icon hai, is liye gehra
            // theek rahega).
            DrawerMenuRow(icon = Icons.Filled.Calculate, label = "Calculator setting", colors = listOf(Color(0xFFD97706), Color(0xFF92400E)), onClick = { onMenu("Calculator setting") })
            DrawerDivider()
            DrawerMenuRow(icon = Icons.Filled.GpsFixed, label = "Gps setting", colors = listOf(Color(0xFF2563EB), Color(0xFF1E3A8A)), onClick = { onMenu("Gps setting") })
            DrawerDivider()
            DrawerMenuRow(icon = Icons.Filled.AdminPanelSettings, label = "Tl/Rp access setting", colors = listOf(Color(0xFF6D28D9), Color(0xFF4C1D95)), onClick = { onMenu("Tl/Rp access setting") })
            DrawerDivider()
            // "app Setting" row wapas (uske 'Ok kro' pe) — pehle ghalat
            // samajh ke hata di thi; ab dobara 4 rows hain.
            // v1.65: label "app Setting" → "Settings" (uska order).
            DrawerMenuRow(icon = Icons.Filled.Settings, label = "Settings", colors = listOf(Color(0xFFFF7EB0), Color(0xFFE91E63)), onClick = { onMenu("app Setting") })
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Neeche: version pill + laal "Check update" button.
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(
                modifier = Modifier.background(Color.White, RoundedCornerShape(999.dp)).border(1.dp, ink.copy(alpha = 0.06f), RoundedCornerShape(999.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Info, contentDescription = null, tint = Color(0xFF9AA1AB), modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(5.dp))
                Text("Version 1.67", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF6B7280))
            }
            Row(
                modifier = Modifier.shadow(6.dp, RoundedCornerShape(999.dp), ambientColor = Color(0xFFB02A31), spotColor = Color(0xFFB02A31))
                    .background(Brush.linearGradient(listOf(Color(0xFFE0454D), Color(0xFF9E1F25))), RoundedCornerShape(999.dp))
                    .clickable { onCheckUpdate() }.padding(horizontal = 14.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Check update", fontSize = 12.5.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        Text("Metro + \u00b7 Developed by Mr. Aryan", fontSize = 11.sp, color = Color(0xFF9AA1AB), modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        Spacer(modifier = Modifier.navigationBarsPadding().height(20.dp))
    }
}

@Composable
private fun DrawerSection(title: String) {
    Text(title, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF9A8F90), letterSpacing = 1.4.sp,
        modifier = Modifier.padding(start = 24.dp, top = 18.dp, bottom = 8.dp))
}

@Composable
private fun DrawerCard(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp)
            .shadow(4.dp, RoundedCornerShape(18.dp), ambientColor = Color(0xFF3C1419).copy(alpha = 0.10f), spotColor = Color(0xFF3C1419).copy(alpha = 0.10f))
            .background(Color.White, RoundedCornerShape(18.dp)).clip(RoundedCornerShape(18.dp))
    ) { content() }
}

@Composable
private fun DrawerDivider() {
    Box(modifier = Modifier.fillMaxWidth().padding(start = 64.dp, end = 14.dp).height(1.dp).background(Color(0xFF14181C).copy(alpha = 0.06f)))
}

// Rangeen gradient tile me safed icon (har row ka apna rang).
@Composable
private fun DrawerIconTile(icon: androidx.compose.ui.graphics.vector.ImageVector, colors: List<Color>) {
    // v1.56: shared 3D tile (upar chamak + kinari), wahi rang.
    com.metroplus.app.ui.Icon3DTile(icon, colors.last(), colors = colors)
}

@Composable
private fun DrawerInfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, colors: List<Color>, label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 11.dp), verticalAlignment = Alignment.CenterVertically) {
        DrawerIconTile(icon, colors)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF9AA1AB))
            Text(value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF14181C))
        }
    }
}

@Composable
private fun DrawerMenuRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    colors: List<Color>,
    trailing: androidx.compose.ui.graphics.vector.ImageVector? = Icons.Filled.ChevronRight,
    labelColor: Color = Color(0xFF14181C),
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(horizontal = 14.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DrawerIconTile(icon, colors)
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = labelColor, modifier = Modifier.weight(1f))
        if (trailing != null) Icon(trailing, contentDescription = null, tint = Color(0xFFB8BEC6), modifier = Modifier.size(22.dp))
    }
}
