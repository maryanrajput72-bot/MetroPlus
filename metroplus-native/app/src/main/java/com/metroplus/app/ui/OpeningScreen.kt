package com.metroplus.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.metroplus.app.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Opening page — trial 2.1.63 ke openingDataScreen ki EXACT copy (computed CSS se naapa, 1px = 1dp):
// menu (orange hero + 4 tiles), Opening Data (2 rangeen cards, 3+2 inputs, 3 buttons),
// Staff Attendance (laal hero + bus, rangeen operator cards, time stepper, status menu),
// Leave Form (purple header, Leave + Overtime cards, rangeen staff chips), GPS Picture.
// SIRF GUI: Save/Share/Copy wiring agle step me.
internal enum class OpTab { MENU, DATA, STAFF, LEAVE, GPS }

private val OpInk = Color(0xFF14181C)
private val OpMuted = Color(0xFF6B7280)

private val demoStaff = listOf("Ahsan" to "1994", "Shehryar" to "3552", "Tahir" to "2022", "Shahid" to "2884", "Usman" to "3212")

// Trial ke 5 staff status (pill + menu option ke rang).
internal enum class StaffStatus(val label: String, val pill: List<Color>, val pillBorder: Color, val opt: List<Color>, val optText: Color, val optBorder: Color) {
    ON_DUTY("ON DUTY", listOf(Color(0xFF1DB87A), Color(0xFF046B50)), Color(0xFF03503C), listOf(Color(0xFFA7EFC9), Color(0xFF5ED7A0)), Color(0xFF065A3E), Color(0xFF8FE3B8)),
    OFF("OFF", listOf(Color(0xFFF43F5E), Color(0xFFA80D2C)), Color(0xFF8A0A24), listOf(Color(0xFFF9B8C6), Color(0xFFF27D95)), Color(0xFF8A0A24), Color(0xFFF2A0B2)),
    WAITING("WAITING", listOf(Color(0xFFD9A400), Color(0xFF7A5500)), Color(0xFF5E4200), listOf(Color(0xFFFDE68A), Color(0xFFF5C94A)), Color(0xFF6B4E00), Color(0xFFEDD27A)),
    LEAVE("LEAVE", listOf(Color(0xFF8B3DF0), Color(0xFF4A0FA0)), Color(0xFF3A0B7E), listOf(Color(0xFFCFC2FB), Color(0xFF9D85F5)), Color(0xFF3E0E7C), Color(0xFFBFA9F7)),
    ABSENT("ABSENT", listOf(Color(0xFF6B7490), Color(0xFF2E3342)), Color(0xFF232733), listOf(Color(0xFFC3CAD6), Color(0xFF8E99AD)), Color(0xFF232733), Color(0xFFB0B8C6))
}

// Opening ka sara likha hua (tab, counts, staff, leave) ViewModel me — page band karke
// dobara kholo ya Camera se wapas aao to sab wahi mile.
internal class OpeningViewModel : ViewModel() {
    val tab = mutableStateOf(OpTab.MENU)
    val bop = mutableStateOf("")
    val tom = mutableStateOf("")
    val tkm = mutableStateOf("")
    val tom1 = mutableStateOf("")
    val tom2 = mutableStateOf("")
    val status = mutableStateListOf(*Array(demoStaff.size) { StaffStatus.ON_DUTY })
    val dutyMinutes = mutableStateListOf(*Array(demoStaff.size) { 110 })
    val leaveShift = mutableStateOf(if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 12) "Morning" else "Evening")
    val leaveName = mutableStateOf("")
    val leaveId = mutableStateOf("")
    val picked = mutableStateOf<String?>(null)
    val otShift = mutableStateOf(leaveShift.value)
    val otName = mutableStateOf("")
    val otId = mutableStateOf("")
}

private fun demoShift(): String = if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 12) "Morning" else "Evening"

@Composable
fun OpeningScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit, onOpenCamera: () -> Unit) {
    val vm: OpeningViewModel = viewModel()
    var tab by vm.tab
    BackHandler { if (tab != OpTab.MENU) tab = OpTab.MENU else onBack() }

    val (titleIcon, titleTint, title) = when (tab) {
        OpTab.MENU -> Triple(Icons.Filled.WbSunny, Color(0xFFF59E0B), "Opening")
        OpTab.DATA -> Triple(Icons.Filled.FolderOpen, Color(0xFFF59E0B), "Opening Data")
        OpTab.STAFF -> Triple(Icons.Filled.Groups, Color(0xFFA32228), "Staff Attendance")
        OpTab.LEAVE -> Triple(Icons.Filled.CalendarMonth, Color(0xFF0F766E), "Leave Form")
        OpTab.GPS -> Triple(Icons.Filled.PhotoCamera, Color(0xFF1A6FD8), "GPS Picture")
    }
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF3EDED))) {
        // Header: white 94%, padding 16/20, 44dp gol back (halki border), beech me icon + 17sp title.
        Row(
            modifier = Modifier.fillMaxWidth().background(Color.White.copy(alpha = 0.94f)).statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape).background(Color.White).border(1.dp, OpInk.copy(alpha = 0.09f), CircleShape)
                    .clickable { if (tab != OpTab.MENU) tab = OpTab.MENU else onBack() },
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = OpInk, modifier = Modifier.size(19.dp)) }
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Icon(titleIcon, contentDescription = null, tint = titleTint, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = OpInk)
            }
            Spacer(modifier = Modifier.width(44.dp))
        }
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            when (tab) {
                OpTab.MENU -> OpeningMenu { tab = it }
                OpTab.DATA -> OpeningDataTab(vm, onGuiNote)
                OpTab.STAFF -> OpeningStaffTab(vm, onGuiNote)
                OpTab.LEAVE -> OpeningLeaveTab(vm, onGuiNote)
                OpTab.GPS -> OpeningGpsTab(onGuiNote, onOpenCamera)
            }
        }
    }
}

// ---------------- MENU ----------------
@Composable
private fun OpeningMenu(open: (OpTab) -> Unit) {
    val heroDate = SimpleDateFormat("EEEE, MMM d", Locale.ENGLISH).format(Date())
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Hero: 135° #FFB547→#FF8A1E→#F2641A, kona 24, padding 18, sab likhai baein.
        val heroShape = RoundedCornerShape(24.dp)
        Box(
            modifier = Modifier.fillMaxWidth().cssShadow(12.dp, heroShape, Color(0xFFF2641A).copy(alpha = 0.28f)).clip(heroShape)
                .background(cssLinear(135f, 0f to Color(0xFFFFB547), 0.55f to Color(0xFFFF8A1E), 1f to Color(0xFFF2641A)))
        ) {
            Watermark(126.dp, x = 237.dp, y = (-14).dp, alpha = 0.2f)
            Column(modifier = Modifier.padding(18.dp)) {
                Text("START OF SHIFT", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, letterSpacing = 1.2.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Shaheed-e-Millat", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                Spacer(modifier = Modifier.height(2.dp))
                Text("${demoShift()} shift · $heroDate", fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
        }
        PlainTile("Opening Data", "Roll counts & TOM opening balances", listOf(Color(0xFFFFF4DC), Color(0xFFFFE0A8)), { open(OpTab.DATA) }) {
            Image(painterResource(id = R.drawable.ic_dock_opening), contentDescription = null, modifier = Modifier.size(46.dp))
        }
        FancyTile("Staff Attendance", "Who is on duty today", listOf(Color(0xFFD93C44), Color(0xFFA32228)), R.drawable.staff_attendance_3d) { open(OpTab.STAFF) }
        FancyTile("Leave Form", "Leave and overtime notes", listOf(Color(0xFF7EF0C1), Color(0xFF0F766E)), R.drawable.leave_form_3d) { open(OpTab.LEAVE) }
        PlainTile("GPS Picture", "Latest GPS photo + staff attendance", listOf(Color(0xFF4CB4FF), Color(0xFF1A6FD8)), { open(OpTab.GPS) }) {
            Icon(Icons.Filled.PhotoCamera, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
        }
    }
}

// Opening Data / GPS tile: white, kona 22, padding 14, 58dp gradient icon, 16sp title, 32dp grey arrow.
@Composable
private fun PlainTile(title: String, sub: String, iconBg: List<Color>, onClick: () -> Unit, icon: @Composable () -> Unit) {
    val shape = RoundedCornerShape(22.dp)
    Box(
        modifier = Modifier.fillMaxWidth().cssShadow(6.dp, shape, Color(0xFF281415).copy(alpha = 0.07f)).clip(shape)
            .background(Color.White).border(1.dp, OpInk.copy(alpha = 0.06f), shape).clickable { onClick() }
    ) {
        Watermark(132.dp, x = 232.dp, y = (-22).dp, alpha = 0.075f)
        Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(58.dp).background(cssLinear(145f, 0f to iconBg[0], 1f to iconBg[1]), RoundedCornerShape(18.dp)), contentAlignment = Alignment.Center) { icon() }
            Column(modifier = Modifier.weight(1f).padding(start = 14.dp, end = 10.dp)) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = OpInk)
                Spacer(modifier = Modifier.height(2.dp))
                Text(sub, fontSize = 12.5.sp, color = OpMuted, lineHeight = 15.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
            Box(modifier = Modifier.size(32.dp).background(Color(0xFFF5F1F1), CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color(0xFF5B6B57), modifier = Modifier.size(18.dp))
            }
        }
    }
}

// Staff / Leave tile: trial .op-tile jaisa — white, kona 22, padding 14, 3D image tile,
// 18sp heavy title, 38dp white arrow; sub 1 line (is se sab tiles ki height barabar 86dp rehti hai).
@Composable
private fun FancyTile(title: String, sub: String, iconBg: List<Color>, image: Int, onClick: () -> Unit) {
    val shape = RoundedCornerShape(22.dp)
    Box(
        modifier = Modifier.fillMaxWidth().cssShadow(6.dp, shape, Color(0xFF281415).copy(alpha = 0.07f)).clip(shape)
            .background(Color.White).border(1.dp, OpInk.copy(alpha = 0.06f), shape)
            .clickable { onClick() }
    ) {
        Watermark(138.dp, x = 228.dp, y = (-23).dp, alpha = 0.075f)
        Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(58.dp).cssShadow(8.dp, RoundedCornerShape(18.dp), Color(0xFF281437).copy(alpha = 0.18f))
                    .background(cssLinear(145f, 0f to iconBg[0], 1f to iconBg[1]), RoundedCornerShape(18.dp)).clip(RoundedCornerShape(18.dp))
            ) { Image(painterResource(id = image), contentDescription = null, contentScale = ContentScale.Fit, modifier = Modifier.fillMaxSize()) }
            Column(modifier = Modifier.weight(1f).padding(start = 14.dp, end = 10.dp)) {
                Text(title, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF20152B))
                Spacer(modifier = Modifier.height(2.dp))
                Text(sub, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF716878), lineHeight = 18.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Box(
                modifier = Modifier.size(38.dp).cssShadow(3.dp, CircleShape, Color(0xFF2D143C).copy(alpha = 0.10f)).background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color(0xFF5B287E), modifier = Modifier.size(20.dp)) }
        }
    }
}

// ---------------- OPENING DATA ----------------
@Composable
private fun OpeningDataTab(vm: OpeningViewModel, onGuiNote: (String) -> Unit) {
    var bop by vm.bop
    var tom by vm.tom
    var tkm by vm.tkm
    var tom1 by vm.tom1
    var tom2 by vm.tom2
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
        DataCard(Color(0xFFFAF0F0), Color(0xFFF4E6E7), "🧻", "Roll Opening Counts", "✨ AUTO OPENING", Color(0xFF7C4A4C), Color(0xFFF4E6E7)) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CountField("BOP ROLL OPENING", Color(0xFF9D4EDD), bop, Modifier.weight(1f)) { bop = it }
                CountField("TOM ROLL OPENING", Color(0xFF6A2D30), tom, Modifier.weight(1f)) { tom = it }
                CountField("TKM ROLL OPENING", Color(0xFF6A2D30), tkm, Modifier.weight(1f)) { tkm = it }
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        DataCard(Color(0xFFFFFDF5), Color(0xFFFEF3E2), "🖥️", "TOM Opening Balances", "MACHINE OPENING", Color(0xFFB45309), Color(0xFFFEF3E2)) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CountField("TOM 01 OPENING", Color(0xFF219EBC), tom1, Modifier.weight(1f)) { tom1 = it }
                CountField("TOM 02 OPENING", Color(0xFFE9A426), tom2, Modifier.weight(1f)) { tom2 = it }
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        BigButton("Send to WhatsApp", Icons.Filled.Chat, listOf(Color(0xFF25D366), Color(0xFF128C7E)), Color.White) { onGuiNote("Send to WhatsApp") }
        Spacer(modifier = Modifier.height(10.dp))
        BigButton("Save Opening", Icons.Filled.Save, listOf(Color(0xFFF59E0B), Color(0xFFD97706)), Color.White) { onGuiNote("Save Opening") }
        Spacer(modifier = Modifier.height(10.dp))
        BigButton("Copy Opening Report", Icons.Filled.ContentCopy, null, Color(0xFF333333)) { onGuiNote("Copy Opening Report") }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

// Rangeen card: kona 22, padding 16; upar emoji (28sp) + 16sp title + gol chip.
@Composable
private fun DataCard(bg: Color, border: Color, emoji: String, title: String, chip: String, chipText: Color, chipBg: Color, fields: @Composable () -> Unit) {
    val shape = RoundedCornerShape(22.dp)
    Column(
        modifier = Modifier.fillMaxWidth().cssShadow(3.dp, shape, Color.Black.copy(alpha = 0.03f)).background(bg, shape).border(1.dp, border, shape).padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = OpInk, lineHeight = 20.sp, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                chip, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = chipText, lineHeight = 14.sp, textAlign = TextAlign.Center,
                modifier = Modifier.width(IntrinsicChipWidth(chip)).background(chipBg, RoundedCornerShape(20.dp)).padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
        fields()
    }
}

// Trial me chips 2 line me tootte hain (113 / 76 chaurai) — wahi chaurai.
private fun IntrinsicChipWidth(chip: String): Dp = if (chip.startsWith("✨")) 113.dp else 76.dp

@Composable
private fun CountField(label: String, labelColor: Color, value: String, modifier: Modifier, onValue: (String) -> Unit) {
    Column(modifier = modifier) {
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = labelColor, letterSpacing = 0.3.sp, lineHeight = 12.5.sp,
            modifier = Modifier.height(25.dp))
        Spacer(modifier = Modifier.height(6.dp))
        NumberBox(value, onValue, Modifier.fillMaxWidth())
    }
}

// 46dp white input, kona 16, #EEE border, 18sp bold beech me, placeholder "0".
@Composable
private fun NumberBox(value: String, onValue: (String) -> Unit, modifier: Modifier) {
    BasicTextField(
        value = value, onValueChange = { t -> onValue(t.filter { it.isDigit() }.take(7)) },
        singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black, textAlign = TextAlign.Center),
        modifier = modifier,
        decorationBox = { inner ->
            Box(
                modifier = Modifier.fillMaxWidth().height(46.dp).background(Color.White, RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(16.dp)).padding(horizontal = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                if (value.isEmpty()) Text("0", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9AA1AB), textAlign = TextAlign.Center)
                inner()
            }
        }
    )
}

// 54dp button, kona 20, 16sp bold; gradient ya white (#DDD border).
@Composable
private fun BigButton(label: String, icon: ImageVector, gradient: List<Color>?, fg: Color, onClick: () -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Row(
        modifier = Modifier.fillMaxWidth().height(54.dp).clip(shape)
            .then(if (gradient != null) Modifier.background(cssLinear(135f, 0f to gradient[0], 1f to gradient[1])) else Modifier.background(Color.White).border(1.dp, Color(0xFFDDDDDD), shape))
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = fg, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Text(label, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = fg)
    }
}

// ---------------- STAFF ATTENDANCE ----------------
private val OperatorAccents = listOf(
    Triple(Color(0xFFE31837), Color(0xFFFFE4E7), listOf(Color(0xFFFF455C), Color(0xFFC81036))),
    Triple(Color(0xFF8B24E8), Color(0xFFF0E2FF), listOf(Color(0xFFA43AF0), Color(0xFF701BC6))),
    Triple(Color(0xFF2478E8), Color(0xFFE1EEFF), listOf(Color(0xFF3998FF), Color(0xFF2164D8))),
    Triple(Color(0xFF079B69), Color(0xFFDDF8EC), listOf(Color(0xFF24CC91), Color(0xFF078B69))),
    Triple(Color(0xFFD88700), Color(0xFFFFF0C8), listOf(Color(0xFFFFC928), Color(0xFFE58B08))),
    Triple(Color(0xFFD92E91), Color(0xFFFFE2F2), listOf(Color(0xFFF65AA7), Color(0xFFC92B79)))
)

@Composable
private fun OpeningStaffTab(vm: OpeningViewModel, onGuiNote: (String) -> Unit) {
    val total = demoStaff.size
    val onDuty = vm.status.count { it == StaffStatus.ON_DUTY }
    val off = vm.status.count { it == StaffStatus.OFF }
    Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFFDF1F3)).padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 16.dp)) {
        // Hero: 135° #FF2E52→#E31837→#C81036, kona 24, upar-daein dots pattern.
        val heroShape = RoundedCornerShape(24.dp)
        Box(
            modifier = Modifier.fillMaxWidth().cssShadow(12.dp, heroShape, Color(0xFFBF1137).copy(alpha = 0.28f)).clip(heroShape)
                .background(cssLinear(135f, 0f to Color(0xFFFF2E52), 0.55f to Color(0xFFE31837), 1f to Color(0xFFC81036)))
        ) {
            Canvas(modifier = Modifier.align(Alignment.TopEnd).padding(top = 8.dp, end = 10.dp).size(90.dp, 60.dp)) {
                val step = 10.dp.toPx()
                var y = step / 2
                while (y < size.height) {
                    var x = step / 2
                    while (x < size.width) { drawCircle(Color.White.copy(alpha = 0.55f * 0.5f), 1.5.dp.toPx(), Offset(x, y)); x += step }
                    y += step
                }
            }
            Column(modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 16.dp, bottom = 14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(painterResource(id = R.drawable.metro_bus), contentDescription = "Metro bus", contentScale = ContentScale.Fit, modifier = Modifier.size(121.dp, 77.dp))
                    Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                        Text("Shaheed-e-Millat", fontSize = 19.7.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, letterSpacing = (-0.2).sp, maxLines = 1)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.CalendarMonth, contentDescription = null, tint = Color.White.copy(alpha = 0.95f), modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(SimpleDateFormat("d MMM, yyyy", Locale.ENGLISH).format(Date()), fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White.copy(alpha = 0.95f))
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.cssShadow(4.dp, RoundedCornerShape(999.dp), Color.Black.copy(alpha = 0.15f))
                                .background(Color.White, RoundedCornerShape(999.dp)).padding(horizontal = 12.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.WbSunny, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("${demoShift()} Shift", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2B1B3D))
                        }
                    }
                }
                Row(
                    modifier = Modifier.padding(top = 14.dp).fillMaxWidth().background(Color.White.copy(alpha = 0.96f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 6.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HeroStat("TOTAL STAFF", total, null, Modifier.weight(1f))
                    Box(modifier = Modifier.width(1.dp).height(32.dp).background(Color(0xFFEFDDE2)))
                    HeroStat("ON DUTY", onDuty, Color(0xFF22C55E), Modifier.weight(1f))
                    Box(modifier = Modifier.width(1.dp).height(32.dp).background(Color(0xFFEFDDE2)))
                    HeroStat("OFF", off, Color(0xFF9CA3AF), Modifier.weight(1f))
                }
            }
        }
        // Staff List heading: 20sp title | "N Operators" laal 18sp | purple Edit button.
        Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp).padding(horizontal = 3.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Staff List", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E1430), modifier = Modifier.weight(1f))
            Text("$total Operators", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFE11D2E))
            Spacer(modifier = Modifier.width(16.dp))
            Row(
                modifier = Modifier.height(44.dp).cssShadow(5.dp, RoundedCornerShape(13.dp), Color(0xFF8B24E8).copy(alpha = 0.24f))
                    .background(cssLinear(135f, 0f to Color(0xFF9B35F2), 1f to Color(0xFFD82EDC)), RoundedCornerShape(13.dp))
                    .clip(RoundedCornerShape(13.dp)).clickable { onGuiNote("Edit Staff List") }.padding(horizontal = 17.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.EditNote, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Edit", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            }
        }
        demoStaff.forEachIndexed { i, (name, id) -> OperatorCard(vm, i, name, id) }
        // Report actions: WhatsApp (green gradient) | Copy (white).
        Row(modifier = Modifier.fillMaxWidth().padding(top = 1.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.weight(1f).height(48.dp).cssShadow(8.dp, RoundedCornerShape(13.dp), Color(0xFF128C7E).copy(alpha = 0.3f))
                    .background(cssLinear(135f, 0f to Color(0xFF20C981), 1f to Color(0xFF078B78)), RoundedCornerShape(13.dp))
                    .clip(RoundedCornerShape(13.dp)).clickable { onGuiNote("WhatsApp") },
                horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Chat, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp))
                Spacer(modifier = Modifier.width(7.dp))
                Text("WhatsApp", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            }
            Row(
                modifier = Modifier.weight(1f).height(48.dp).background(Color.White, RoundedCornerShape(13.dp))
                    .border(1.dp, Color(0xFFE3DCE9), RoundedCornerShape(13.dp)).clip(RoundedCornerShape(13.dp)).clickable { onGuiNote("Copy") },
                horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.ContentCopy, contentDescription = null, tint = Color(0xFF30213F), modifier = Modifier.size(15.dp))
                Spacer(modifier = Modifier.width(7.dp))
                Text("Copy", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF30213F))
            }
        }
    }
}

@Composable
private fun HeroStat(label: String, value: Int, dot: Color?, modifier: Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF5B4B65), letterSpacing = 0.4.sp)
        Row(modifier = Modifier.padding(top = 2.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("$value", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF241536))
            if (dot != null) { Spacer(modifier = Modifier.width(6.dp)); Box(modifier = Modifier.size(9.dp).background(dot, CircleShape)) }
        }
    }
}

// Operator card: tint→white gradient, baein 4dp rang ki patti, ID badge, naam, status pill (menu),
// ON DUTY ho to neeche −/time/+ (purple).
@Composable
private fun OperatorCard(vm: OpeningViewModel, i: Int, name: String, id: String) {
    val (accent, tint, badge) = OperatorAccents[i % OperatorAccents.size]
    val status = vm.status[i]
    var menuOpen by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(18.dp)
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 11.dp).height(IntrinsicSize.Min)
            .cssShadow(5.dp, shape, Color(0xFF361E4B).copy(alpha = 0.09f)).clip(shape)
            .background(cssLinear(135f, 0f to tint, 1f to Color.White)).border(1.dp, tint, shape)
    ) {
        Box(modifier = Modifier.width(4.dp).fillMaxHeight().background(accent))
        Column(modifier = Modifier.weight(1f).padding(horizontal = 6.dp, vertical = 10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(52.dp, 40.dp).background(cssLinear(135f, 0f to badge[0], 1f to badge[1]), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) { Text(id, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = if (i % 6 == 4) Color(0xFF4B2800) else Color.White) }
                Text(name, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black, textAlign = TextAlign.Center, maxLines = 1,
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp))
                Box {
                    Row(
                        modifier = Modifier.height(38.dp).clip(RoundedCornerShape(12.dp))
                            .background(cssLinear(135f, 0f to status.pill[0], 1f to status.pill[1]))
                            .border(1.dp, status.pillBorder, RoundedCornerShape(12.dp))
                            .clickable { menuOpen = true }.padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(status.label, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(13.dp))
                    }
                    // Trial ka status menu: 192dp, peach gradient, 5 rangeen options.
                    DropdownMenu(
                        expanded = menuOpen, onDismissRequest = { menuOpen = false },
                        modifier = Modifier.width(192.dp).background(cssLinear(160f, 0f to Color(0xFFFFF7F1), 1f to Color(0xFFFDEBF0))).padding(horizontal = 8.dp)
                    ) {
                        StaffStatus.values().forEach { s ->
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp).clip(RoundedCornerShape(12.dp))
                                    .background(cssLinear(135f, 0f to s.opt[0], 1f to s.opt[1])).border(1.dp, s.optBorder, RoundedCornerShape(12.dp))
                                    .clickable { vm.status[i] = s; menuOpen = false }.padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) { Text(s.label, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = s.optText) }
                        }
                    }
                }
            }
            if (status == StaffStatus.ON_DUTY) {
                Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    TimeStep("−") { vm.dutyMinutes[i] = (vm.dutyMinutes[i] - 10).coerceAtLeast(0) }
                    Spacer(modifier = Modifier.width(10.dp))
                    val m = vm.dutyMinutes[i]
                    Box(
                        modifier = Modifier.size(84.dp, 48.dp).background(Color.White, RoundedCornerShape(13.dp)).border(1.dp, Color(0xFFDCC5F5), RoundedCornerShape(13.dp)),
                        contentAlignment = Alignment.Center
                    ) { Text(String.format(Locale.US, "%02d:%02d", m / 60, m % 60), fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black) }
                    Spacer(modifier = Modifier.width(10.dp))
                    TimeStep("+") { vm.dutyMinutes[i] = (vm.dutyMinutes[i] + 10).coerceAtMost(24 * 60) }
                }
            }
        }
    }
}

@Composable
private fun TimeStep(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(56.dp, 48.dp).cssShadow(4.dp, RoundedCornerShape(11.dp), Color(0xFF701BC6).copy(alpha = 0.23f))
            .background(cssLinear(135f, 0f to Color(0xFFA43AF0), 1f to Color(0xFF701BC6)), RoundedCornerShape(11.dp))
            .clip(RoundedCornerShape(11.dp)).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) { Text(label, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color.White) }
}

// ---------------- LEAVE FORM ----------------
private val PickColors = listOf(
    listOf(Color(0xFFF7D7D9), Color(0xFFEFB1B4)), listOf(Color(0xFFFFF3C2), Color(0xFFFFE38A)),
    listOf(Color(0xFFD1FAE5), Color(0xFFA7F3D0)), listOf(Color(0xFFDBEAFE), Color(0xFFBFDBFE)),
    listOf(Color(0xFFEDE9FE), Color(0xFFDDD6FE))
)

@Composable
private fun OpeningLeaveTab(vm: OpeningViewModel, onGuiNote: (String) -> Unit) {
    val today = SimpleDateFormat("dd-MM-yyyy", Locale.US).format(Date())
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        // Purple header: 135° #9B35F2→#7B20D4→#D82EDC, kona 22, 64dp logo tile.
        val hShape = RoundedCornerShape(22.dp)
        Row(
            modifier = Modifier.fillMaxWidth().cssShadow(10.dp, hShape, Color(0xFF7B20D4).copy(alpha = 0.25f)).clip(hShape)
                .background(cssLinear(135f, 0f to Color(0xFF9B35F2), 0.55f to Color(0xFF7B20D4), 1f to Color(0xFFD82EDC))).padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(64.dp).cssShadow(7.dp, RoundedCornerShape(19.dp), Color(0xFF3C006E).copy(alpha = 0.3f))
                    .background(cssLinear(145f, 0f to Color(0xFFD8A5FF), 1f to Color(0xFF7B20D4)), RoundedCornerShape(19.dp)).clip(RoundedCornerShape(19.dp))
            ) { Image(painterResource(id = R.drawable.leave_form_3d), contentDescription = null, contentScale = ContentScale.Fit, modifier = Modifier.fillMaxSize()) }
            Spacer(modifier = Modifier.width(13.dp))
            Column {
                Text("Leave Form", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, letterSpacing = (-0.4).sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Leave and overtime notes", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White.copy(alpha = 0.96f))
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        var shift by vm.leaveShift
        var name by vm.leaveName
        var empId by vm.leaveId
        var picked by vm.picked
        FormCard("Leave Form") {
            FormLabel("Date"); DateBox(today) { onGuiNote("Date Picker") }
            FormGap(); FormLabel("Station"); StationBox("Shaheed-e-Millat") { onGuiNote("Station Picker") }
            FormGap(); FormLabel("Shift"); ShiftPick(shift) { shift = it }
            FormGap(); FormLabel("Name"); TextBox(name, "Type name or pick below") { name = it }
            Spacer(modifier = Modifier.height(8.dp))
            StaffPicker(picked) { n, id -> picked = n; name = n; empId = id }
            FormGap(); FormLabel("Employee ID"); TextBox(empId, "Employee ID", orange = true) { empId = it }
        }
        Spacer(modifier = Modifier.height(30.dp))
        var otShift by vm.otShift
        var otName by vm.otName
        var otId by vm.otId
        FormCard("Overtime Detail") {
            FormLabel("Date"); DateBox(today) { onGuiNote("Date Picker") }
            FormGap(); FormLabel("Station"); StationBox("Select station") { onGuiNote("Station Picker") }
            FormGap(); FormLabel("Shift"); ShiftPick(otShift) { otShift = it }
            FormGap(); FormLabel("Name"); TextBox(otName, "Type name or pick below") { otName = it }
            Spacer(modifier = Modifier.height(8.dp))
            Text("No staff saved for this station", fontSize = 12.sp, color = Color(0xFF9AA1AB), modifier = Modifier.padding(vertical = 8.dp))
            FormGap(); FormLabel("Employee ID"); TextBox(otId, "Employee ID", orange = true) { otId = it }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth().height(48.dp).cssShadow(6.dp, RoundedCornerShape(14.dp), Color(0xFF8B24E8).copy(alpha = 0.25f))
                .background(cssLinear(135f, 0f to Color(0xFF9B35F2), 1f to Color(0xFFD82EDC)), RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp)).clickable { onGuiNote("Generate Note") },
            horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.EditNote, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Generate Note", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun FormCard(title: String, content: @Composable () -> Unit) {
    val shape = RoundedCornerShape(18.dp)
    Column(
        modifier = Modifier.fillMaxWidth().cssShadow(5.dp, shape, Color(0xFF422458).copy(alpha = 0.08f))
            .background(cssLinear(135f, 0f to Color(0xFFF6EDFF), 1f to Color.White), shape).border(1.dp, Color(0xFFE7D6FA), shape).padding(16.dp)
    ) {
        Text(title, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = OpInk)
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable private fun FormGap() = Spacer(modifier = Modifier.height(12.dp))

@Composable
private fun FormLabel(text: String) {
    Text(text, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5B6470), modifier = Modifier.padding(top = 4.dp, bottom = 7.dp))
}

@Composable
private fun DateBox(date: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().height(35.dp).clip(RoundedCornerShape(10.dp))
            .background(cssLinear(135f, 0f to Color(0xFFE0F2FE), 1f to Color(0xFFBFDBFE))).border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(10.dp))
            .clickable { onClick() }.padding(horizontal = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) { Text(date, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = OpInk) }
}

@Composable
private fun StationBox(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().height(37.dp).clip(RoundedCornerShape(10.dp))
            .background(cssLinear(135f, 0f to Color(0xFFDCFCE7), 1f to Color(0xFFBBF7D0))).border(1.dp, Color(0xFFBBF7D0), RoundedCornerShape(10.dp))
            .clickable { onClick() }.padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.LocationOn, contentDescription = null, tint = Color(0xFF9E1F25), modifier = Modifier.size(15.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 13.5.sp, color = OpInk, modifier = Modifier.weight(1f))
        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = Color(0xFF9AA1AB), modifier = Modifier.size(18.dp))
    }
}

// Shift: do 45dp buttons (kona 14); chuna hua laal gradient + halka ring, baqi white.
@Composable
private fun ShiftPick(selected: String, onPick: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        listOf("Morning", "Evening").forEach { s ->
            val on = s == selected
            val shape = RoundedCornerShape(14.dp)
            Box(
                modifier = Modifier.weight(1f).height(45.dp)
                    .then(if (on) Modifier.cssShadow(6.dp, shape, Color(0xFFB91018).copy(alpha = 0.35f)).border(3.dp, Color(0xFFE11D2E).copy(alpha = 0.25f), shape).padding(1.dp) else Modifier)
                    .clip(shape)
                    .then(if (on) Modifier.background(cssLinear(135f, 0f to Color(0xFFE11D2E), 1f to Color(0xFFB91018))) else Modifier.background(Color.White).border(1.3.dp, Color(0xFFE5E7EB), shape))
                    .clickable { onPick(s) },
                contentAlignment = Alignment.Center
            ) { Text(s, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (on) Color.White else Color(0xFF5B6470)) }
        }
    }
}

@Composable
private fun TextBox(value: String, placeholder: String, orange: Boolean = false, onValue: (String) -> Unit) {
    BasicTextField(
        value = value, onValueChange = { onValue(it.take(40)) }, singleLine = true,
        textStyle = TextStyle(fontSize = 13.5.sp, color = OpInk),
        modifier = Modifier.fillMaxWidth(),
        decorationBox = { inner ->
            Box(
                modifier = Modifier.fillMaxWidth().height(35.dp).clip(RoundedCornerShape(10.dp))
                    .then(
                        if (orange) Modifier.background(cssLinear(135f, 0f to Color(0xFFFFEDD5), 1f to Color(0xFFFED7AA))).border(1.dp, Color(0xFFFED7AA), RoundedCornerShape(10.dp))
                        else Modifier.background(Color(0xFFFAF7F7)).border(1.dp, OpInk.copy(alpha = 0.14f), RoundedCornerShape(10.dp))
                    ).padding(horizontal = 10.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) Text(placeholder, fontSize = 13.5.sp, color = Color(0xFF9AA1AB))
                inner()
            }
        }
    )
}

// Staff chips: 54dp, kona 16, rangeen 120° gradients (5 rang ghoomte), naam beech me + ID tag;
// jitni jagah ho phail jate hain (trial ka flex-wrap).
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StaffPicker(picked: String?, onPick: (String, String) -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        demoStaff.forEachIndexed { i, (name, id) ->
            val c = PickColors[i % PickColors.size]
            val shape = RoundedCornerShape(16.dp)
            Row(
                modifier = Modifier.weight(1f).height(54.dp).cssShadow(2.dp, shape, OpInk.copy(alpha = 0.05f)).clip(shape)
                    .background(cssLinear(120f, 0f to c[0], 1f to c[1]))
                    .border(if (picked == name) 2.dp else 1.3.dp, if (picked == name) Color(0xFFE11D2E) else Color(0xFFE9EDF0), shape)
                    .clickable { onPick(name, id) }.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = OpInk, textAlign = TextAlign.Center, maxLines = 1, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(12.dp))
                Text(id, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9AA1AB),
                    modifier = Modifier.background(Color(0xFFF0F3F5), RoundedCornerShape(8.dp)).padding(horizontal = 9.dp, vertical = 4.dp))
            }
        }
    }
}

// ---------------- GPS PICTURE ----------------
@Composable
private fun OpeningGpsTab(onGuiNote: (String) -> Unit, onOpenCamera: () -> Unit) {
    val shape = RoundedCornerShape(24.dp)
    Column(modifier = Modifier.padding(16.dp)) {
        Box(
            modifier = Modifier.fillMaxWidth().height(300.dp).cssShadow(8.dp, shape, Color(0xFF281415).copy(alpha = 0.08f)).clip(shape)
                .background(cssLinear(135f, 0f to Color(0xFFE8F2FF), 1f to Color(0xFFCFE3FF))).border(1.dp, OpInk.copy(alpha = 0.06f), shape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Filled.Image, contentDescription = null, tint = Color(0xFF5B7AA8), modifier = Modifier.size(38.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text("No GPS photo yet — take one with the GPS Camera", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5B7AA8), textAlign = TextAlign.Center)
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        Column(
            modifier = Modifier.fillMaxWidth().cssShadow(8.dp, shape, Color(0xFF281415).copy(alpha = 0.08f)).background(Color.White, shape)
                .border(1.dp, OpInk.copy(alpha = 0.06f), shape).padding(16.dp)
        ) {
            Text("GPS CAMERA", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1A6FD8), letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text("GPS Picture", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = OpInk)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Swipe the photo for older GPS photos. Share the shown photo with today’s staff attendance.", fontSize = 13.sp, color = OpMuted, lineHeight = 19.sp)
            Spacer(modifier = Modifier.height(14.dp))
            GpsButton("Share on WhatsApp", Icons.Filled.Chat, true) { onGuiNote("Share on WhatsApp") }
            Spacer(modifier = Modifier.height(10.dp))
            GpsButton("Open GPS Camera", Icons.Filled.PhotoCamera, false) { onOpenCamera() }
            Spacer(modifier = Modifier.height(10.dp))
            GpsButton("Remove Photo", Icons.Filled.Delete, false) { onGuiNote("Remove Photo") }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun GpsButton(label: String, icon: ImageVector, green: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier.fillMaxWidth().height(48.dp)
            .then(if (green) Modifier.cssShadow(8.dp, shape, Color(0xFF128C7E).copy(alpha = 0.3f)) else Modifier)
            .clip(shape)
            .then(if (green) Modifier.background(cssLinear(135f, 0f to Color(0xFF25D366), 1f to Color(0xFF128C7E))) else Modifier.background(Color.White).border(1.3.dp, Color(0xFFE0E0E0), shape))
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = if (green) Color.White else Color(0xFF333333), modifier = Modifier.size(17.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = if (green) Color.White else Color(0xFF333333))
    }
}
