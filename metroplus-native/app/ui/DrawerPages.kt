package com.metroplus.app.ui

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.metroplus.app.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Item 7: drawer ke pages — har ek apna page (GUI phase; save/wiring agle step me).
// Edit Profile (trial profileEditScreen), Calculator setting (trial Manage
// Denominations modal), Gps setting (trial gpsSettingsScreen), Tl/Rp access
// setting (app ki apni TL spec: TL PIN + role access).

private val DpInk = Color(0xFF14181C)
private val DpGray = Color(0xFF5B6470)
private val DpRed = Color(0xFF9E1F25)

@Composable
private fun DpHeader(title: String, onBack: () -> Unit, onSave: (() -> Unit)? = null) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.White.copy(alpha = 0.94f)).statusBarsPadding().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(38.dp).clip(CircleShape).background(Color.White).border(1.dp, DpInk.copy(alpha = 0.09f), CircleShape).clickable { onBack() },
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = DpInk, modifier = Modifier.size(18.dp)) }
        Spacer(modifier = Modifier.width(12.dp))
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = DpInk, letterSpacing = (-0.3).sp, modifier = Modifier.weight(1f))
        if (onSave != null) {
            Box(
                modifier = Modifier.size(38.dp).clip(CircleShape).background(Color.White).border(1.dp, DpInk.copy(alpha = 0.09f), CircleShape).clickable { onSave() },
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Filled.Check, contentDescription = "Save", tint = Color(0xFFF59E0B), modifier = Modifier.size(18.dp)) }
        } else Spacer(modifier = Modifier.width(38.dp))
    }
}

@Composable
private fun DpToggle(checked: Boolean, onChange: (Boolean) -> Unit) {
    val w = 52.dp; val h = 30.dp
    Box(
        modifier = Modifier.size(w, h).clip(RoundedCornerShape(999.dp))
            .background(if (checked) DpRed else Color(0xFFE5E7EB)).clickable { onChange(!checked) },
        contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(modifier = Modifier.size(24.dp).padding(3.dp).background(Color.White, CircleShape))
    }
}

@Composable
private fun DpCard(title: String?, content: @Composable ColumnScope.() -> Unit) {
    val shape = RoundedCornerShape(18.dp)
    Column(
        modifier = Modifier.fillMaxWidth().cssShadow(5.dp, shape, DpInk.copy(alpha = 0.08f))
            .background(Color.White, shape).border(1.dp, DpInk.copy(alpha = 0.06f), shape).padding(16.dp)
    ) {
        if (title != null) {
            Text(title, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = DpInk.copy(alpha = 0.55f), letterSpacing = 1.2.sp)
            Spacer(modifier = Modifier.height(10.dp))
        }
        content()
    }
}

@Composable
private fun DpField(label: String, icon: ImageVector, value: String, onValue: (String) -> Unit, placeholder: String, password: Boolean = false) {
    val shape = RoundedCornerShape(12.dp)
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp, bottom = 6.dp)) {
        Icon(icon, contentDescription = null, tint = Color(0xFFC6434A), modifier = Modifier.size(15.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = DpGray)
        Text(" *", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE11D2E))
    }
    BasicTextField(
        value = value, onValueChange = onValue, singleLine = true,
        visualTransformation = if (password) PasswordVisualTransformation() else VisualTransformation.None,
        textStyle = TextStyle(fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold, color = DpInk),
        modifier = Modifier.fillMaxWidth(),
        decorationBox = { inner ->
            Box(
                modifier = Modifier.fillMaxWidth().clip(shape).background(Color(0xFFF6F2F2)).border(1.dp, DpInk.copy(alpha = 0.1f), shape)
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) Text(placeholder, fontSize = 13.5.sp, color = DpInk.copy(alpha = 0.35f))
                inner()
            }
        }
    )
}

@Composable
private fun DpToggleRow(options: List<String>, selected: String, onPick: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        options.forEach { o ->
            val on = o == selected
            Box(
                modifier = Modifier.weight(1f).height(46.dp).clip(RoundedCornerShape(14.dp))
                    .then(if (on) Modifier.cssShadow(6.dp, RoundedCornerShape(14.dp), Color(0xFF7A0C12).copy(alpha = 0.3f))
                        .background(cssLinear(135f, 0f to Color(0xFFE11D2A), 0.55f to Color(0xFF7A0C12), 1f to Color(0xFF1A1A1D)))
                    else Modifier.background(Color(0xFFF6F2F2)).border(1.5.dp, Color(0xFFE7DCDD), RoundedCornerShape(14.dp)))
                    .clickable { onPick(o) },
                contentAlignment = Alignment.Center
            ) { Text(o, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = if (on) Color.White else Color(0xFF3A2A2C)) }
        }
    }
}

@Composable
private fun DpSetRow(icon: ImageVector, iconTint: Color, label: String, desc: String, trailing: @Composable () -> Unit, onClick: (() -> Unit)? = null) {
    Row(
        modifier = Modifier.fillMaxWidth().then(if (onClick != null) Modifier.clickable { onClick() } else Modifier).padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DpInk)
            Text(desc, fontSize = 12.5.sp, color = DpGray)
        }
        Spacer(modifier = Modifier.width(8.dp))
        trailing()
    }
}

// ---------------- Edit Profile ----------------

@Composable
fun EditProfileScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit) {
    BackHandler { onBack() }
    var tlName by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("Aryan") }
    var phone by remember { mutableStateOf("") }
    var empId by remember { mutableStateOf("") }
    var station by remember { mutableStateOf("Shaheed-e-Millat") }
    var stationOpen by remember { mutableStateOf(false) }
    var shift by remember { mutableStateOf("Morning") }
    var role by remember { mutableStateOf("Operator") }
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF3EDED))) {
        DpHeader("Edit Profile", onBack) { onGuiNote("Save Profile") }
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            DpCard("PERSONAL INFO") {
                DpField("TL Name", Icons.Filled.SupervisorAccount, tlName, { tlName = it }, "TL name")
                Spacer(modifier = Modifier.height(10.dp))
                DpField("Your name", Icons.Filled.Person, name, { name = it }, "Your name")
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp, bottom = 6.dp)) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = Color(0xFFC6434A), modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Station", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = DpGray)
                    Text(" *", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE11D2E))
                }
                Row(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFFF6F2F2)).border(1.dp, DpInk.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                        .clickable { stationOpen = true }.padding(horizontal = 12.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(station, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold, color = DpInk, modifier = Modifier.weight(1f))
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = Color(0xFF9AA1AB), modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.height(10.dp))
                DpField("Mobile Number", Icons.Filled.Phone, phone, { phone = it }, "Mobile number")
                Spacer(modifier = Modifier.height(10.dp))
                DpField("Employee ID", Icons.Filled.Badge, empId, { empId = it }, "Employee ID")
            }
            DpCard("WORK INFO") {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 6.dp)) {
                    Icon(Icons.Filled.Schedule, contentDescription = null, tint = Color(0xFFC6434A), modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Shift", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = DpGray)
                    Text(" *", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE11D2E))
                }
                DpToggleRow(listOf("Morning", "Evening"), shift) { shift = it }
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 6.dp)) {
                    Icon(Icons.Filled.Work, contentDescription = null, tint = Color(0xFFC6434A), modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Role", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = DpGray)
                    Text(" *", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE11D2E))
                }
                DpToggleRow(listOf("TL", "RP", "Operator"), role) { role = it }
            }
            Box(
                modifier = Modifier.fillMaxWidth().height(48.dp).cssShadow(8.dp, RoundedCornerShape(14.dp), Color(0xFF7A0C12).copy(alpha = 0.3f))
                    .clip(RoundedCornerShape(14.dp)).background(cssLinear(135f, 0f to Color(0xFFE11D2A), 0.55f to Color(0xFF7A0C12), 1f to Color(0xFF1A1A1D)))
                    .clickable { onGuiNote("Save Profile") },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save Profile", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (stationOpen) StationPickerSheet(station, { station = it }, { stationOpen = false })
    }
}

// ---------------- Calculator setting ----------------

@Composable
fun CalculatorSettingsScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    // Trial Manage Denominations modal ke 11 toggles (5000 se 1 tak, 75 samet).
    val denoms = listOf(
        5000 to "Rs. 5,000 Note", 1000 to "Rs. 1,000 Note", 500 to "Rs. 500 Note",
        100 to "Rs. 100 Note", 75 to "Rs. 75", 50 to "Rs. 50 Note",
        20 to "Rs. 20 Note", 10 to "Rs. 10 Note",
        5 to "Rs. 5 Coin", 2 to "Rs. 2 Coin", 1 to "Rs. 1 Coin"
    )
    val badgeColors = mapOf(
        5000 to Color(0xFFF5C542), 1000 to Color(0xFF5B8DEF), 500 to Color(0xFF2DD4BF),
        100 to Color(0xFFFF6B6B), 50 to Color(0xFFA78BFA), 20 to Color(0xFFFB923C), 10 to Color(0xFFD6A46B)
    )
    val enabled = remember { mutableStateMapOf<Int, Boolean>().apply { denoms.forEach { (v, _) -> put(v, true) } } }
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF3EDED))) {
        DpHeader("Calculator setting", onBack)
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("Enable or disable specific currency notes & coins in your calculator:", fontSize = 12.5.sp, color = DpGray, modifier = Modifier.padding(horizontal = 4.dp))
            DpCard(null) {
                denoms.forEachIndexed { i, (v, label) ->
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("$v", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color.White,
                            modifier = Modifier.background(badgeColors[v] ?: Color(0xFF9AA1AB), RoundedCornerShape(8.dp)).padding(horizontal = 10.dp, vertical = 5.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(label, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DpInk, modifier = Modifier.weight(1f))
                        DpToggle(enabled[v] == true) { enabled[v] = it }
                    }
                    if (i < denoms.size - 1) Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(DpInk.copy(alpha = 0.06f)))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// ---------------- Gps setting ----------------

@Composable
fun GpsSettingsScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit) {
    BackHandler { onBack() }
    var showStation by remember { mutableStateOf(true) }
    var mapType by remember { mutableStateOf("Satellite") }
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF3EDED))) {
        DpHeader("Gps setting", onBack)
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            DpCard(null) {
                DpSetRow(Icons.Filled.LocationOn, Color(0xFFFFC107), "Station Name on Photo", "Nearest station name on GPS photos", { DpToggle(showStation) { showStation = it } })
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(DpInk.copy(alpha = 0.06f)))
                DpSetRow(Icons.Filled.GpsFixed, Color(0xFF00C6FF), "GPS Accuracy", "Real-time satellite tracking",
                    { Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("High Precision", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DpGray)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = DpGray, modifier = Modifier.size(16.dp))
                    } }, onClick = { onGuiNote("GPS Accuracy") })
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(DpInk.copy(alpha = 0.06f)))
                DpSetRow(Icons.Filled.Map, Color(0xFFB91018), "Map Settings", "Satellite map style",
                    { Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(mapType, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DpGray)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = DpGray, modifier = Modifier.size(16.dp))
                    } }, onClick = { mapType = if (mapType == "Satellite") "Streets" else "Satellite" })
                // Money Calculator Setting row khatam (uska order) — denomination
                // setting sirf drawer ki "Calculator setting" se khulti hai.
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// ---------------- Tl/Rp access setting ----------------

@Composable
fun TlRpSettingsScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit) {
    BackHandler { onBack() }
    var gmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF3EDED))) {
        DpHeader("Tl/Rp access setting", onBack)
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            // Sab se upar: jinke paas Gmail/password nahi, unke liye admin note.
            // Batch item 5: note ke neeche pyara sa WhatsApp button (sirf
            // "WhatsApp" likha hai). Number uske batane pe lagega — abhi GUI note.
            DpCard(null) {
                Text("Contact to admin for access", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = DpRed)
                Spacer(modifier = Modifier.height(4.dp))
                Text("If you do not have a TL Gmail or password, contact the admin — only the admin can give access.", fontSize = 12.5.sp, color = DpGray, lineHeight = 18.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier.fillMaxWidth().height(46.dp).cssShadow(5.dp, RoundedCornerShape(14.dp), Color(0xFF25D366).copy(alpha = 0.3f))
                        .clip(RoundedCornerShape(14.dp)).background(cssLinear(135f, 0f to Color(0xFF25D366), 1f to Color(0xFF128C7E)))
                        .clickable { onGuiNote("WhatsApp") },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painterResource(id = R.drawable.img_icon_whatsapp), contentDescription = null,
                            contentScale = ContentScale.Fit, modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("WhatsApp", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                }
            }
            // TL login (Gmail + password) — Password Change option khatam (uska
            // order): password sirf admin de/bad sakta hai, app se nahi.
            DpCard("TL LOGIN") {
                DpField("Gmail", Icons.Filled.Email, gmail, { gmail = it }, "TL Gmail")
                Spacer(modifier = Modifier.height(10.dp))
                DpField("Password", Icons.Filled.Lock, password, { password = it }, "TL password", password = true)
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier.fillMaxWidth().height(48.dp).cssShadow(6.dp, RoundedCornerShape(14.dp), DpRed.copy(alpha = 0.25f))
                        .clip(RoundedCornerShape(14.dp)).background(DpRed).clickable { onGuiNote("TL Login") },
                    contentAlignment = Alignment.Center
                ) { Text("Login", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White) }
            }
            DpCard("ROLE ACCESS") {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 6.dp)) {
                    Icon(Icons.Filled.Person, contentDescription = null, tint = Color(0xFF1A6FD8), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Operator", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DpInk)
                        Text("Only GPS Camera + Calculator", fontSize = 12.5.sp, color = DpGray)
                    }
                }
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(DpInk.copy(alpha = 0.06f)))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 6.dp)) {
                    Icon(Icons.Filled.SupervisorAccount, contentDescription = null, tint = DpRed, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("TL / RP", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DpInk)
                        Text("Full app (TOM, Closing, Complaint, Settings)", fontSize = 12.5.sp, color = DpGray)
                    }
                }
            }
            // Batch item 5: neeche wala "TL ACCESS" text card POORA khatam (faltu letters).
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
