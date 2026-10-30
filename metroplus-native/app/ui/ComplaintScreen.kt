package com.metroplus.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Complaints (trial 2.1.63 ki complaintsScreen + complaintFormScreen ki EXACT copy, GUI phase).
// List: "All Complaints" — 2x2 grid, 4 cards (TOM / AGM / TKM / Other).
// Card pe tap → form page (us type ka): Report Info + type card + Generate Letter
// + Letter Preview + Share on WhatsApp. Function (save/WhatsApp) agle step me.
// PMBS (Incident Info) variant bhi isi file me hai — Closing ka PMBS card
// Item 6 me isi form se wire hoga.

private val CmpInk = Color(0xFF14181C)
private val CmpGray = Color(0xFF6B7280)
private val CmpRed = Color(0xFF9E1F25)

internal enum class CmpType(val title: String) {
    TOM("TOM Complaints"), AGM("AGM Complaint"), TKM("TKM Complaint"),
    OTHER("Other Complaints"), PMBS("PMBS Complaint"), POS("POS Card Complaint")
}

@Composable
fun ComplaintScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit) {
    var formType by remember { mutableStateOf<CmpType?>(null) }
    BackHandler { if (formType != null) formType = null else onBack() }
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF3EDED))) {
        ComplaintListScreen(onBack = onBack, onOpen = { formType = it })
        AnimatedVisibility(
            visible = formType != null,
            enter = slideInHorizontally(animationSpec = tween(350, easing = FastOutSlowInEasing)) { it } + fadeIn(animationSpec = tween(350)),
            exit = slideOutHorizontally(animationSpec = tween(350, easing = FastOutSlowInEasing)) { it } + fadeOut(animationSpec = tween(350)),
            modifier = Modifier.fillMaxSize()
        ) {
            formType?.let { t ->
                ComplaintFormScreen(type = t, onBack = { formType = null }, onGuiNote = onGuiNote)
            }
        }
    }
}

@Composable
private fun CmpHeader(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.White.copy(alpha = 0.94f)).statusBarsPadding().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(38.dp).clip(CircleShape).background(Color.White).border(1.dp, CmpInk.copy(alpha = 0.09f), CircleShape).clickable { onBack() },
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = CmpInk, modifier = Modifier.size(18.dp)) }
        Spacer(modifier = Modifier.width(12.dp))
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = CmpInk, letterSpacing = (-0.3).sp)
    }
}

@Composable
private fun ComplaintListScreen(onBack: () -> Unit, onOpen: (CmpType) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        CmpHeader("All Complaints", onBack)
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            // Batch item 10: cards pyare + colourful (laal ki jagah blue/purple
            // gradient) — TOM purple, AGM gold, TKM blue, OTHER teal, POS orange.
            Row(modifier = Modifier.height(IntrinsicSize.Max), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CmpTypeCard(CmpType.TOM, Icons.Filled.ConfirmationNumber, Color(0xFF6D28D9),
                    listOf(0f to Color(0xFFEDE9FE), 0.6f to Color(0xFFC4B5FD), 1f to Color(0xFFA78BFA)), Color(0xFF8B5CF6).copy(alpha = 0.25f),
                    Modifier.weight(1f)) { onOpen(CmpType.TOM) }
                CmpTypeCard(CmpType.AGM, Icons.Filled.Person, Color(0xFF8A6D00),
                    listOf(0f to Color(0xFFFFF6CF), 0.6f to Color(0xFFFFE08A), 1f to Color(0xFFFFCF5C)), Color(0xFFF0AA00).copy(alpha = 0.25f),
                    Modifier.weight(1f)) { onOpen(CmpType.AGM) }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.height(IntrinsicSize.Max), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CmpTypeCard(CmpType.TKM, Icons.Filled.Badge, Color(0xFF1D4ED8),
                    listOf(0f to Color(0xFFDBEAFE), 0.6f to Color(0xFF93C5FD), 1f to Color(0xFF60A5FA)), Color(0xFF3B82F6).copy(alpha = 0.25f),
                    Modifier.weight(1f)) { onOpen(CmpType.TKM) }
                CmpTypeCard(CmpType.OTHER, Icons.Filled.Description, Color(0xFF047857),
                    listOf(0f to Color(0xFFD1FAE5), 0.6f to Color(0xFF6EE7B7), 1f to Color(0xFF34D399)), Color(0xFF10B981).copy(alpha = 0.25f),
                    Modifier.weight(1f)) { onOpen(CmpType.OTHER) }
            }
            Spacer(modifier = Modifier.height(12.dp))
            // Batch item 10c: naya "POS Card Complaint" section (shell — andar ki
            // cheezein wo baad me batayega).
            Row(modifier = Modifier.height(IntrinsicSize.Max), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CmpTypeCard(CmpType.POS, Icons.Filled.CreditCard, Color(0xFFC2410C),
                    listOf(0f to Color(0xFFFFEDD5), 0.6f to Color(0xFFFDBA74), 1f to Color(0xFFFB923C)), Color(0xFFF97316).copy(alpha = 0.25f),
                    Modifier.weight(1f)) { onOpen(CmpType.POS) }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun RowScope.CmpTypeCard(
    type: CmpType, icon: ImageVector, iconTint: Color,
    stops: List<Pair<Float, Color>>, shadow: Color, modifier: Modifier, onClick: () -> Unit
) {
    val shape = RoundedCornerShape(22.dp)
    Column(
        modifier = modifier.fillMaxHeight().cssShadow(14.dp, shape, shadow).clip(shape)
            .background(cssLinear(135f, *stops.toTypedArray())).clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 16.dp)
    ) {
        Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.height(6.dp))
        Text(type.title, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = CmpInk, letterSpacing = (-0.3).sp)
    }
}

// ---------------- Form ----------------

// Batch item 10b: andar wale cards ke bhi colour hon — har type ka apna
// accent: upar 4dp accent strip + kicker accent rang me.
@Composable
private fun CmpCard(title: String, kicker: String, accent: Color, content: @Composable ColumnScope.() -> Unit) {
    val shape = RoundedCornerShape(22.dp)
    Column(
        modifier = Modifier.fillMaxWidth().cssShadow(5.dp, shape, CmpInk.copy(alpha = 0.08f))
            .background(Color.White, shape).border(1.dp, CmpInk.copy(alpha = 0.06f), shape).clip(shape)
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(accent))
        Column(modifier = Modifier.padding(16.dp)) {
            Text(kicker, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = accent, letterSpacing = 1.2.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(title, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = CmpInk, letterSpacing = (-0.4).sp)
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
private fun CmpInfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(label, fontSize = 13.5.sp, color = CmpGray, modifier = Modifier.weight(1f))
        Text(value, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = CmpInk)
    }
}

@Composable
private fun ColumnScope.CmpField(label: String, value: String, onValue: (String) -> Unit, placeholder: String, lines: Int = 1) {
    Text(label, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CmpInk.copy(alpha = 0.65f), modifier = Modifier.padding(top = 6.dp, bottom = 6.dp))
    val shape = RoundedCornerShape(12.dp)
    BasicTextField(
        value = value, onValueChange = onValue, singleLine = lines == 1, minLines = lines, maxLines = if (lines == 1) 1 else 5,
        textStyle = TextStyle(fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold, color = CmpInk),
        modifier = Modifier.fillMaxWidth(),
        decorationBox = { inner ->
            Box(
                modifier = Modifier.fillMaxWidth().clip(shape).background(Color(0xFFF6F2F2)).border(1.dp, CmpInk.copy(alpha = 0.1f), shape)
                    .padding(horizontal = 10.dp, vertical = 9.dp),
                contentAlignment = Alignment.TopStart
            ) {
                if (value.isEmpty()) Text(placeholder, fontSize = 13.5.sp, color = CmpInk.copy(alpha = 0.35f))
                inner()
            }
        }
    )
}

@Composable
private fun CmpChip(text: String, selected: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(999.dp)
    // Batch item 10b: chips colourful — selected pe purple → indigo gradient.
    val chipBg: Brush = if (selected) cssLinear(135f, 0f to Color(0xFF8B5CF6), 1f to Color(0xFF6366F1))
        else Brush.linearGradient(listOf(Color(0xFFF0F3F5), Color(0xFFF0F3F5)))
    Text(
        text, fontSize = 12.5.sp, fontWeight = FontWeight.Bold,
        color = if (selected) Color.White else CmpInk,
        modifier = Modifier.clip(shape)
            .background(chipBg, shape)
            .border(1.dp, if (selected) Color(0xFF8B5CF6) else CmpInk.copy(alpha = 0.1f), shape)
            .clickable { onClick() }.padding(horizontal = 12.dp, vertical = 7.dp)
    )
}

@Composable
private fun CmpChipsRow(chips: List<String>, selected: Set<String>, onToggle: (String) -> Unit) {
    @OptIn(ExperimentalLayoutApi::class)
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        chips.forEach { c -> CmpChip(c, c in selected) { onToggle(c) } }
    }
}

@Composable
internal fun ComplaintFormScreen(type: CmpType, onBack: () -> Unit, onGuiNote: (String) -> Unit) {
    val today = remember { SimpleDateFormat("EEEE, d MMMM yyyy", Locale.ENGLISH).format(Date()) }
    val shift = if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 12) "Morning" else "Evening"
    var showPreview by remember { mutableStateOf(false) }
    // Type fields (GUI state; save baad me).
    var tomSelected by remember { mutableStateOf(setOf<String>()) }
    var printerSelected by remember { mutableStateOf(setOf<String>()) }
    var tomEquip by remember { mutableStateOf("") }
    var tomIssue by remember { mutableStateOf("") }
    var agmEquipName by remember { mutableStateOf("") }
    var agmEquipSelected by remember { mutableStateOf(setOf<String>()) }
    var agmDir by remember { mutableStateOf("IN") }
    var agmIssue by remember { mutableStateOf("") }
    var tkmEquip by remember { mutableStateOf("") }
    var tkmIssue by remember { mutableStateOf("") }
    var otherEquipName by remember { mutableStateOf("") }
    var otherEquipNo by remember { mutableStateOf("") }
    var otherIssue by remember { mutableStateOf("") }
    var incTime by remember { mutableStateOf("") }
    var incEmpName by remember { mutableStateOf("") }
    var incEmpId by remember { mutableStateOf("") }
    var incType by remember { mutableStateOf("") }
    var incDetails by remember { mutableStateOf("") }
    var incInjury by remember { mutableStateOf("") }
    var incCasualties by remember { mutableStateOf("") }
    var incStatus by remember { mutableStateOf("") }
    // Batch item 10b: har type ka accent (andar wale cards + letter button).
    val accent = when (type) {
        CmpType.TOM -> Color(0xFF8B5CF6)
        CmpType.AGM -> Color(0xFFB45309)
        CmpType.TKM -> Color(0xFF2563EB)
        CmpType.OTHER -> Color(0xFF059669)
        CmpType.PMBS -> CmpRed
        CmpType.POS -> Color(0xFFEA580C)
    }

    // FIX (v1.42): form ka apna poora opaque background + click-consumer —
    // pehle background nahi tha, is liye list peeche se nazar aati thi aur
    // khaali jagah ke tap list tak pahunch jate the.
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF3EDED))
        .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { }) {
        CmpHeader(type.title, onBack)
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            CmpCard("Report Info", "AUTO DETAILS", accent) {
                CmpInfoRow("Date", today)
                CmpInfoRow("Station", "Shaheed-e-Millat")
                CmpInfoRow("Shift", shift)
                CmpInfoRow("Supervisor", "—")
            }
            when (type) {
                CmpType.TOM -> CmpCard("TOM Info", "COMPLAINT DETAILS", accent) {
                    Text("Which TOM has the issue? (select multiple)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CmpInk.copy(alpha = 0.65f), modifier = Modifier.padding(top = 6.dp, bottom = 6.dp))
                    CmpChipsRow(listOf("TOM 1", "TOM 2", "TOM 3"), tomSelected) { tomSelected = if (it in tomSelected) tomSelected - it else tomSelected + it }
                    Text("Equipment No (select printer)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CmpInk.copy(alpha = 0.65f), modifier = Modifier.padding(top = 6.dp, bottom = 6.dp))
                    CmpChipsRow(listOf("Printer 1", "Printer 2", "Printer 3"), printerSelected) {
                        printerSelected = if (it in printerSelected) printerSelected - it else printerSelected + it
                        tomEquip = (printerSelected + tomSelected).joinToString(", ")
                    }
                    CmpField("Equipment No", tomEquip, { tomEquip = it }, "Auto: printer ya TOM")
                    Text("Issue", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CmpInk.copy(alpha = 0.65f), modifier = Modifier.padding(top = 6.dp, bottom = 6.dp))
                    CmpField("", tomIssue, { tomIssue = it }, "Type the issue or pick one below")
                    Spacer(modifier = Modifier.height(8.dp))
                    CmpChipsRow(listOf("Internet Issue", "Jumping Issue", "App Not Working", "Ticket Miss Print", "Miss Print", "Slips Stuck in Printer"), setOf(tomIssue)) { tomIssue = it }
                }
                CmpType.AGM -> CmpCard("AGM Info", "COMPLAINT DETAILS", accent) {
                    CmpField("Equipment Name", agmEquipName, { agmEquipName = it }, "e.g. Turnstile")
                    Text("Equipment No", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CmpInk.copy(alpha = 0.65f), modifier = Modifier.padding(top = 6.dp, bottom = 6.dp))
                    CmpChipsRow(listOf("AGM 1", "AGM 2", "AGM 3"), agmEquipSelected) { agmEquipSelected = if (it in agmEquipSelected) agmEquipSelected - it else agmEquipSelected + it }
                    Text("Direction", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CmpInk.copy(alpha = 0.65f), modifier = Modifier.padding(top = 6.dp, bottom = 6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CmpChip("IN", agmDir == "IN") { agmDir = "IN" }
                        CmpChip("OUT", agmDir == "OUT") { agmDir = "OUT" }
                    }
                    CmpField("Issue", agmIssue, { agmIssue = it }, "Type the issue", lines = 3)
                    Spacer(modifier = Modifier.height(8.dp))
                    CmpChipsRow(listOf("Internet Issue", "Slip Notes Scanning", "Display Not Working", "App Not Open", "T-Cash Card Not Working", "Debit and T-Cash Card Not Working", "Debit Card Not Working"), setOf(agmIssue)) { agmIssue = it }
                }
                CmpType.TKM -> CmpCard("TKM Info", "COMPLAINT DETAILS", accent) {
                    CmpField("Equipment No (auto by station)", tkmEquip, { tkmEquip = it }, "e.g. tkm 01")
                    CmpField("Issue", tkmIssue, { tkmIssue = it }, "Type the issue or pick one below", lines = 3)
                    Spacer(modifier = Modifier.height(8.dp))
                    CmpChipsRow(listOf("Debit Card Not Working", "Debit and T-Cash Card Not Working", "T-Card Not Working", "Display Error", "Printer Roll Issue", "Roll Not Available"), setOf(tkmIssue)) { tkmIssue = it }
                }
                CmpType.OTHER -> CmpCard("Other Info", "COMPLAINT DETAILS", accent) {
                    CmpField("Equipment Name", otherEquipName, { otherEquipName = it }, "e.g. AC")
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CmpChip("AC", otherEquipName == "AC") { otherEquipName = "AC" }
                        CmpChip("Light", otherEquipName == "Light") { otherEquipName = "Light" }
                        CmpChip("Fan", otherEquipName == "Fan") { otherEquipName = "Fan" }
                    }
                    CmpField("Equipment No", otherEquipNo, { otherEquipNo = it }, "Equipment number")
                    CmpField("Issue", otherIssue, { otherIssue = it }, "Type the issue", lines = 3)
                }
                // Batch item 10c: POS shell — andar ki cheezein baad me ayengi.
                CmpType.POS -> CmpCard("POS Card Complaint", "COMING SOON", accent) {
                    Text("POS Card Complaint details will be added later.", fontSize = 13.5.sp, color = CmpGray, lineHeight = 20.sp)
                }
                CmpType.PMBS -> CmpCard("Incident Info", "COMPLAINT DETAILS", accent) {
                    CmpField("Incident Time", incTime, { incTime = it }, "e.g. 14:30")
                    CmpField("Employee Name", incEmpName, { incEmpName = it }, "Employee name")
                    CmpField("Employee ID", incEmpId, { incEmpId = it }, "Employee ID")
                    CmpField("Incident Type", incType, { incType = it }, "Incident type")
                    CmpField("Incident Details", incDetails, { incDetails = it }, "Incident details", lines = 3)
                    CmpField("Physical Injury", incInjury, { incInjury = it }, "Nill")
                    CmpField("Any Casualties Reported", incCasualties, { incCasualties = it }, "Nill")
                    CmpField("Status", incStatus, { incStatus = it }, "Nill")
                }
            }
            // Generate Letter (batch item 10: laal ki jagah purple gradient).
            Box(
                modifier = Modifier.fillMaxWidth().height(48.dp).cssShadow(6.dp, RoundedCornerShape(14.dp), Color(0xFF8B5CF6).copy(alpha = 0.3f))
                    .clip(RoundedCornerShape(14.dp)).background(cssLinear(135f, 0f to Color(0xFF8B5CF6), 1f to Color(0xFF6D28D9))).clickable { showPreview = true },
                contentAlignment = Alignment.Center
            ) { Text("Generate Letter", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White) }
            if (showPreview) {
                CmpCard("Letter Preview", "LETTER PREVIEW", accent) {
                    val issue = when (type) {
                        CmpType.TOM -> tomIssue
                        CmpType.AGM -> agmIssue
                        CmpType.TKM -> tkmIssue
                        CmpType.OTHER -> otherIssue
                        CmpType.PMBS -> incDetails
                        CmpType.POS -> ""
                    }
                    val equip = when (type) {
                        CmpType.TOM -> tomEquip.ifEmpty { tomSelected.joinToString(", ") }
                        CmpType.AGM -> agmEquipSelected.joinToString(", ")
                        CmpType.TKM -> tkmEquip
                        CmpType.OTHER -> otherEquipNo
                        CmpType.PMBS -> incType
                        CmpType.POS -> ""
                    }
                    Text(
                        "${type.title}\nDate: $today\nStation: Shaheed-e-Millat\nShift: $shift\nEquipment: ${equip.ifEmpty { "—" }}\nIssue: ${issue.ifEmpty { "—" }}",
                        fontSize = 13.5.sp, color = CmpInk, lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth().height(48.dp).cssShadow(6.dp, RoundedCornerShape(14.dp), Color(0xFF128C7E).copy(alpha = 0.3f))
                            .clip(RoundedCornerShape(14.dp)).background(cssLinear(135f, 0f to Color(0xFF25D366), 1f to Color(0xFF128C7E))).clickable { onGuiNote("Share on WhatsApp") },
                        contentAlignment = Alignment.Center
                    ) { Text("Share on WhatsApp", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White) }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
