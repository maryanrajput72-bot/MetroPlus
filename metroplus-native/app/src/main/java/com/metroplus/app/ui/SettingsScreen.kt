package com.metroplus.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.metroplus.app.R

// Settings & Control (trial 2.1.63 ki settingsScreen se shuru hua).
// v1.65 (uska order "green interface apne hisaab se set karo, WhatsApp Report
// Format hatao, kuch add karna ho to karo, GUI pyara"): poora sabz hata ke naya
// rang — indigo → jamni → gulabi hero; profile card coral → gulabi; QUICK
// SETTINGS ke 4 rangeen dabbe (Profile, Calculator, GPS, TL/RP — drawer wale
// pages hi khulte hain); har group ka apna rangeen dabba; WhatsApp Report Format
// row khatam (page code me hai, bas yahan se rasta nahi). GUI phase: rows note
// ya apna page kholti hain, toggles sirf GUI.

private val SetBg = Color(0xFFF5F3FA)
private val SetInk = Color(0xFF14181C)
private val SetMuted = Color(0xFF6B7280)
private val SetGrad = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6), Color(0xFFEC4899))
private val SetAccent = Color(0xFF7C3AED)

// Toggles ki halat ViewModel me (page band karke kholo to wahi rahe).
class SettingsViewModel : ViewModel() {
    val darkMode = mutableStateOf(false)
    val pushNotifications = mutableStateOf(true)
    val sounds = mutableStateOf(true)
}

@Composable
fun SettingsScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit, onOpenPage: (String) -> Unit = {}) {
    BackHandler { onBack() }
    val vm: SettingsViewModel = viewModel()
    var dark by vm.darkMode
    var push by vm.pushNotifications
    var sound by vm.sounds

    Column(modifier = Modifier.fillMaxSize().background(SetBg).verticalScroll(rememberScrollState())) {
        // Hero: indigo → jamni → gulabi, chamak ke gole, back + done, title + sub,
        // neeche profile card hero ke andar.
        val hShape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
        Box(
            modifier = Modifier.fillMaxWidth().cssShadow(14.dp, hShape, SetAccent.copy(alpha = 0.35f)).clip(hShape)
                .background(cssLinear(135f, 0f to SetGrad[0], 0.55f to SetGrad[1], 1f to SetGrad[2]))
        ) {
            Box(Modifier.matchParentSize()) {
                Glow(220.dp, x = 90.dp, y = (-90).dp, alpha = 0.3f, align = Alignment.TopEnd)
                Glow(150.dp, x = (-60).dp, y = 50.dp, alpha = 0.12f, solid = true, align = Alignment.BottomStart)
                Box(modifier = Modifier.align(Alignment.TopEnd).offset(x = 70.dp, y = 20.dp).size(200.dp).border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape))
            }
            Column(modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    HeaderCircle(Icons.Filled.ArrowBack, "Back", onBack)
                    HeaderCircle(Icons.Filled.Check, "Done", onBack)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Settings", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, letterSpacing = (-0.4).sp)
                        Text("Control your app, profile & alerts", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.White.copy(alpha = 0.85f))
                    }
                    Icon3DTile(Icons.Filled.Settings, SetGrad[1], size = 60.dp, iconSize = 30.dp, corner = 19.dp,
                        colors = listOf(Color.White.copy(alpha = 0.38f), Color.White.copy(alpha = 0.12f)))
                }
                Spacer(modifier = Modifier.height(16.dp))
                ProfileCard { onOpenPage("Edit Profile") }
            }
        }
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            // QUICK SETTINGS: 4 rangeen dabbe (2x2), drawer ke pages kholte hain.
            GroupTitle("QUICK SETTINGS", SetAccent)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                QuickTile("Profile", "Name, station", Icons.Filled.Person, listOf(Color(0xFFFF8A9B), Color(0xFFE11D48)), Modifier.weight(1f)) { onOpenPage("Edit Profile") }
                QuickTile("Calculator", "Notes & coins", Icons.Filled.Calculate, listOf(Color(0xFFFFC94D), Color(0xFFF97316)), Modifier.weight(1f)) { onOpenPage("Calculator setting") }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                QuickTile("GPS", "Photo stamp", Icons.Filled.GpsFixed, listOf(Color(0xFF5EE7F2), Color(0xFF2563EB)), Modifier.weight(1f)) { onOpenPage("Gps setting") }
                QuickTile("TL / RP", "Premium access", Icons.Filled.AdminPanelSettings, listOf(Color(0xFFC084FC), Color(0xFF6D28D9)), Modifier.weight(1f)) { onOpenPage("Tl/Rp access setting") }
            }

            GroupTitle("ACCOUNT", Color(0xFF0D9488))
            SetList(Color(0xFF0D9488)) {
                SetRow(Icons.Filled.Badge, Color(0xFF0D9488), "Supervisor Name", "Not set", divider = true,
                    onClick = { onGuiNote("Supervisor Name") }) {
                    Box(
                        modifier = Modifier.size(34.dp).clip(CircleShape)
                            .background(Brush.linearGradient(listOf(Color(0xFFFFD166), Color(0xFFFF6B8B)))).clickable { onGuiNote("Supervisor Name") },
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = Color.White, modifier = Modifier.size(15.dp)) }
                }
                SetRow(null, Color(0xFFF59E0B), "TL / RP Login", "Log in for premium access", divider = false,
                    onClick = { onOpenPage("TL / RP Login") }, image = R.drawable.img_tl_padlock) { Chevron() }
            }

            // General Preference — tap karo to khule (batch item 9b).
            var genExpanded by remember { mutableStateOf(false) }
            GroupTitle("GENERAL PREFERENCE", Color(0xFFDB2777))
            SetList(Color(0xFFDB2777)) {
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { genExpanded = !genExpanded }.padding(horizontal = 14.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon3DTile(Icons.Filled.Tune, Color(0xFFDB2777))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("General Preference", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = SetInk)
                        Text("Dark mode, notifications, sound", fontSize = 12.5.sp, color = SetMuted)
                    }
                    Icon(
                        if (genExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null, tint = Color(0xFFDB2777), modifier = Modifier.size(22.dp)
                    )
                }
                if (genExpanded) {
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1EEF6)))
                    SetRow(Icons.Filled.DarkMode, Color(0xFF334155), "Dark mode", "Red & black theme (off = white & red)",
                        onClick = { dark = !dark }) { Toggle(dark) { dark = it } }
                    SetRow(Icons.Filled.Notifications, Color(0xFFF59E0B), "Push Notifications", "Chat messages & alerts",
                        onClick = { push = !push }) { Toggle(push) { push = it } }
                    SetRow(Icons.Filled.VolumeUp, Color(0xFFDB2777), "Sounds & Vibration", "Notification sound + vibration", divider = false,
                        onClick = { sound = !sound }) { Toggle(sound) { sound = it } }
                }
            }

            GroupTitle("APP", Color(0xFF2563EB))
            SetList(Color(0xFF2563EB)) {
                // WhatsApp Report Format row v1.65 me hata di (uska order).
                SetRow(Icons.Filled.Sms, Color(0xFF22C55E), "Chat Settings", "Bubble size, colors, background",
                    onClick = { onOpenPage("Chat Settings") }) { Chevron() }
                SetRow(Icons.Filled.Storage, Color(0xFF475569), "System & Storage", "Sync, cache and data",
                    onClick = { onOpenPage("System & Storage") }) { Chevron() }
                SetRow(Icons.Filled.Info, Color(0xFF2563EB), "App Info", "Version, about", divider = false,
                    onClick = { onOpenPage("App Info") }) { Chevron() }
            }
            Spacer(modifier = Modifier.height(20.dp))
            // Logout: laal → gulabi gradient, safed likhai.
            val lShape = RoundedCornerShape(18.dp)
            Row(
                modifier = Modifier.fillMaxWidth().height(54.dp)
                    .cssShadow(10.dp, lShape, Color(0xFFE11D48).copy(alpha = 0.35f)).clip(lShape)
                    .background(cssLinear(120f, 0f to Color(0xFFFF6B6B), 1f to Color(0xFFE11D48)))
                    .clickable { onOpenPage("Logout") },
                horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Logout, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text("Logout from Metro Plus app", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun HeaderCircle(icon: ImageVector, desc: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(42.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.22f))
            .border(1.dp, Color.White.copy(alpha = 0.35f), CircleShape).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) { Icon(icon, contentDescription = desc, tint = Color.White, modifier = Modifier.size(19.dp)) }
}

// Profile card (hero ke andar): coral → gulabi, avatar me asal logo, pehle ROLE
// phir bara naam (batch item 9a — card pe pencil NAHI); tap = Edit Profile page.
@Composable
private fun ProfileCard(onEdit: () -> Unit) {
    val shape = RoundedCornerShape(22.dp)
    Row(
        modifier = Modifier.fillMaxWidth().clip(shape)
            .background(Color.White.copy(alpha = 0.18f)).border(1.dp, Color.White.copy(alpha = 0.35f), shape)
            .clickable { onEdit() }.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(52.dp).cssShadow(6.dp, CircleShape, Color.Black.copy(alpha = 0.25f))
                .background(Color.White, CircleShape).padding(2.5.dp).clip(CircleShape).background(Color.Black),
            contentAlignment = Alignment.Center
        ) { Image(painterResource(id = R.drawable.logo_hd), contentDescription = null, contentScale = ContentScale.Crop, colorFilter = LogoEnhance, modifier = Modifier.fillMaxSize()) }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            // Role ka pehla word, phir bara naam (login step me asli aayenge).
            Text("OPERATOR", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color.White.copy(alpha = 0.85f), letterSpacing = 1.sp)
            Text("Aryan", fontSize = 21.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Text("Shaheed-e-Millat", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.White.copy(alpha = 0.85f))
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
    }
}

// Quick setting dabba: rangeen gradient, 3D icon, title + sub.
@Composable
private fun QuickTile(title: String, sub: String, icon: ImageVector, colors: List<Color>, modifier: Modifier, onClick: () -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = modifier.cssShadow(10.dp, shape, colors.last().copy(alpha = 0.3f)).clip(shape)
            .background(cssLinear(135f, 0f to colors[0].lighter(0.55f), 1f to colors[0].lighter(0.15f)))
            .border(1.dp, colors.last().copy(alpha = 0.18f), shape)
            .clickable { onClick() }.padding(14.dp)
    ) {
        Icon3DTile(icon, colors.last(), size = 42.dp, iconSize = 22.dp, corner = 13.dp, colors = listOf(colors[0], colors[1]))
        Spacer(modifier = Modifier.height(10.dp))
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = SetInk)
        Text(sub, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = colors.last().darker(0.25f))
    }
}

@Composable
private fun GroupTitle(text: String, accent: Color) {
    Row(modifier = Modifier.padding(start = 4.dp, top = 20.dp, bottom = 9.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(width = 4.dp, height = 15.dp).background(Brush.verticalGradient(listOf(accent.lighter(0.3f), accent.darker(0.15f))), RoundedCornerShape(2.dp)))
        Spacer(modifier = Modifier.width(7.dp))
        Text(text, fontSize = 11.5.sp, fontWeight = FontWeight.ExtraBold, color = accent.darker(0.25f), letterSpacing = 1.sp)
    }
}

// Group ka safed dabba: upar halki rangeen chamak + us rang ka saya.
@Composable
private fun SetList(accent: Color, content: @Composable ColumnScope.() -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = Modifier.fillMaxWidth()
            .cssShadow(8.dp, shape, accent.copy(alpha = 0.14f))
            .background(Brush.verticalGradient(listOf(accent.copy(alpha = 0.05f), Color.White, Color.White)), shape)
            .border(1.dp, accent.copy(alpha = 0.12f), shape).clip(shape),
        content = content
    )
}

// Row: 3D icon tile (ya tasveer), label + desc, daein control; rows ke beech halki lakeer.
@Composable
private fun SetRow(
    icon: ImageVector?, iconTint: Color, label: String, desc: String,
    divider: Boolean = true, onClick: () -> Unit, image: Int? = null, trailing: @Composable () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(horizontal = 14.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (image != null) Box(
                modifier = Modifier.size(38.dp).background(Brush.linearGradient(listOf(Color(0xFFFFF4D6), Color(0xFFFFE2A8))), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) { Image(painterResource(id = image), contentDescription = null, contentScale = ContentScale.Fit, modifier = Modifier.size(28.dp)) }
            else if (icon != null) Icon3DTile(icon, iconTint)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(label, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = SetInk)
                Text(desc, fontSize = 12.5.sp, color = SetMuted)
            }
            Spacer(modifier = Modifier.width(8.dp))
            trailing()
        }
        if (divider) Box(modifier = Modifier.fillMaxWidth().padding(start = 64.dp).height(1.dp).background(Color(0xFFF1EEF6)))
    }
}

@Composable
private fun Chevron() {
    Box(modifier = Modifier.size(28.dp).background(SetAccent.copy(alpha = 0.08f), CircleShape), contentAlignment = Alignment.Center) {
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = SetAccent, modifier = Modifier.size(18.dp))
    }
}

// Toggle: 44x24, off #E4DEDE, 18dp safed knob (saya), 20dp khiskta hai.
// v1.65: "on" ab jamni → gulabi gradient (Settings ka naya rang).
@Composable
private fun Toggle(on: Boolean, onChange: (Boolean) -> Unit) {
    val knob by animateDpAsState(if (on) 20.dp else 0.dp, tween(200), label = "toggleKnob")
    Box(
        modifier = Modifier.size(44.dp, 24.dp).clip(RoundedCornerShape(24.dp))
            .background(if (on) Brush.horizontalGradient(listOf(SetGrad[1], SetGrad[2])) else Brush.linearGradient(listOf(Color(0xFFE4DEDE), Color(0xFFE4DEDE))))
            .clickable { onChange(!on) }
    ) {
        Box(
            modifier = Modifier.padding(3.dp).offset(x = knob).size(18.dp)
                .shadow(3.dp, CircleShape).background(Color.White, CircleShape)
        )
    }
}
