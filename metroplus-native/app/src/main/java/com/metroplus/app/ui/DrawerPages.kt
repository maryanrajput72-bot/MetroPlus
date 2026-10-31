package com.metroplus.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.Support
import androidx.compose.material.icons.filled.Work
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metroplus.app.R

// Item 7: drawer ke pages — har ek apna page (GUI phase; save/wiring agle step me).
// v1.61 (uska order "har setting ki GUI advance + cards + pyare gradient"): har page
// ke upar apne rang ka gradient hero (back + bara 3D icon + title), neeche section
// cards jin ke header pe 3D icon tile, gradient toggles/buttons. Values pehle ki
// tarah pageState me (page band karke kholo to wahi).
//   Edit Profile  → laal/gulabi (brand)   Calculator → sunehra/narangi
//   Gps setting   → neela/aqua            Tl/Rp      → jamni/indigo

private val DpInk = Color(0xFF14181C)
private val DpGray = Color(0xFF5B6470)
private val DpBg = Color(0xFFF4EFF0)

private val ProfileGrad = listOf(Color(0xFFFF6B8B), Color(0xFFE5383B), Color(0xFF8A0F3A))
private val CalcGrad = listOf(Color(0xFFFFD166), Color(0xFFFF9F43), Color(0xFFE8590C))
private val GpsGrad = listOf(Color(0xFF5EE7F2), Color(0xFF3A8DFF), Color(0xFF3B3FD8))
private val TlGrad = listOf(Color(0xFFC084FC), Color(0xFF7C3AED), Color(0xFF3730A3))

// ---------------- shared pieces ----------------

// Gradient hero: status bar ke neeche se, neeche gol kone, chamak ke gole, back button,
// daein bara 3D icon. extra = hero ke andar neeche koi aur cheez (jaise profile avatar row).
@Composable
private fun DpHero(
    title: String, sub: String, icon: ImageVector, colors: List<Color>, onBack: () -> Unit,
    extra: (@Composable ColumnScope.() -> Unit)? = null
) {
    val shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
    Box(
        modifier = Modifier.fillMaxWidth().cssShadow(14.dp, shape, colors.last().copy(alpha = 0.35f)).clip(shape)
            .background(cssLinear(135f, 0f to colors[0], 0.55f to colors[1], 1f to colors[2]))
    ) {
        // matchParentSize: sajawat hero ko content se lamba na kare.
        Box(Modifier.matchParentSize()) {
            Glow(220.dp, x = 90.dp, y = (-90).dp, alpha = 0.32f, align = Alignment.TopEnd)
            Glow(140.dp, x = (-50).dp, y = 40.dp, alpha = 0.12f, solid = true, align = Alignment.BottomStart)
            Box(modifier = Modifier.align(Alignment.TopEnd).offset(x = 60.dp, y = 30.dp).size(190.dp).border(1.dp, Color.White.copy(alpha = 0.16f), CircleShape))
        }
        Column(modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 22.dp)) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.22f))
                    .border(1.dp, Color.White.copy(alpha = 0.35f), CircleShape).clickable { onBack() },
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(20.dp)) }
            Spacer(modifier = Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, letterSpacing = (-0.4).sp)
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(sub, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.White.copy(alpha = 0.85f), lineHeight = 17.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Icon3DTile(icon, colors[1], size = 64.dp, iconSize = 32.dp, corner = 20.dp,
                    colors = listOf(Color.White.copy(alpha = 0.38f), Color.White.copy(alpha = 0.12f)))
            }
            if (extra != null) { Spacer(modifier = Modifier.height(16.dp)); extra() }
        }
    }
}

// Section card: header me chhota 3D tile + title (+ optional daein cheez), neeche content.
@Composable
private fun DpSection(
    title: String, icon: ImageVector, accent: Color,
    trailing: (@Composable () -> Unit)? = null, content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(22.dp)
    Column(
        modifier = Modifier.fillMaxWidth().cssShadow(8.dp, shape, accent.copy(alpha = 0.16f))
            .background(Color.White, shape).border(1.dp, accent.copy(alpha = 0.12f), shape).clip(shape)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(accent.copy(alpha = 0.13f), accent.copy(alpha = 0.02f))))
                .padding(horizontal = 14.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon3DTile(icon, accent, size = 32.dp, iconSize = 17.dp, corner = 10.dp)
            Spacer(modifier = Modifier.width(10.dp))
            Text(title, fontSize = 14.5.sp, fontWeight = FontWeight.ExtraBold, color = accent.darker(0.35f), letterSpacing = 0.2.sp, modifier = Modifier.weight(1f))
            trailing?.invoke()
        }
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 16.dp), content = content)
    }
}

@Composable
private fun DpToggle(checked: Boolean, accent: Color = BrandRed, onChange: (Boolean) -> Unit) {
    val knob by animateDpAsState(if (checked) 22.dp else 0.dp, tween(200), label = "dpKnob")
    Box(
        modifier = Modifier.size(50.dp, 28.dp).clip(RoundedCornerShape(999.dp))
            .background(if (checked) Brush.horizontalGradient(listOf(accent.lighter(0.25f), accent.darker(0.1f)))
                else Brush.horizontalGradient(listOf(Color(0xFFE5E7EB), Color(0xFFE5E7EB))))
            .clickable { onChange(!checked) }
    ) {
        Box(modifier = Modifier.padding(3.dp).offset(x = knob).size(22.dp).cssShadow(3.dp, CircleShape, Color.Black.copy(alpha = 0.25f)).background(Color.White, CircleShape))
    }
}

@Composable
private fun DpField(label: String, icon: ImageVector, value: String, onValue: (String) -> Unit, placeholder: String,
                    accent: Color = BrandRed, password: Boolean = false) {
    val shape = RoundedCornerShape(14.dp)
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp, bottom = 6.dp)) {
        Text(label, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = DpGray)
        Text(" *", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = BrandRed)
    }
    BasicTextField(
        value = value, onValueChange = onValue, singleLine = true,
        visualTransformation = if (password) PasswordVisualTransformation() else VisualTransformation.None,
        textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = DpInk),
        modifier = Modifier.fillMaxWidth(),
        decorationBox = { inner ->
            Row(
                modifier = Modifier.fillMaxWidth().clip(shape)
                    .background(Brush.horizontalGradient(listOf(accent.copy(alpha = 0.07f), Color(0xFFF8F6F6))))
                    .border(1.dp, if (value.isNotEmpty()) accent.copy(alpha = 0.45f) else DpInk.copy(alpha = 0.09f), shape)
                    .padding(horizontal = 12.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) Text(placeholder, fontSize = 14.sp, color = DpInk.copy(alpha = 0.35f))
                    inner()
                }
                if (value.isNotEmpty()) Icon(Icons.Filled.Check, contentDescription = null, tint = accent, modifier = Modifier.size(16.dp))
            }
        }
    )
}

// Chuno-ek chips (gradient jab chuna ho).
@Composable
private fun DpChoice(options: List<String>, selected: String, colors: List<Color>, onPick: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        options.forEach { o ->
            val on = o == selected
            val s = RoundedCornerShape(14.dp)
            Box(
                modifier = Modifier.weight(1f).height(46.dp)
                    .then(if (on) Modifier.cssShadow(7.dp, s, colors.last().copy(alpha = 0.35f)) else Modifier)
                    .clip(s)
                    .then(if (on) Modifier.background(cssLinear(135f, 0f to colors[0], 1f to colors[2]))
                        else Modifier.background(Color(0xFFF6F2F2)).border(1.5.dp, Color(0xFFE7DCDD), s))
                    .clickable { onPick(o) },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (on) { Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp)); Spacer(modifier = Modifier.width(5.dp)) }
                    Text(o, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = if (on) Color.White else Color(0xFF3A2A2C))
                }
            }
        }
    }
}

@Composable
private fun DpBigButton(text: String, icon: ImageVector, colors: List<Color>, onClick: () -> Unit) {
    val s = RoundedCornerShape(16.dp)
    Box(
        modifier = Modifier.fillMaxWidth().height(52.dp).cssShadow(10.dp, s, colors.last().copy(alpha = 0.4f)).clip(s)
            .background(cssLinear(120f, 0f to colors[0], 0.5f to colors[1], 1f to colors[2])).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.22f), Color.Transparent))))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(19.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        }
    }
}

@Composable
private fun DpSetRow(icon: ImageVector, accent: Color, label: String, desc: String, trailing: @Composable () -> Unit, onClick: (() -> Unit)? = null) {
    Row(
        modifier = Modifier.fillMaxWidth().then(if (onClick != null) Modifier.clickable { onClick() } else Modifier).padding(vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon3DTile(icon, accent)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DpInk)
            Text(desc, fontSize = 12.5.sp, color = DpGray)
        }
        Spacer(modifier = Modifier.width(8.dp))
        trailing()
    }
}

@Composable
private fun DpDivider() = Box(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp).height(1.dp).background(DpInk.copy(alpha = 0.06f)))

@Composable
private fun DpPage(content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(DpBg).verticalScroll(rememberScrollState()), content = content)
}

@Composable
private fun DpBody(content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp), content = content)
}

// Chhota stat dabba (hero ke neeche ya summary card me).
@Composable
private fun RowScope.DpStat(value: String, label: String, accent: Color) {
    Column(
        modifier = Modifier.weight(1f).clip(RoundedCornerShape(16.dp))
            .background(Brush.verticalGradient(listOf(accent.copy(alpha = 0.14f), accent.copy(alpha = 0.05f))))
            .border(1.dp, accent.copy(alpha = 0.18f), RoundedCornerShape(16.dp)).padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = accent.darker(0.25f))
        Text(label, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = DpGray)
    }
}

// ---------------- Edit Profile ----------------

@Composable
fun EditProfileScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit) {
    BackHandler { onBack() }
    // Values ViewModel me (page band karke kholo to wahi rahein).
    var tlName by pageState("profile_tlName", "")
    var name by pageState("profile_name", "Aryan")
    var phone by pageState("profile_phone", "")
    var empId by pageState("profile_empId", "")
    var station by pageState("profile_station", "Shaheed-e-Millat")
    var stationOpen by remember { mutableStateOf(false) }
    var shift by pageState("profile_shift", "Morning")
    var role by pageState("profile_role", "Operator")
    val filled = listOf(tlName, name, phone, empId, station).count { it.isNotBlank() }
    val progress by animateFloatAsState(filled / 5f, tween(400), label = "profileProgress")
    val accent = ProfileGrad[1]

    DpPage {
        DpHero("Edit Profile", "Your details on every report", Icons.Filled.Person, ProfileGrad, onBack) {
            // Avatar + naam + role chip, hero ke andar.
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(62.dp).cssShadow(8.dp, CircleShape, Color.Black.copy(alpha = 0.3f))
                        .background(Color.White, CircleShape).padding(3.dp).clip(CircleShape).background(Color.Black),
                    contentAlignment = Alignment.Center
                ) { Image(painterResource(id = R.drawable.logo_hd), contentDescription = null, contentScale = ContentScale.Crop, colorFilter = LogoEnhance, modifier = Modifier.fillMaxSize()) }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(name.ifBlank { "Your name" }, fontSize = 19.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        HeroChip(role.uppercase()); HeroChip("$shift shift")
                    }
                }
            }
        }
        DpBody {
            // Profile kitni mukammal hai (5 fields).
            val pShape = RoundedCornerShape(20.dp)
            Column(
                modifier = Modifier.fillMaxWidth().cssShadow(6.dp, pShape, accent.copy(alpha = 0.14f)).background(Color.White, pShape).padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Profile complete", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = DpInk, modifier = Modifier.weight(1f))
                    Text("${filled * 20}%", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = accent)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.fillMaxWidth().height(9.dp).clip(RoundedCornerShape(999.dp)).background(accent.copy(alpha = 0.12f))) {
                    Box(modifier = Modifier.fillMaxWidth(progress).height(9.dp).clip(RoundedCornerShape(999.dp))
                        .background(Brush.horizontalGradient(listOf(ProfileGrad[0], ProfileGrad[2]))))
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(if (filled == 5) "All details filled" else "${5 - filled} detail(s) missing", fontSize = 12.sp, color = DpGray)
            }
            DpSection("PERSONAL INFO", Icons.Filled.Person, Color(0xFFE5383B)) {
                DpField("TL Name", Icons.Filled.SupervisorAccount, tlName, { tlName = it }, "TL name", accent)
                Spacer(modifier = Modifier.height(10.dp))
                DpField("Your name", Icons.Filled.Person, name, { name = it }, "Your name", accent)
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.padding(top = 2.dp, bottom = 6.dp)) {
                    Text("Station", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = DpGray)
                    Text(" *", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = BrandRed)
                }
                val sShape = RoundedCornerShape(14.dp)
                Row(
                    modifier = Modifier.fillMaxWidth().clip(sShape)
                        .background(Brush.horizontalGradient(listOf(accent.copy(alpha = 0.07f), Color(0xFFF8F6F6))))
                        .border(1.dp, accent.copy(alpha = 0.45f), sShape)
                        .clickable { stationOpen = true }.padding(horizontal = 12.dp, vertical = 11.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = accent, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(station, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = DpInk, modifier = Modifier.weight(1f))
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = Color(0xFF9AA1AB), modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.height(10.dp))
                DpField("Mobile Number", Icons.Filled.Phone, phone, { phone = it }, "Mobile number", accent)
                Spacer(modifier = Modifier.height(10.dp))
                DpField("Employee ID", Icons.Filled.Badge, empId, { empId = it }, "Employee ID", accent)
            }
            DpSection("WORK INFO", Icons.Filled.Work, Color(0xFF3A7BEF)) {
                Row(modifier = Modifier.padding(bottom = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Schedule, contentDescription = null, tint = Color(0xFF3A7BEF), modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Shift", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = DpGray)
                }
                DpChoice(listOf("Morning", "Evening"), shift, listOf(Color(0xFFFFD166), Color(0xFFFF9F43), Color(0xFFE8590C))) { shift = it }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.padding(bottom = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Work, contentDescription = null, tint = Color(0xFF3A7BEF), modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Role", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = DpGray)
                }
                DpChoice(listOf("TL", "RP", "Operator"), role, GpsGrad) { role = it }
            }
            DpBigButton("Save Profile", Icons.Filled.Check, ProfileGrad) { onGuiNote("Save Profile") }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
    if (stationOpen) StationPickerSheet(station, { station = it }, { stationOpen = false })
}

@Composable
private fun HeroChip(text: String) {
    Text(
        text, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, letterSpacing = 0.6.sp,
        modifier = Modifier.background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(999.dp))
            .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(999.dp)).padding(horizontal = 9.dp, vertical = 3.dp)
    )
}

// ---------------- Calculator setting ----------------

@Composable
fun CalculatorSettingsScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    // Trial Manage Denominations modal ke 11 toggles (5000 se 1 tak, 75 samet).
    val denoms = listOf(
        5000 to "Note", 1000 to "Note", 500 to "Note", 100 to "Note", 75 to "Note", 50 to "Note",
        20 to "Note", 10 to "Note", 5 to "Coin", 2 to "Coin", 1 to "Coin"
    )
    val badgeColors = mapOf(
        5000 to Color(0xFFF5B82E), 1000 to Color(0xFF5B8DEF), 500 to Color(0xFF14B8A6),
        100 to Color(0xFFFF6B6B), 75 to Color(0xFFEC4899), 50 to Color(0xFFA78BFA), 20 to Color(0xFFFB923C),
        10 to Color(0xFFC08A4F), 5 to Color(0xFF94A3B8), 2 to Color(0xFF9CA3AF), 1 to Color(0xFFB0B7C3)
    )
    val enabled = pageMap("calc_enabled") { denoms.associate { (v, _) -> v to true } }
    val on = denoms.count { enabled[it.first] == true }
    val notesOn = denoms.count { it.second == "Note" && enabled[it.first] == true }
    val coinsOn = denoms.count { it.second == "Coin" && enabled[it.first] == true }

    DpPage {
        DpHero("Calculator setting", "Choose which notes & coins to count", Icons.Filled.Calculate, CalcGrad, onBack)
        DpBody {
            DpSection("SUMMARY", Icons.Filled.Payments, Color(0xFFFF9F43)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    DpStat("$on/11", "Enabled", Color(0xFFFF9F43))
                    DpStat("$notesOn", "Notes", Color(0xFF3A7BEF))
                    DpStat("$coinsOn", "Coins", Color(0xFF64748B))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    QuickChip("Enable all", Modifier.weight(1f)) { denoms.forEach { enabled[it.first] = true } }
                    QuickChip("Notes only", Modifier.weight(1f)) { denoms.forEach { enabled[it.first] = it.second == "Note" } }
                }
            }
            Text("DENOMINATIONS", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = DpGray, letterSpacing = 1.sp, modifier = Modifier.padding(start = 4.dp))
            // 2 column grid of denomination cards.
            denoms.chunked(2).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    row.forEach { (v, kind) ->
                        DenomCard(v, kind, badgeColors[v] ?: Color(0xFF9AA1AB), enabled[v] == true, Modifier.weight(1f)) { enabled[v] = it }
                    }
                    if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun QuickChip(text: String, modifier: Modifier, onClick: () -> Unit) {
    val s = RoundedCornerShape(12.dp)
    Box(
        modifier = modifier.height(40.dp).clip(s).background(Brush.horizontalGradient(listOf(Color(0xFFFFF4E0), Color(0xFFFFE7CC))))
            .border(1.dp, Color(0xFFFF9F43).copy(alpha = 0.35f), s).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) { Text(text, fontSize = 13.5.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFC2410C)) }
}

@Composable
private fun DenomCard(value: Int, kind: String, color: Color, on: Boolean, modifier: Modifier, onChange: (Boolean) -> Unit) {
    val s = RoundedCornerShape(20.dp)
    val alpha by animateFloatAsState(if (on) 1f else 0.45f, tween(200), label = "denomAlpha")
    Column(
        modifier = modifier.cssShadow(if (on) 8.dp else 2.dp, s, color.copy(alpha = 0.3f)).clip(s)
            .background(if (on) cssLinear(135f, 0f to color.lighter(0.82f), 1f to color.lighter(0.55f)) else Brush.linearGradient(listOf(Color(0xFFF3F4F6), Color(0xFFF3F4F6))))
            .border(1.dp, if (on) color.copy(alpha = 0.35f) else DpInk.copy(alpha = 0.06f), s)
            .clickable { onChange(!on) }.padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // v1.65: chhota rangeen dabba ki jagah Money Calculator wala asli note badge
            // (gradient + andar dashed lakeer + value); sikke gol.
            val (ns, ne) = NoteColors[value] ?: (color.lighter(0.2f) to color.darker(0.15f))
            val coinText = if (kind == "Coin") Color(0xFF3F3F46) else Color.White
            Box(modifier = Modifier.graphicsLayer { this.alpha = 0.55f + 0.45f * alpha }) {
                NoteBadge(value, ns, ne, coinText, coin = kind == "Coin", minWidth = 58.dp, height = 34.dp)
            }
            Spacer(modifier = Modifier.weight(1f))
            DpToggle(on, color.darker(0.1f)) { onChange(it) }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text("Rs. ${"%,d".format(value)}", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = DpInk.copy(alpha = alpha))
        Text(if (on) "$kind · counted" else "$kind · hidden", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = DpGray.copy(alpha = alpha))
    }
}

// ---------------- Gps setting ----------------

@Composable
fun GpsSettingsScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit) {
    BackHandler { onBack() }
    var showStation by pageState("gps_showStation", true)
    var showDate by pageState("gps_showDate", true)
    var showCoords by pageState("gps_showCoords", true)
    var accuracy by pageState("gps_accuracy", "High")
    var mapType by pageState("gps_mapType", "Satellite")
    val blue = GpsGrad[1]

    DpPage {
        DpHero("Gps setting", "Location stamp & map style for photos", Icons.Filled.GpsFixed, GpsGrad, onBack)
        DpBody {
            // GPS ki halat (GUI demo — asal GPS baad me).
            DpSection("GPS STATUS", Icons.Filled.MyLocation, blue, trailing = {
                Row(
                    modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(Color(0xFF22C55E).copy(alpha = 0.12f)).padding(horizontal = 9.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(7.dp).background(Color(0xFF22C55E), CircleShape))
                    Spacer(modifier = Modifier.width(5.dp))
                    Text("READY", fontSize = 10.5.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF15803D))
                }
            }) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    DpStat(accuracy, "Accuracy", blue)
                    DpStat(mapType, "Map", Color(0xFF3B3FD8))
                    DpStat(if (showStation) "On" else "Off", "Station", Color(0xFF0891B2))
                }
            }
            DpSection("PHOTO STAMP", Icons.Filled.PhotoCamera, Color(0xFF0891B2)) {
                DpSetRow(Icons.Filled.LocationOn, Color(0xFFF59E0B), "Station name", "Nearest station on GPS photos", { DpToggle(showStation, blue) { showStation = it } })
                DpDivider()
                DpSetRow(Icons.Filled.Schedule, Color(0xFFEC4899), "Date & time", "Day, date and time on photo", { DpToggle(showDate, blue) { showDate = it } })
                DpDivider()
                DpSetRow(Icons.Filled.GpsFixed, Color(0xFF22C55E), "Coordinates", "Latitude / longitude on photo", { DpToggle(showCoords, blue) { showCoords = it } })
            }
            DpSection("GPS ACCURACY", Icons.Filled.GpsFixed, blue) {
                DpChoice(listOf("Balanced", "High", "Max"), accuracy, GpsGrad) { accuracy = it }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    when (accuracy) { "Balanced" -> "Saves battery, a little less exact"; "Max" -> "Most exact, uses more battery"; else -> "Real-time satellite tracking" },
                    fontSize = 12.5.sp, color = DpGray
                )
            }
            DpSection("MAP STYLE", Icons.Filled.Layers, Color(0xFF3B3FD8)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MapCard("Satellite", mapType == "Satellite", listOf(Color(0xFF14532D), Color(0xFF166534), Color(0xFF365314)), Modifier.weight(1f)) { mapType = "Satellite" }
                    MapCard("Streets", mapType == "Streets", listOf(Color(0xFFF1F5F9), Color(0xFFE2E8F0), Color(0xFFFDE68A)), Modifier.weight(1f)) { mapType = "Streets" }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

// Map ka chhota namoona (rangeen dabba + sarkon ki lakeerein) — chuna hua neeli kinari.
@Composable
private fun MapCard(name: String, selected: Boolean, colors: List<Color>, modifier: Modifier, onClick: () -> Unit) {
    val s = RoundedCornerShape(18.dp)
    Column(
        modifier = modifier.clip(s).border(if (selected) 2.5.dp else 1.dp, if (selected) GpsGrad[1] else DpInk.copy(alpha = 0.1f), s)
            .clickable { onClick() }.padding(6.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(84.dp).clip(RoundedCornerShape(13.dp)).background(Brush.linearGradient(colors.take(2)))) {
            val road = if (name == "Streets") Color.White else Color(0xFF4D7C0F).copy(alpha = 0.7f)
            Box(modifier = Modifier.fillMaxWidth().padding(top = 34.dp).height(8.dp).background(if (name == "Streets") colors[2] else road))
            Box(modifier = Modifier.padding(start = 48.dp).width(8.dp).fillMaxSize().background(road))
            Box(modifier = Modifier.align(Alignment.Center).offset(x = 18.dp, y = (-10).dp).size(16.dp).background(BrandRed, CircleShape).border(3.dp, Color.White, CircleShape))
        }
        Row(modifier = Modifier.padding(start = 6.dp, top = 8.dp, bottom = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(name, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = DpInk, modifier = Modifier.weight(1f))
            if (selected) Icon(Icons.Filled.Check, contentDescription = null, tint = GpsGrad[1], modifier = Modifier.size(17.dp))
        }
    }
}

// ---------------- Tl/Rp access setting ----------------

@Composable
fun TlRpSettingsScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit) {
    BackHandler { onBack() }
    var gmail by pageState("tlrp_gmail", "")
    var password by pageState("tlrp_password", "")
    var showPremium by remember { mutableStateOf(false) }
    val purple = TlGrad[1]
    Box(modifier = Modifier.fillMaxSize()) {
    DpPage {
        DpHero("Tl/Rp access setting", "Premium access for team leads", Icons.Filled.AdminPanelSettings, TlGrad, onBack)
        DpBody {
            // Sab se upar: jinke paas Gmail/password nahi, unke liye admin note +
            // pyara WhatsApp button (sirf "WhatsApp"; number uske batane pe — abhi GUI note).
            DpSection("Contact to admin for access", Icons.Filled.Support, BrandRed) {
                Text("If you do not have a TL Gmail or password, contact the admin — only the admin can give access.", fontSize = 12.5.sp, color = DpGray, lineHeight = 18.sp)
                Spacer(modifier = Modifier.height(12.dp))
                val s = RoundedCornerShape(16.dp)
                Box(
                    modifier = Modifier.fillMaxWidth().height(50.dp).cssShadow(8.dp, s, Color(0xFF25D366).copy(alpha = 0.35f)).clip(s)
                        .background(cssLinear(135f, 0f to Color(0xFF4ADE80), 0.5f to Color(0xFF25D366), 1f to Color(0xFF128C7E)))
                        .clickable { onGuiNote("WhatsApp") },
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.22f), Color.Transparent))))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(painterResource(id = R.drawable.img_icon_whatsapp_clean), contentDescription = null, contentScale = ContentScale.Fit, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("WhatsApp", fontSize = 15.5.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                }
            }
            // TL login (Gmail + password) — Password Change option khatam (uska order).
            DpSection("TL LOGIN", Icons.Filled.Lock, purple) {
                DpField("Gmail", Icons.Filled.Email, gmail, { gmail = it }, "TL Gmail", purple)
                Spacer(modifier = Modifier.height(10.dp))
                DpField("Password", Icons.Filled.Lock, password, { password = it }, "TL password", purple, password = true)
                Spacer(modifier = Modifier.height(14.dp))
                DpBigButton("Login", Icons.Filled.Check, TlGrad) { onGuiNote("TL Login") }
            }
            DpSection("ROLE ACCESS", Icons.Filled.SupervisorAccount, Color(0xFF3A7BEF)) {
                RoleRow(Icons.Filled.Person, Color(0xFF3A7BEF), "Operator", "Only GPS Camera + Calculator", listOf("GPS", "Calc"))
                DpDivider()
                RoleRow(Icons.Filled.SupervisorAccount, purple, "TL / RP", "Full app access", listOf("TOM", "Closing", "Complaint", "Settings"))
            }
            // v1.62: Premium request modal ka button (63 ke premium modal ki GUI).
            DpBigButton("Request Premium Access", Icons.Filled.AdminPanelSettings, TlGrad) { showPremium = true }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
    if (showPremium) PremiumRequestModal(onClose = { showPremium = false }, onGuiNote = onGuiNote)
    }
}

@Composable
private fun RoleRow(icon: ImageVector, accent: Color, title: String, sub: String, tags: List<String>) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 9.dp), verticalAlignment = Alignment.Top) {
        Icon3DTile(icon, accent)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DpInk)
            Text(sub, fontSize = 12.5.sp, color = DpGray)
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                tags.forEach { t ->
                    Text(t, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = accent.darker(0.2f),
                        modifier = Modifier.background(accent.copy(alpha = 0.1f), RoundedCornerShape(999.dp)).padding(horizontal = 8.dp, vertical = 2.dp))
                }
            }
        }
    }
}
