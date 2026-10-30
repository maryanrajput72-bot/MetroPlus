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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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

// Settings & Control — trial 2.1.63 ke settingsScreen ki EXACT copy (computed CSS se naapa):
// bg #F6F2F2, white header (44dp gol buttons, 18sp title), laal profile card (kona 24),
// group titles 11.5sp, white lists (kona 20) me 65dp rows, 38dp rangeen icon tiles, laal toggles,
// neeche laal "Logout" button. GUI phase: rows sirf note dikhate hain, toggles sirf GUI me chalte hain.

private val SetBg = Color(0xFFF6F2F2)
private val SetInk = Color(0xFF14181C)
private val SetMuted = Color(0xFF6B7280)
private val SetRed = Color(0xFF9E1F25)

// Toggles ki halat ViewModel me (page band karke kholo to wahi rahe).
class SettingsViewModel : ViewModel() {
    val darkMode = mutableStateOf(false)
    val pushNotifications = mutableStateOf(true)
    val sounds = mutableStateOf(true)
}

@Composable
fun SettingsScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit) {
    BackHandler { onBack() }
    val vm: SettingsViewModel = viewModel()
    var dark by vm.darkMode
    var push by vm.pushNotifications
    var sound by vm.sounds

    Column(modifier = Modifier.fillMaxSize().background(SetBg)) {
        // Header: white 92%, padding 16/20, back | "Settings & Control" | check.
        Column(modifier = Modifier.fillMaxWidth().background(Color.White.copy(alpha = 0.92f))) {
            Row(
                modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
            ) {
                HeaderCircle(Icons.Filled.ArrowBack, "Back", onBack)
                Text("Settings & Control", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = SetInk, letterSpacing = (-0.3).sp)
                HeaderCircle(Icons.Filled.Check, "Done", onBack)
            }
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(SetInk.copy(alpha = 0.06f)))
        }
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            ProfileCard { onGuiNote("Edit Profile") }
            Spacer(modifier = Modifier.height(18.dp))
            SetList {
                SetRow(Icons.Filled.Badge, Color(0xFFF2E2E3), SetRed, "Supervisor Name", "Not set", divider = false,
                    onClick = { onGuiNote("Supervisor Name") }) {
                    Box(
                        modifier = Modifier.size(34.dp).background(Color(0xFFF4F0F0), CircleShape).clickable { onGuiNote("Supervisor Name") },
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = Color(0xFFC9C2C2), modifier = Modifier.size(15.dp)) }
                }
            }
            GroupTitle("PREMIUM ACCESS")
            SetList {
                SetRow(null, Color(0xFFF6EFDF), Color.Unspecified, "TL / RP Login", "Log in for premium access", divider = false,
                    onClick = { onGuiNote("TL / RP Login") }, image = R.drawable.img_tl_padlock) { Chevron() }
            }
            GroupTitle("GENERAL PREFERENCES")
            SetList {
                SetRow(Icons.Filled.DarkMode, Color(0xFFE0E1E3), Color(0xFF111827), "Dark mode", "Red & black theme (off = white & red)",
                    onClick = { dark = !dark }) { Toggle(dark) { dark = it } }
                SetRow(Icons.Filled.Notifications, Color(0xFFF2E2E3), SetRed, "Push Notifications", "Chat messages & alerts",
                    onClick = { push = !push }) { Toggle(push) { push = it } }
                SetRow(Icons.Filled.VolumeUp, Color(0xFFF2E2E3), SetRed, "Sounds & Vibration", "Notification sound + vibration", divider = false,
                    onClick = { sound = !sound }) { Toggle(sound) { sound = it } }
            }
            GroupTitle("APP")
            SetList {
                SetRow(Icons.Filled.GpsFixed, Color(0xFFDEF8FF), Color(0xFF00C6FF), "Metro & GPS Settings", "GPS accuracy, refresh rate, currency",
                    onClick = { onGuiNote("Metro & GPS Settings") }) { Chevron() }
                SetRow(Icons.Filled.Storage, Color(0xFFF4E9FE), Color(0xFFA855F7), "System & Storage", "Sync data, clear cache",
                    onClick = { onGuiNote("System & Storage") }) { Chevron() }
                SetRow(Icons.Filled.Sms, Color(0xFFE2F7EA), Color(0xFF22C55E), "Chat Settings", "Bubble size, colors, background",
                    onClick = { onGuiNote("Chat Settings") }) { Chevron() }
                SetRow(Icons.Filled.Chat, Color(0xFFE3F9EB), Color(0xFF25D366), "WhatsApp Report Format", "Edit opening & closing templates",
                    onClick = { onGuiNote("WhatsApp Report Format") }) { Chevron() }
                SetRow(Icons.Filled.Info, Color(0xFFF2E2E3), SetRed, "App Info", "Version, about", divider = false,
                    onClick = { onGuiNote("App Info") }) { Chevron() }
            }
            Spacer(modifier = Modifier.height(18.dp))
            // Logout: white, laal halki border (#FDE0E0), kona 18, laal icon + 15sp text beech me.
            Row(
                modifier = Modifier.fillMaxWidth().height(52.dp)
                    .cssShadow(4.dp, RoundedCornerShape(18.dp), Color(0xFFE53935).copy(alpha = 0.08f))
                    .background(Color.White, RoundedCornerShape(18.dp))
                    .border(1.dp, Color(0xFFFDE0E0), RoundedCornerShape(18.dp))
                    .clip(RoundedCornerShape(18.dp)).clickable { onGuiNote("Logout") },
                horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Logout, contentDescription = null, tint = Color(0xFFE53935), modifier = Modifier.size(17.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text("Logout from Metro Plus app", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFE53935))
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun HeaderCircle(icon: ImageVector, desc: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(44.dp).clip(CircleShape).background(Color(0xFFF4F0F0)).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) { Icon(icon, contentDescription = desc, tint = SetInk, modifier = Modifier.size(19.dp)) }
}

// Laal profile card: 135° gradient #BF2E35→#9E1F25→#7A171C, kona 24, padding 18, watermark.
@Composable
private fun ProfileCard(onEdit: () -> Unit) {
    val shape = RoundedCornerShape(24.dp)
    Box(
        modifier = Modifier.fillMaxWidth()
            .cssShadow(14.dp, shape, SetRed.copy(alpha = 0.3f)).clip(shape)
            .background(cssLinear(135f, 0f to Color(0xFFBF2E35), 0.55f to SetRed, 1f to Color(0xFF7A171C)))
    ) {
        Watermark(143.dp, x = 257.dp, y = (-25).dp, alpha = 0.2f)
        Row(modifier = Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(58.dp).background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(18.dp))
                    .border(2.dp, Color.White.copy(alpha = 0.45f), RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) { Text("OP", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color.White) }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                // GUI demo naam; Login step me asli profile se aayega.
                Text("Aryan", fontSize = 19.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.background(Color.White.copy(alpha = 0.95f), RoundedCornerShape(20.dp)).padding(horizontal = 10.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Shield, contentDescription = null, tint = Color(0xFF7A171C), modifier = Modifier.size(11.dp))
                    Spacer(modifier = Modifier.width(5.dp))
                    Text("Staff Account", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF7A171C))
                }
            }
            Box(
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(14.dp)).background(Color.White.copy(alpha = 0.18f))
                    .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(14.dp)).clickable { onEdit() },
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Filled.Edit, contentDescription = "Edit Profile", tint = Color.White, modifier = Modifier.size(16.dp)) }
        }
    }
}

@Composable
private fun GroupTitle(text: String) {
    Text(
        text, fontSize = 11.5.sp, fontWeight = FontWeight.ExtraBold, color = SetMuted, letterSpacing = 1.sp,
        modifier = Modifier.padding(start = 6.dp, top = 18.dp, bottom = 8.dp)
    )
}

@Composable
private fun SetList(content: @Composable ColumnScope.() -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = Modifier.fillMaxWidth()
            .cssShadow(4.dp, shape, Color(0xFF281415).copy(alpha = 0.05f))
            .background(Color.White, shape).border(1.dp, SetInk.copy(alpha = 0.06f), shape).clip(shape),
        content = content
    )
}

// 65dp row: 38dp tile (kona 12), 15sp Bold label + 12.5sp desc, daein control; rows ke beech #F2F0F0 lakeer.
@Composable
private fun SetRow(
    icon: ImageVector?, tileBg: Color, iconTint: Color, label: String, desc: String,
    divider: Boolean = true, onClick: () -> Unit, image: Int? = null, trailing: @Composable () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(horizontal = 14.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(38.dp).background(tileBg, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                if (image != null) Image(painterResource(id = image), contentDescription = null, contentScale = ContentScale.Fit, modifier = Modifier.size(28.dp))
                else if (icon != null) Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(label, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = SetInk)
                Text(desc, fontSize = 12.5.sp, color = SetMuted)
            }
            Spacer(modifier = Modifier.width(8.dp))
            trailing()
        }
        if (divider) Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF2F0F0)))
    }
}

@Composable
private fun Chevron() {
    Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color(0xFFB4BCB2), modifier = Modifier.size(20.dp))
}

// Trial toggle: 44x24, off #E4DEDE, on 135° #D93C44→#9E1F25, 18dp safed knob (saya), 20dp khiskta hai.
@Composable
private fun Toggle(on: Boolean, onChange: (Boolean) -> Unit) {
    val knob by animateDpAsState(if (on) 20.dp else 0.dp, tween(200), label = "toggleKnob")
    Box(
        modifier = Modifier.size(44.dp, 24.dp).clip(RoundedCornerShape(24.dp))
            .background(if (on) cssLinear(135f, 0f to Color(0xFFD93C44), 1f to SetRed) else Brush.linearGradient(listOf(Color(0xFFE4DEDE), Color(0xFFE4DEDE))))
            .clickable { onChange(!on) }
    ) {
        Box(
            modifier = Modifier.padding(3.dp).offset(x = knob).size(18.dp)
                .shadow(3.dp, CircleShape).background(Color.White, CircleShape)
        )
    }
}
