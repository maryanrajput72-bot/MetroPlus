package com.metroplus.app

import android.os.Bundle
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
import com.metroplus.app.ui.rememberDockIcon
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
import androidx.compose.material.icons.filled.MonetizationOn
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
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.rememberDrawerState
import com.metroplus.app.ui.ClosingScreen
import com.metroplus.app.ui.GpsCameraScreen
import com.metroplus.app.ui.MoneyCalculatorScreen
import com.metroplus.app.ui.OpeningScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
enum class AppScreen { HOME, MONEY, OPENING, CLOSING, CAMERA, SETTINGS, TOM }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { MetroPlusTheme { MetroPlusApp() } }
    }

    override fun onResume() {
        super.onResume()
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
    // Update mile to app ke andar dialog aata hai; download bhi app ke andar
    // hoti hai (koi notification download nahi), progress dialog me dikhta hai.
    var downloadProgress by remember { mutableStateOf<Int?>(null) }
    fun startDownload(apkUrl: String) {
        downloadProgress = 0
        UpdateManager.downloadUpdate(
            context.applicationContext, apkUrl,
            onProgress = { pct -> downloadProgress = pct },
            onComplete = { file ->
                // Pehle 100% dikhao (shikayat thi: 100 kabhi nazar nahi aata tha),
                // phir silent install — koi button, koi dialog nahi.
                downloadProgress = 100
                Handler(Looper.getMainLooper()).postDelayed({
                    downloadProgress = null
                    if (UpdateManager.canInstall(context.applicationContext)) {
                        UpdateManager.installUpdate(context.applicationContext, file)
                    } else {
                        // Nayi app ke liye "Install unknown apps" ijazat EK dafa darkar hai
                        // (purani app ki ijazat nayi app par nahi chalti) — settings seedha
                        // khol do; wapas aate hi onResume install khud khol dega.
                        UpdateManager.pendingInstallFile = file
                        UpdateManager.openUnknownSourcesSettings(context.applicationContext)
                    }
                }, 900)
            },
            onError = {
                // Error par bar chhup jata hai, koi error popup nahi.
                downloadProgress = null
            }
        )
    }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(5000)
        UpdateManager.checkForUpdate(
            context.applicationContext,
            // Update mile to BINA button dabaye khud download shuru (purana silent system).
            onUpdate = { versionName, apkUrl -> startDownload(apkUrl) }
        )
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var activeNav by remember { mutableStateOf(NavTab.HOME) }
    var screen by remember { mutableStateOf(AppScreen.HOME) }
    // Back pe slide-out ke dauran bhi wahi page dikhe (warna khali box slide hota tha).
    var lastSubScreen by remember { mutableStateOf(AppScreen.MONEY) }
    LaunchedEffect(screen) { if (screen != AppScreen.HOME) lastSubScreen = screen }
    // Camera jahan se khula, back pe wahin wapas (Opening → GPS → Camera → back = Opening).
    var cameraFrom by remember { mutableStateOf(AppScreen.HOME) }
    fun backHome() { screen = AppScreen.HOME; activeNav = NavTab.HOME }
    var dialog by remember { mutableStateOf<String?>(null) }
    fun toast(msg: String) { Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() }
    fun notYet(name: String) { dialog = "$name\n\nGUI phase: ye screen abhi sirf design me hai, iska feature agle step me banega." }
    fun manualCheck() {
        UpdateManager.checkForUpdate(
            context.applicationContext,
            onUpdate = { versionName, apkUrl -> startDownload(apkUrl) },
            onNoUpdate = { toast("App pehle hi latest version pe hai") },
            onError = { toast("Update check nahi ho saka, internet check karein") }
        )
    }
    fun closeDrawer() { scope.launch { drawerState.close() } }

    BackHandler(enabled = drawerState.isOpen) { closeDrawer() }

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
                    onEdit = { closeDrawer(); notYet("Edit Profile") },
                    onMenu = { name ->
                        closeDrawer()
                        if (name == "app Setting") { activeNav = NavTab.SETTINGS; screen = AppScreen.SETTINGS } else notYet(name)
                    },
                    onCheckUpdate = { closeDrawer(); manualCheck() },
                    onLogout = { closeDrawer(); toast("GUI phase: logout ka feature baad me") }
                )
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize().background(HomeBackground)) {
            HomeScreen(
                onProfileClick = { scope.launch { drawerState.open() } },
                onChatClick = { notYet("Group Chat") },
                onRefreshClick = { manualCheck() },
                onFeatureClick = {
                    if (it == "Money Calc" || it == "Money Calculator") screen = AppScreen.MONEY
                    else if (it == "Opening") { activeNav = NavTab.OPENING; screen = AppScreen.OPENING }
                    else if (it == "Closing") { activeNav = NavTab.CLOSING; screen = AppScreen.CLOSING }
                    else if (it == "GPS Camera") { cameraFrom = AppScreen.HOME; screen = AppScreen.CAMERA }
                    else if (it == "Settings") { activeNav = NavTab.SETTINGS; screen = AppScreen.SETTINGS }
                    else if (it == "Tom Data") screen = AppScreen.TOM
                    else notYet(it)
                }
            )
            // Download progress: upar patli line (progress ke % ke barabar bharti hai) + sirf
            // "Updating…%" text ke neeche background pill; baqi jagah transparent taake neeche
            // page bhi nazar aaye. Cancel NAHI hai (uske order pe hata diya); 100% par install
            // screen khud khul jati hai, dobara koi button nahi dabana parta.
            downloadProgress?.let { pct ->
                Column(modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth().statusBarsPadding()) {
                    if (pct >= 0) LinearProgressIndicator(progress = { pct / 100f }, modifier = Modifier.fillMaxWidth().height(3.dp), color = Color(0xFF9E1F25), trackColor = Color(0xFFB02A31).copy(alpha = 0.25f))
                    else LinearProgressIndicator(modifier = Modifier.fillMaxWidth().height(3.dp), color = Color(0xFF9E1F25), trackColor = Color(0xFFB02A31).copy(alpha = 0.25f))
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Surface(shape = RoundedCornerShape(999.dp), color = Color.White.copy(alpha = 0.94f), shadowElevation = 4.dp) {
                            Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (pct >= 0) "Updating\u2026 $pct%" else "Updating\u2026",
                                    fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9E1F25)
                                )                            }
                        }
                    }
                }
            }
        }
    }
        // Sub-screen overlay: page right se left smooth slide hoke aata hai (350ms),
        // back pe wapas right ki taraf slide hoke jata hai. Aage ke sab pages isi tareeqe se khulenge.
        AnimatedVisibility(
            visible = screen != AppScreen.HOME,
            enter = slideInHorizontally(animationSpec = tween(350, easing = FastOutSlowInEasing)) { it } + fadeIn(animationSpec = tween(350)),
            exit = slideOutHorizontally(animationSpec = tween(350, easing = FastOutSlowInEasing)) { it } + fadeOut(animationSpec = tween(350)),
            modifier = Modifier.fillMaxSize()
        ) {
            val shown = if (screen != AppScreen.HOME) screen else lastSubScreen
            // Dock har page pe samne rehta hai — is liye page ka content dock ke upar khatam hota hai
            // (camera full-screen hai, wahan dock nahi).
            val dockSpace = if (shown == AppScreen.CAMERA) Modifier else Modifier.navigationBarsPadding().padding(bottom = 76.dp)
            Box(modifier = Modifier.fillMaxSize().background(HomeBackground).then(dockSpace)) {
                when (shown) {
                    AppScreen.MONEY -> MoneyCalculatorScreen(onBack = { backHome() }, onGuiNote = { notYet(it) })
                    AppScreen.OPENING -> OpeningScreen(onBack = { backHome() }, onGuiNote = { notYet(it) }, onOpenCamera = { cameraFrom = AppScreen.OPENING; screen = AppScreen.CAMERA })
                    AppScreen.CLOSING -> ClosingScreen(onBack = { backHome() }, onGuiNote = { notYet(it) })
                    AppScreen.CAMERA -> GpsCameraScreen(onBack = { if (cameraFrom == AppScreen.OPENING) screen = AppScreen.OPENING else backHome() }, onGuiNote = { notYet(it) })
                    AppScreen.SETTINGS -> SettingsScreen(onBack = { backHome() }, onGuiNote = { notYet(it) })
                    AppScreen.TOM -> TomDataScreen(onBack = { backHome() }, onGuiNote = { notYet(it) })
                    AppScreen.HOME -> Unit
                }
            }
        }
        // Dock sab pages ke UPAR (naya page khulne pe bhi samne rehta hai). Sirf drawer khula ho
        // ya full-screen camera ho to neeche khisak jata hai.
        AnimatedVisibility(
            visible = drawerState.targetValue == DrawerValue.Closed && screen != AppScreen.CAMERA,
            enter = slideInVertically(tween(250)) { it } + fadeIn(tween(250)),
            exit = slideOutVertically(tween(250)) { it } + fadeOut(tween(250)),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomNavBar(
                activeNav = activeNav, onNavClick = { tab ->
                    activeNav = tab
                    when (tab) {
                        NavTab.HOME -> screen = AppScreen.HOME
                        NavTab.SETTINGS -> screen = AppScreen.SETTINGS
                        NavTab.OPENING -> screen = AppScreen.OPENING
                        NavTab.CLOSING -> screen = AppScreen.CLOSING
                        NavTab.PORTAL -> notYet("GCS Portal")
                    }
                }
            )
        }
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
    val tabs = listOf(
        Triple(NavTab.SETTINGS, DockIcon.SETTINGS, "Settings"),
        Triple(NavTab.OPENING, DockIcon.OPENING, "Opening"),
        Triple(NavTab.HOME, DockIcon.HOME, "Home"),
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

// JazzCash-style slider — user ki screenshot ke mutabiq: green gradient header,
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
                ) { Image(painterResource(id = R.drawable.logo_hd), contentDescription = "Profile", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()) }
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
            DrawerMenuRow(icon = Icons.Filled.Calculate, label = "Calculator setting", colors = listOf(Color(0xFFFFC04D), Color(0xFFF08C00)), onClick = { onMenu("Calculator setting") })
            DrawerDivider()
            DrawerMenuRow(icon = Icons.Filled.GpsFixed, label = "Gps setting", colors = listOf(Color(0xFF5AB0FF), Color(0xFF1A6FD8)), onClick = { onMenu("Gps setting") })
            DrawerDivider()
            DrawerMenuRow(icon = Icons.Filled.AdminPanelSettings, label = "Tl/Rp access setting", colors = listOf(Color(0xFFA78BFA), Color(0xFF5B3FD0)), onClick = { onMenu("Tl/Rp access setting") })
            DrawerDivider()
            DrawerMenuRow(icon = Icons.Filled.Settings, label = "app Setting", colors = listOf(Color(0xFFFF7EB0), Color(0xFFE91E63)), onClick = { onMenu("app Setting") })
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
                Text("Version 1.35", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF6B7280))
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
    Box(
        modifier = Modifier.size(38.dp).shadow(3.dp, RoundedCornerShape(12.dp), ambientColor = colors.last(), spotColor = colors.last())
            .background(Brush.linearGradient(colors), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) { Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp)) }
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
