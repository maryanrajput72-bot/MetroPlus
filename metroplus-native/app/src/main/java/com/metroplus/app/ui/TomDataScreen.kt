package com.metroplus.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Today
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.metroplus.app.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Tom Data Register — trial 2.1.63 ke tomDataScreen ki EXACT copy (computed CSS, 1px = 1dp):
// station hero (Opening = neela-purple, Closing = peela-orange), Opening/Closing switch,
// rangeen tom-cards (kona 24, 120° gradients), 36dp inputs, Grand Summary, action buttons.
// SIRF GUI: hisaab (Used / Sale / Slips) aur Save/Share agle step me.

private val TdInk = Color(0xFF14181C)
private val TdMuted = Color(0xFF5C6470)

// Har field ki value ek map me (key = card + field), ViewModel me taake page band karke kholo to wahi rahe.
class TomDataViewModel : ViewModel() {
    val closingMode = mutableStateOf(false)
    val values = mutableStateMapOf<String, String>()
}

@Composable
fun TomDataScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit) {
    BackHandler { onBack() }
    val vm: TomDataViewModel = viewModel()
    var closing by vm.closingMode
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF3EDED))) {
        // Header: back | laal invoice icon + "Tom Data Register" | history.
        Row(
            modifier = Modifier.fillMaxWidth().background(Color.White.copy(alpha = 0.94f)).statusBarsPadding().padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TdCircle(Icons.Filled.ArrowBack, "Back", onBack)
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.ReceiptLong, contentDescription = null, tint = Color(0xFFB02A31), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tom Data Register", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TdInk)
            }
            TdCircle(Icons.Filled.History, "History") { onGuiNote("Register History") }
        }
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            StationHero(closing, onGuiNote)
            Spacer(modifier = Modifier.height(12.dp))
            ModeSwitch(closing) { closing = it }
            Spacer(modifier = Modifier.height(16.dp))
            if (closing) ClosingMode(vm, onGuiNote) else OpeningMode(vm, onGuiNote)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun TdCircle(icon: ImageVector, desc: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(44.dp).clip(CircleShape).background(Color.White).border(1.dp, TdInk.copy(alpha = 0.09f), CircleShape).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) { Icon(icon, contentDescription = desc, tint = TdInk, modifier = Modifier.size(19.dp)) }
}

// Station hero: 125° gradient (Opening neela, Closing laal), kona 24, watermark,
// bus avatar + "Daily Shift Register", save/WhatsApp chhote gol buttons, shift + date chips, revenue.
@Composable
private fun StationHero(closing: Boolean, onGuiNote: (String) -> Unit) {
    val shape = RoundedCornerShape(24.dp)
    val stops = if (closing) arrayOf(0f to Color(0xFFF2BDC0), 0.55f to Color(0xFFE69A9E), 1f to Color(0xFFE69A9E))
    else arrayOf(0f to Color(0xFFDCECFF), 0.55f to Color(0xFFB9D4FB), 1f to Color(0xFFB9D4FB))
    val shadow = if (closing) Color(0xFF9E1F25).copy(alpha = 0.16f) else Color(0xFF2563EB).copy(alpha = 0.14f)
    val border = if (closing) Color(0xFF9E1F25).copy(alpha = 0.3f) else Color(0xFF2563EB).copy(alpha = 0.22f)
    val shift = if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 12) "Morning Shift" else "Evening Shift"
    Box(
        modifier = Modifier.fillMaxWidth().cssShadow(8.dp, shape, shadow).clip(shape).background(cssLinear(125f, *stops)).border(1.dp, border, shape)
    ) {
        Watermark(219.dp, x = 200.dp, y = (-8).dp, alpha = 0.2f)
        Column(modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 20.dp, bottom = 18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(46.dp).cssShadow(4.dp, RoundedCornerShape(14.dp), Color.Black.copy(alpha = 0.08f))
                        .background(Color.White.copy(alpha = 0.75f), RoundedCornerShape(14.dp)).border(1.dp, Color.White.copy(alpha = 0.95f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) { Image(remember { heroBusIcon() }, contentDescription = "Metro bus", modifier = Modifier.size(40.dp)) }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Daily Shift Register", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = TdMuted, letterSpacing = 0.3.sp)
                    Text("Shaheed-e-Millat", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = TdInk, letterSpacing = (-0.2).sp, maxLines = 1)
                }
                HeroMini(R.drawable.img_icon_save, Color.Black.copy(alpha = 0.4f), Color.White.copy(alpha = 0.18f)) { onGuiNote("Save") }
                Spacer(modifier = Modifier.width(7.dp))
                HeroMini(R.drawable.img_icon_whatsapp, Color(0xFFDE4A51).copy(alpha = 0.15f), Color(0xFFDE4A51).copy(alpha = 0.4f)) { onGuiNote("WhatsApp") }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.clip(RoundedCornerShape(18.dp)).background(Color(0xFFB02A31).copy(alpha = 0.1f))
                        .border(1.dp, Color(0xFFB02A31).copy(alpha = 0.3f), RoundedCornerShape(18.dp)).clickable { onGuiNote("Shift") }
                        .padding(horizontal = 9.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Schedule, contentDescription = null, tint = Color(0xFFE6353E), modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(shift, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7A1A1F))
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = Color(0xFF7A1A1F).copy(alpha = 0.7f), modifier = Modifier.size(14.dp))
                }
                Row(
                    modifier = Modifier.clip(RoundedCornerShape(18.dp)).background(TdInk.copy(alpha = 0.05f))
                        .border(1.dp, TdInk.copy(alpha = 0.1f), RoundedCornerShape(18.dp)).clickable { onGuiNote("Date Picker") }
                        .padding(horizontal = 12.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Today, contentDescription = null, tint = Color(0xFFE6353E), modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH).format(Date()), fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = TdInk)
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = TdInk.copy(alpha = 0.7f), modifier = Modifier.size(14.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text("Total Shift Revenue", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = TdMuted, letterSpacing = 0.3.sp)
            Text("Rs. 0", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = TdInk, letterSpacing = (-0.8).sp)
        }
    }
}

@Composable
private fun HeroMini(image: Int, bg: Color, border: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(29.dp).clip(CircleShape).background(bg).border(1.dp, border, CircleShape).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) { Image(painterResource(id = image), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.size(28.dp).clip(CircleShape)) }
}

// Opening / Closing switch: white card (kona 18), active button gradient (Opening neela, Closing laal).
@Composable
private fun ModeSwitch(closing: Boolean, onChange: (Boolean) -> Unit) {
    val shape = RoundedCornerShape(18.dp)
    Row(
        modifier = Modifier.fillMaxWidth().cssShadow(4.dp, shape, TdInk.copy(alpha = 0.07f)).background(Color.White, shape)
            .border(1.3.dp, TdInk.copy(alpha = 0.1f), shape).padding(5.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        SwitchButton("Opening Data", Icons.Filled.MeetingRoom, !closing, listOf(Color(0xFF4CB4FF), Color(0xFF1A6FD8)), Color.White) { onChange(false) }
        SwitchButton("Closing Data", Icons.Filled.DoorFront, closing, listOf(Color(0xFFE6353E), Color(0xFFA30D14)), Color(0xFF230607)) { onChange(true) }
    }
}

@Composable
private fun RowScope.SwitchButton(label: String, icon: ImageVector, active: Boolean, grad: List<Color>, activeText: Color, onClick: () -> Unit) {
    val shape = RoundedCornerShape(13.dp)
    Row(
        modifier = Modifier.weight(1f).height(40.dp)
            .then(if (active) Modifier.cssShadow(6.dp, shape, grad[0].copy(alpha = 0.45f)) else Modifier)
            .clip(shape).then(if (active) Modifier.background(cssLinear(135f, 0f to grad[0], 1f to grad[1])) else Modifier)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    ) {
        val c = if (active) activeText else TdMuted
        Icon(icon, contentDescription = null, tint = c, modifier = Modifier.size(15.dp))
        Spacer(modifier = Modifier.width(7.dp))
        Text(label, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = c)
    }
}

// ---------------- card + field building blocks ----------------
private class CardStyle(val stops: Array<Pair<Float, Color>>, val border: Color)
private val P1 = CardStyle(arrayOf(0f to Color(0xFFF7D7D9), 1f to Color(0xFFEFB1B4)), Color(0xFF9E1F25).copy(alpha = 0.3f))
private val P2 = CardStyle(arrayOf(0f to Color(0xFFFDF3D7), 1f to Color(0xFFFBE3A6)), Color(0xFFB8860B).copy(alpha = 0.3f))
private val P3 = CardStyle(arrayOf(0f to Color(0xFFF4D2D4), 1f to Color(0xFFE8A7AA)), Color(0xFF0F766E).copy(alpha = 0.3f))
private val P4 = CardStyle(arrayOf(0f to Color(0xFFDCEAFF), 1f to Color(0xFFB9D4FB)), Color(0xFF2563EB).copy(alpha = 0.3f))

private class Badge(val text: Color, val tint: Color)
private val Cyan = Badge(Color(0xFF0E7490), Color(0xFF06B6D4))
private val Amber = Badge(Color(0xFFB45309), Color(0xFFF59E0B))
private val Purple = Badge(Color(0xFF7E22CE), Color(0xFFA855F7))
private val Orange = Badge(Color(0xFFC2410C), Color(0xFFEA580C))
private val RedB = Badge(Color(0xFF78040A), Color(0xFFB91018))

@Composable
private fun TomCard(style: CardStyle, icon: Int, title: String, badge: String, badgeStyle: Badge, badgeIcon: ImageVector? = null, content: @Composable () -> Unit) {
    val shape = RoundedCornerShape(22.dp)
    Box(modifier = Modifier.fillMaxWidth().cssShadow(6.dp, shape, TdInk.copy(alpha = 0.08f)).clip(shape).background(cssLinear(120f, *style.stops)).border(1.dp, style.border, shape)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(32.dp).cssShadow(2.dp, RoundedCornerShape(10.dp), Color.Black.copy(alpha = 0.3f)).clip(RoundedCornerShape(10.dp))) {
                    Image(painterResource(id = icon), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TdInk, letterSpacing = (-0.2).sp, lineHeight = 18.5.sp, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(6.dp))
                Row(
                    modifier = Modifier.cssShadow(2.dp, RoundedCornerShape(20.dp), Color.Black.copy(alpha = 0.25f))
                        .background(badgeStyle.tint.copy(alpha = 0.13f), RoundedCornerShape(20.dp)).border(1.dp, badgeStyle.tint.copy(alpha = 0.42f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (badgeIcon != null) { Icon(badgeIcon, contentDescription = null, tint = badgeStyle.text, modifier = Modifier.size(11.dp)); Spacer(modifier = Modifier.width(4.dp)) }
                    Text(badge.uppercase(), fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = badgeStyle.text, letterSpacing = 0.5.sp)
                }
            }
            Spacer(modifier = Modifier.height(3.dp))
            content()
        }
    }
}

private class Field(val key: String, val label: String, val labelColor: Color = TdInk, val readOnly: Boolean = false)

// Ek line me fields (gap 8/9), har ek: 10sp uppercase label + 36dp input (kona 12, #F6F2F2).
@Composable
private fun FieldRow(vm: TomDataViewModel, fields: List<Field>) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(if (fields.size == 4) 8.dp else 9.dp)) {
        fields.forEach { f ->
            Column(modifier = Modifier.weight(1f)) {
                Text(f.label.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = f.labelColor, letterSpacing = 0.3.sp, maxLines = 1)
                Spacer(modifier = Modifier.height(5.dp))
                TdInput(vm.values[f.key] ?: "", f.readOnly) { vm.values[f.key] = it }
            }
        }
    }
}

@Composable
private fun TdInput(value: String, readOnly: Boolean, onValue: (String) -> Unit) {
    val shape = RoundedCornerShape(12.dp)
    BasicTextField(
        value = value, onValueChange = { t -> onValue(t.filter { it.isDigit() }.take(8)) }, readOnly = readOnly, singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = TextStyle(fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = if (readOnly) Color(0xFF7A1A1F) else TdInk, textAlign = TextAlign.Center),
        modifier = Modifier.fillMaxWidth(),
        decorationBox = { inner ->
            Box(
                modifier = Modifier.fillMaxWidth().height(36.dp).clip(shape)
                    .then(
                        if (readOnly) Modifier.background(Brush.verticalGradient(listOf(Color(0xFFB02A31).copy(alpha = 0.1f), Color(0xFFB02A31).copy(alpha = 0.18f))))
                            .border(1.3.dp, Color(0xFFB02A31).copy(alpha = 0.35f), shape)
                        else Modifier.background(Color(0xFFF6F2F2)).border(1.3.dp, TdInk.copy(alpha = 0.12f), shape)
                    ).padding(horizontal = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                if (value.isEmpty()) Text("0", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = if (readOnly) Color(0xFF7A1A1F) else TdInk.copy(alpha = 0.35f), textAlign = TextAlign.Center)
                inner()
            }
        }
    )
}

// Buttons: kona 18; primary amber, WhatsApp green, secondary white (ya laal tint).
@Composable
private fun TdButton(label: String, icon: ImageVector, kind: String, modifier: Modifier = Modifier.fillMaxWidth(), onClick: () -> Unit) {
    val shape = RoundedCornerShape(18.dp)
    val (h, fs, fw) = when (kind) { "primary", "closingEnter" -> Triple(45.dp, 15.sp, FontWeight.Bold); else -> Triple(41.dp, 13.5.sp, FontWeight.SemiBold) }
    val fg = when (kind) { "secondary" -> TdInk; "danger" -> Color(0xFFF87171); else -> Color.White }
    Row(
        modifier = modifier.height(h)
            .then(
                when (kind) {
                    "primary" -> Modifier.cssShadow(8.dp, shape, Color(0xFFF59E0B).copy(alpha = 0.4f)).clip(shape).background(cssLinear(135f, 0f to Color(0xFFF59E0B), 1f to Color(0xFFD97706)))
                    "whatsapp" -> Modifier.cssShadow(5.dp, shape, Color(0xFF25D366).copy(alpha = 0.3f)).clip(shape).background(cssLinear(135f, 0f to Color(0xFF25D366), 1f to Color(0xFF128C7E)))
                    "danger" -> Modifier.clip(shape).background(Color(0xFFEF4444).copy(alpha = 0.08f)).border(1.dp, Color(0xFFEF4444).copy(alpha = 0.4f), shape)
                    else -> Modifier.clip(shape).background(Color.White).border(1.dp, TdInk.copy(alpha = 0.12f), shape)
                }
            ).clickable { onClick() },
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = fg, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, fontSize = fs, fontWeight = fw, color = fg, maxLines = 1)
    }
}

// ---------------- OPENING MODE ----------------
@Composable
private fun OpeningMode(vm: TomDataViewModel, onGuiNote: (String) -> Unit) {
    TomCard(P1, R.drawable.img_icon_roll, "Roll Opening Counts", "Auto Opening", Cyan, Icons.Filled.AutoFixHigh) {
        FieldRow(vm, listOf(Field("op_bop", "BOP Roll Opening", Color(0xFFC084FC)), Field("op_tom", "TOM Roll Opening", Color(0xFFB02A31)), Field("op_tkm", "TKM Roll Opening", Color(0xFFB91018))))
    }
    Spacer(modifier = Modifier.height(14.dp))
    TomCard(P2, R.drawable.img_icon_lcd, "TOM Opening Balances", "Machine Opening", Amber) {
        FieldRow(vm, listOf(Field("op_tom1", "TOM 01 Opening", Color(0xFF38BDF8)), Field("op_tom2", "TOM 02 Opening", Color(0xFFFBBF24))))
    }
    Spacer(modifier = Modifier.height(14.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        TdButton("Send to WhatsApp", Icons.Filled.Chat, "whatsapp", Modifier.weight(1f)) { onGuiNote("Send to WhatsApp") }
        Spacer(modifier = Modifier.weight(1f))
    }
    Spacer(modifier = Modifier.height(10.dp))
    TdButton("Save Opening", Icons.Filled.Save, "primary") { onGuiNote("Save Opening") }
    Spacer(modifier = Modifier.height(10.dp))
    TdButton("Copy Opening Report", Icons.Filled.ContentCopy, "secondary") { onGuiNote("Copy Opening Report") }
}

// ---------------- CLOSING MODE ----------------
@Composable
private fun ClosingMode(vm: TomDataViewModel, onGuiNote: (String) -> Unit) {
    val shape = RoundedCornerShape(18.dp)
    Row(
        modifier = Modifier.height(47.dp).cssShadow(5.dp, shape, Color(0xFFB02A31).copy(alpha = 0.32f)).clip(shape)
            .background(cssLinear(135f, 0f to Color(0xFFD33941), 1f to Color(0xFF9E1F25))).clickable { onGuiNote("Enter Data (Closing Shift)") }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.EditNote, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Enter Data (Closing Shift)", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
    }
    Spacer(modifier = Modifier.height(14.dp))
    TomCard(P3, R.drawable.img_icon_roll, "BOP Roll", "Used: 0", Purple) {
        FieldRow(vm, listOf(Field("bop_open", "Opening"), Field("bop_recv", "Received"), Field("bop_dist", "Distribute"), Field("bop_close", "Closing")))
        Spacer(modifier = Modifier.height(11.dp))
        FieldRow(vm, listOf(Field("bop_cards", "Card Count"), Field("bop_slip", "Sale Slip"), Field("bop_amt", "Sale Amount (×30)", readOnly = true)))
    }
    Spacer(modifier = Modifier.height(14.dp))
    TomCard(P4, R.drawable.img_icon_roll, "TOM Roll", "Used: 0", Orange) {
        FieldRow(vm, listOf(Field("tom_open", "Opening"), Field("tom_recv", "Received"), Field("tom_dist", "Distribute"), Field("tom_close", "Closing")))
    }
    Spacer(modifier = Modifier.height(14.dp))
    TomCard(P1, R.drawable.img_icon_roll, "TKM Roll", "Used: 0", RedB) {
        FieldRow(vm, listOf(Field("tkm_open", "Opening"), Field("tkm_recv", "Received"), Field("tkm_dist", "Distribution"), Field("tkm_close", "Closing")))
    }
    Spacer(modifier = Modifier.height(14.dp))
    MachineCard(vm, P2, "TOM A", "Machine 01", Amber, "a", Color(0xFFFBBF24))
    Spacer(modifier = Modifier.height(14.dp))
    MachineCard(vm, P3, "TOM B", "Machine 02", Cyan, "b", Color(0xFF00C6FF))
    Spacer(modifier = Modifier.height(14.dp))
    GrandSummary()
    Spacer(modifier = Modifier.height(30.dp))
    TdButton("Save & Lock Daily Register", Icons.Filled.Save, "primary") { onGuiNote("Save & Lock Daily Register") }
    Spacer(modifier = Modifier.height(10.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        TdButton("Send to WhatsApp", Icons.Filled.Chat, "whatsapp", Modifier.weight(1f)) { onGuiNote("Send to WhatsApp") }
        TdButton("Copy Text", Icons.Filled.ContentCopy, "secondary", Modifier.weight(1f)) { onGuiNote("Copy Text") }
    }
    Spacer(modifier = Modifier.height(10.dp))
    TdButton("Clear / Reset All Register Fields", Icons.Filled.DeleteOutline, "danger") { vm.values.clear() }
}

@Composable
private fun MachineCard(vm: TomDataViewModel, style: CardStyle, title: String, badge: String, badgeStyle: Badge, key: String, valueColor: Color) {
    TomCard(style, R.drawable.img_icon_lcd, title, badge, badgeStyle) {
        FieldRow(vm, listOf(Field("m${key}_open", "Opening"), Field("m${key}_rech", "Recharge (+)"), Field("m${key}_close", "Closing")))
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth().cssShadow(2.dp, RoundedCornerShape(12.dp), TdInk.copy(alpha = 0.05f))
                .background(Color.White, RoundedCornerShape(12.dp)).border(1.dp, TdInk.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("$title SALE", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = TdMuted, letterSpacing = 0.3.sp)
                Text("Rs. 0", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = valueColor)
            }
            Column(horizontalAlignment = Alignment.Start) {
                Text("$title SLIPS (÷30)", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = TdMuted, letterSpacing = 0.3.sp)
                Text("0 Slips", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = valueColor)
            }
        }
    }
}

// Daily Grand Summary: gulabi card, "METRO SHIFT" kala badge, 2 + 3 white stat boxes.
@Composable
private fun GrandSummary() {
    val shape = RoundedCornerShape(22.dp)
    Column(
        modifier = Modifier.fillMaxWidth().cssShadow(10.dp, shape, Color(0xFFFF6B2B).copy(alpha = 0.18f)).clip(shape)
            .background(cssLinear(120f, *P1.stops)).border(1.dp, Color(0xFF9E1F25).copy(alpha = 0.3f), shape)
            .padding(horizontal = 16.dp, vertical = 18.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.ShowChart, contentDescription = null, tint = TdInk, modifier = Modifier.size(17.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Daily Grand Summary", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = TdInk, modifier = Modifier.weight(1f))
            Text(
                "METRO SHIFT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE6353E),
                modifier = Modifier.background(Color.Black.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFE6353E).copy(alpha = 0.35f), RoundedCornerShape(12.dp)).padding(horizontal = 10.dp, vertical = 3.dp)
            )
        }
        Spacer(modifier = Modifier.height(13.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            GrandStat("Grand Sale Amount", "Rs. 0", Modifier.weight(1.2f))
            GrandStat("Total Slips Sold", "0", Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            GrandStat("TOM Roll Used", "0", Modifier.weight(1f))
            GrandStat("BOP Roll Used", "0", Modifier.weight(1f))
            GrandStat("TKM Roll Used", "0", Modifier.weight(1f))
        }
    }
}

@Composable
private fun GrandStat(label: String, value: String, modifier: Modifier) {
    Column(
        modifier = modifier.cssShadow(2.dp, RoundedCornerShape(18.dp), TdInk.copy(alpha = 0.05f)).background(Color.White, RoundedCornerShape(18.dp))
            .border(1.dp, TdInk.copy(alpha = 0.08f), RoundedCornerShape(18.dp)).padding(horizontal = 8.dp, vertical = 10.dp)
    ) {
        Text(label, fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold, color = TdMuted, maxLines = 1)
        Spacer(modifier = Modifier.height(3.dp))
        Text(value, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = TdInk)
    }
}

// Trial ka hero bus (SVG `tom-hero-bus`, 64 viewport) — wahi shapes, gradients aur chamak.
private fun heroBusIcon(): ImageVector {
    fun rr(x: Float, y: Float, w: Float, h: Float, r: Float) =
        "M${x + r},${y}h${w - 2 * r}a$r,$r 0 0 1 $r,${r}v${h - 2 * r}a$r,$r 0 0 1 ${-r},${r}h${-(w - 2 * r)}a$r,$r 0 0 1 ${-r},${-r}v${-(h - 2 * r)}a$r,$r 0 0 1 $r,${-r}z"
    fun circle(cx: Float, cy: Float, r: Float) = "M${cx - r},${cy}a$r,$r 0 1,0 ${2 * r},0a$r,$r 0 1,0 ${-2 * r},0z"
    fun n(d: String) = PathParser().parsePathString(d).toNodes()
    return ImageVector.Builder(defaultWidth = 64.dp, defaultHeight = 64.dp, viewportWidth = 64f, viewportHeight = 64f).apply {
        addPath(n("M12,58.5a20,3.2 0 1,0 40,0a20,3.2 0 1,0 -40,0z"), fill = SolidColor(Color.Black), fillAlpha = 0.18f)
        addPath(n(rr(10f, 9f, 44f, 45f, 12f)), fill = SolidColor(Color(0xFF7A0F13)))
        addPath(n(rr(10f, 7f, 44f, 44f, 12f)), fill = Brush.linearGradient(0f to Color(0xFFFF6B6B), 0.55f to Color(0xFFE5383B), 1f to Color(0xFFA4161A), start = Offset(0f, 7f), end = Offset(0f, 51f)))
        addPath(n(rr(16f, 9.5f, 32f, 5f, 2.5f)), fill = Brush.linearGradient(0f to Color.White, 1f to Color(0xFFFFD6D6), start = Offset(0f, 9.5f), end = Offset(0f, 14.5f)), fillAlpha = 0.9f)
        addPath(n(rr(15f, 17f, 34f, 16f, 5f)), fill = SolidColor(Color(0xFF5C0B0E)))
        addPath(n(rr(15.5f, 16.5f, 33f, 15.5f, 5f)), fill = Brush.linearGradient(0f to Color(0xFFE0F4FF), 0.5f to Color(0xFF8FD3FF), 1f to Color(0xFF3A86D8), start = Offset(15.5f, 16.5f), end = Offset(48.5f, 32f)))
        addPath(n("M19 19.5 L27 19.5 L21 30 L18 30 Z"), fill = SolidColor(Color.White), fillAlpha = 0.55f)
        addPath(n(rr(15f, 36f, 34f, 5f, 2.5f)), fill = SolidColor(Color.White), fillAlpha = 0.9f)
        for (cx in listOf(19f, 45f)) {
            addPath(n(circle(cx, 45.5f, 3.2f)), fill = SolidColor(Color(0xFFFFF6C2)))
            addPath(n(circle(cx, 45.5f, 1.6f)), fill = SolidColor(Color(0xFFFFD23F)))
        }
        addPath(n(rr(26f, 44f, 12f, 3f, 1.5f)), fill = SolidColor(Color(0xFF5C0B0E)), fillAlpha = 0.6f)
        addPath(n(rr(13f, 50f, 9f, 7f, 3f)), fill = SolidColor(Color(0xFF1C1C22)))
        addPath(n(rr(42f, 50f, 9f, 7f, 3f)), fill = SolidColor(Color(0xFF1C1C22)))
        addPath(n("M15 12 Q32 5 49 12"), stroke = SolidColor(Color.White), strokeAlpha = 0.55f, strokeLineWidth = 1.6f, strokeLineCap = StrokeCap.Round)
    }.build()
}
